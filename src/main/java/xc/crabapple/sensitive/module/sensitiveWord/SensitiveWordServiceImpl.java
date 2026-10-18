package xc.crabapple.sensitive.module.sensitiveWord;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dao.SensitiveWordDao;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xc.crabapple.core.cipher.sensitive.SensitiveUtil;
import xc.crabapple.core.collection.CollectionUtil;
import xc.crabapple.core.collection.ListUtil;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.file.FileUtil;
import xc.crabapple.core.file.office.OfficeUtil;
import xc.crabapple.core.io.IOUtil;
import xc.crabapple.core.json.jackson.JacksonUtil;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.random.SnowflakeUtil;
import xc.crabapple.core.servlet.ServletUtil;
import xc.crabapple.ssm.mybatis.pagination.PageList;
import xc.crabapple.ssm.mybatis.pagination.PageResult;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service(value = "sensitiveWordServiceImpl")
public class SensitiveWordServiceImpl {

    private boolean p_initial = false;

    /**
     * xuechong 2024-12-27
     * 把所有敏感词加载到本地内存
     */
    public void p_init() {
        if (p_initial) {
            return;
        }
        p_initial = true;
        //
        List<SensitiveWordDao> sensitiveWordDaoList = MM.sensitiveWordMapper.selectList(null);
        if (CollectionUtil.isBlank(sensitiveWordDaoList)) {
            return;
        }
        for (int i = 0; i < sensitiveWordDaoList.size(); i++) {
            SensitiveWordDao sensitiveWordDao = sensitiveWordDaoList.get(i);
            if (sensitiveWordDao == null) {
                continue;
            }
            String keyword = sensitiveWordDao.getKeyword();
            if (StringUtil.isBlank(keyword)) {
                continue;
            }
            SensitiveUtil.add(keyword);
        }
    }

    @Transactional
    public S2cDto insert() {
        String text = ServletUtil.getString("text");
        if(StringUtil.isBlank(text)){
            LogUtil.error("SensitiveWordServiceImpl_insert_1", "数据为空", text);
            S2cDto s2cDto = S2cDto.failure("数据为空");
            return s2cDto;
        }
        SensitiveWordDao sensitiveWordDao = JacksonUtil.json2bean(text, SensitiveWordDao.class);
        if(sensitiveWordDao == null){
            LogUtil.error("SensitiveWordServiceImpl_insert_2", "对象为空", text);
            S2cDto s2cDto = S2cDto.failure("对象为空");
            s2cDto.setParam("text", text);
            return s2cDto;
        }
        String keyword = sensitiveWordDao.getKeyword();
        SensitiveWordDao oldSensitiveWordDao = MM.sensitiveWordMapper.selectListByKeyword(keyword);
        if(oldSensitiveWordDao != null){
            LogUtil.error("SensitiveWordServiceImpl_insert_3", "敏感词已存在", keyword, text);
            S2cDto s2cDto = S2cDto.failure("敏感词已存在");
            s2cDto.setParam("keyword", keyword);
            return s2cDto;
        }
        //
        sensitiveWordDao.setId(SnowflakeUtil.getLong());
        MM.sensitiveWordMapper.create(sensitiveWordDao);
        // 插入日志
        MM.logService.insert("content", "管理员新建敏感词:" + sensitiveWordDao.getKeyword(), "hello", "world");
        return S2cDto.okay();
    }

    /**
     * xuechong 2025-04-25
     * @return
     */
    @Transactional
    public S2cDto insertByTxt(){
        Map<String, Integer> repeatMap = deleteRepeatMap();
        //
        String importPath = MM.propertiesService.getString("sensitive_word_import_path", "./data/sensitive-words.txt");
        List<String> txtList = FileUtil.fileReadList(importPath);
        if (CollectionUtil.isBlank(txtList)) {
            LogUtil.error("SensitiveWordServiceImpl_insertByTxt_1", "敏感词导入文件不存在或为空", importPath);
            return S2cDto.failure("未找到可导入的敏感词文件：" + importPath);
        }
        for(String line : txtList){
            if(repeatMap.containsKey(line)){
                continue;
            }
            SensitiveWordDao sensitiveWordDao2 = new SensitiveWordDao();
            sensitiveWordDao2.setId(SnowflakeUtil.getLong());
            sensitiveWordDao2.setKeyword(line);
            sensitiveWordDao2.setType("其它关键词");
            sensitiveWordDao2.setDesc("");
            MM.sensitiveWordMapper.create(sensitiveWordDao2);
        }
        //
        return S2cDto.okay();
    }

    @Transactional
    public S2cDto upload(MultipartFile file) {
        Map<String, Integer> repeatMap = deleteRepeatMap();
        //
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String contentType = "";
            String originalFilename = file.getOriginalFilename();
            if (StringUtil.containOf(originalFilename, "xlsx")) {
                contentType = "xlsx";
            } else if (StringUtil.containOf(originalFilename, "xls")) {
                contentType = "xls";
            } else {
                return S2cDto.failure("请上传xlsx或xls文件");
            }
            List<List<List<String>>> list = OfficeUtil.readExcelReturnList(inputStream, contentType);
            if (CollectionUtil.isBlank(list)) {
                return S2cDto.failure("文件内容为空");
            }
            List<List<String>> lists = list.get(0);
            for (int i = 1; i < lists.size(); i++) {
                List<String> row = lists.get(i);
                if (row == null || row.size() < 4) {
                    continue;
                }
                String keyword = row.get(1);
                String type = row.get(2);
                String desc = row.get(3);
                keyword = StringUtil.getStandardStr(keyword);
                type = StringUtil.getStandardStr(type);
                desc = StringUtil.getStandardStr(desc);
                //
                if (StringUtil.isBlank(keyword)) {
                    continue;
                }
                if (repeatMap.containsKey(keyword)) {
                    continue;
                }
                repeatMap.put(keyword, 1);
                SensitiveWordDao sensitiveWordDao = new SensitiveWordDao(SnowflakeUtil.getLong(), keyword, type, desc);
                MM.sensitiveWordMapper.create(sensitiveWordDao);
            }
            // 插入日志
            MM.logService.insert("content", "管理员上传敏感词列表");
            return S2cDto.okay("导入成功");
        } catch (Throwable throwable) {
            LogUtil.error("SensitiveWordServiceImpl_upload_t1", throwable, "文件读取失败", file);
            return S2cDto.failure("文件读取失败");
        } finally {
            IOUtil.close(inputStream);
        }
    }

    /**
     * xuechong 2025-04-27
     * 一次性删除单个或多个敏感词
     * @return
     */
    @Transactional
    public S2cDto delete() {
        String text = ServletUtil.getString("text");
        if(StringUtil.isBlank(text)){
            LogUtil.error("SensitiveWordServiceImpl_delete_1", "数据为空", text);
            S2cDto s2cDto = S2cDto.failure("数据为空");
            return s2cDto;
        }
        JsonNode jsonNode = JacksonUtil.json2jsonNode(text);
        String ids = JacksonUtil.getString(jsonNode, "ids");
        if (StringUtil.isBlank(ids)) {
            LogUtil.error("SensitiveWordServiceImpl_delete_2", "ids为空", text);
            S2cDto s2cDto = S2cDto.failure("ids为空");
            return s2cDto;
        }
        String[] array = StringUtil.split(ids, ",");
        for(int i = 0; i < array.length; i++){
            String id = array[i];
            Long idLong = Long.valueOf(id);
            SensitiveWordDao sensitiveWordDao = MM.sensitiveWordMapper.selectById(idLong);
            if (sensitiveWordDao == null) {
                continue;
            }
            MM.sensitiveWordMapper.delete(idLong);
        }
        // 插入日志
        MM.logService.insert("content", "管理员删除敏感词:" + ids);
        return S2cDto.okay("删除成功");
    }

    /**
     * 去重
     * @return
     */
    @Transactional
    public S2cDto deleteRepeatKeyword(){
        Map<String, Integer> deletedRepeatMap = deleteRepeatMap();
        if(CollectionUtil.isBlank(deletedRepeatMap)){
            S2cDto s2cDto = S2cDto.okay("无重复关键字");
            return s2cDto;
        }
        //
        int size = deletedRepeatMap.size();
        S2cDto s2cDto = S2cDto.okay("去重完成");
        s2cDto.setParam("size", size);
        return s2cDto;
    }

    @Transactional
    public S2cDto update() {
        String text = ServletUtil.getString("text");
        if(StringUtil.isBlank(text)){
            LogUtil.error("SensitiveWordServiceImpl_update_1", "数据为空", text);
            S2cDto s2cDto = S2cDto.failure("数据为空");
            return s2cDto;
        }
        SensitiveWordDao sensitiveWordDao = JacksonUtil.json2bean(text, SensitiveWordDao.class);
        if(sensitiveWordDao == null){
            LogUtil.error("SensitiveWordServiceImpl_update_2", "对象为空", text);
            S2cDto s2cDto = S2cDto.failure("对象为空");
            return s2cDto;
        }
        Long id = sensitiveWordDao.getId();
        if (id == null) {
            LogUtil.error("SensitiveWordServiceImpl_update_3", "id为空", text);
            S2cDto s2cDto = S2cDto.failure("id为空");
            return s2cDto;
        }
        MM.sensitiveWordMapper.update(sensitiveWordDao);
        // 插入日志
        MM.logService.insert("content", "管理员修改敏感词:" + sensitiveWordDao.getKeyword());
        return S2cDto.okay("修改成功");
    }

    public S2cDto getList() {
        String keyword = ServletUtil.getString("keyword");
        String type = ServletUtil.getString("type");
        String desc = ServletUtil.getString("desc");
        Integer startIndex = ServletUtil.getInteger("startIndex", 0);
        Integer pageSize = ServletUtil.getInteger("pageSize", 20);
        //
        Map<String, Object> paramMap = new HashMap<>();
        if (!StringUtil.isBlank(keyword)) {
            paramMap.put("keyword", keyword);
        }
        if (!StringUtil.isBlank(type)) {
            paramMap.put("type", type);
        }
        if (!StringUtil.isBlank(desc)) {
            paramMap.put("desc", desc);
        }
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        List<String> typeList = p_getTypeList();
        paramMap.put("needTotal", "yes");
        PageList<SensitiveWordDao> sensitiveWordDaoList = MM.sensitiveWordMapper.selectList(paramMap);
        PageResult pageResult = sensitiveWordDaoList.getPageResult();
        int total = pageResult.getTotalCount();
        S2cDto s2cDto = S2cDto.okay("查询成功");
        s2cDto.setParam("data", sensitiveWordDaoList);
        s2cDto.setParam("total", total);
        s2cDto.setParam("typeList", typeList);
        return s2cDto;
    }

    /**
     * xuechong 2025-04-27
     * 按照敏感词所包含的字符串数量进行排序
     * @return
     */
    public S2cDto sortByCount(){
        String order = ServletUtil.getString("order");  // asc / desc
        List<SensitiveWordDao> list = MM.sensitiveWordMapper.selectList(null);
        Collections.sort(list, new LengthComparator(order));
        S2cDto s2cDto = S2cDto.okay();
        s2cDto.setParam("list", list);
        return s2cDto;
    }

    /**
     * xuechong 2024-12-27
     * 检查用户发送的问题是否含有敏感词，如含有敏感词则返回true，没有则返回false
     */
    public S2cDto check() {
        String question = ServletUtil.getString("question");
        question = StringUtil.getStandardStr(question);
        if (StringUtil.isBlank(question)) {
            LogUtil.error("SensitiveWordServiceImpl_check_1", "问题为空", question);
            S2cDto s2cDto = S2cDto.failure("问题为空");
            return s2cDto;
        }
        //
        p_init();
        Map<String, Object> map = SensitiveUtil.checkWithDetail(question);
        if(CollectionUtil.isBlank(map)){
            S2cDto s2cDto = S2cDto.okay();
            return s2cDto;
        }
        //
        List<String> sensitiveList = (List<String>) map.get("sensitiveList");
        S2cDto s2cDto = S2cDto.okay();
        s2cDto.setParam("sensitiveList", sensitiveList);
        return s2cDto;
    }

    /**
     * xuechong 2025-04-27
     * 关键词去重
     * @return
     */
    public Map<String, Integer> deleteRepeatMap(){
        PageList<SensitiveWordDao> sensitiveWordDaoPageList = MM.sensitiveWordMapper.selectList(null);
        if(CollectionUtil.isBlank(sensitiveWordDaoPageList)){
            return null;
        }
        Map<String, Integer> repeatMap = new HashMap<>();
        for(SensitiveWordDao sensitiveWordDao : sensitiveWordDaoPageList){
            String keyword = sensitiveWordDao.getKeyword();
            if(repeatMap.containsKey(keyword)){
                MM.sensitiveWordMapper.delete(sensitiveWordDao.getId());
                continue;
            }
            repeatMap.put(keyword, 1);
        }
        return repeatMap;
    }

    /**
     * xuechong 2025-04-27
     * 获取关键字类别集合
     * @return
     */
    private List<String> p_getTypeList(){
        List<String> list = ListUtil.getList(
                "A1包含违反社会主义核心价值观的内容：煽动颠覆国家政权、推翻社会主义制度",
                "A1包含违反社会主义核心价值观的内容：危害国家安全和利益、损害国家形象",
                "A1包含违反社会主义核心价值观的内容：中国中的一些法律、行政法规禁止的内容",
                "A1包含违反社会主义核心价值观的内容：宣扬恐怖主义、极端主义",
                "A1包含违反社会主义核心价值观的内容：宣扬民族仇恨",
                "A1包含违反社会主义核心价值观的内容：宣扬暴力、淫秽色情",
                "A5包含违反社会主义核心价值观的内容：传播虚假有害信息",
                "A2包含歧视性内容：民族歧视内容",
                "A2包含歧视性内容：国别歧视内容",
                "A2包含歧视性内容：其他方面歧视内容",
                "A2包含歧视性内容：信仰歧视内容",
                "其它：关键词"
        );
        return list;
    }

    /**
     * xuehcong 2025-04-16
     * 按照字符串长度，由少到多进行排序
     */
    private static class LengthComparator implements Comparator<SensitiveWordDao> {
        private String order;
        public LengthComparator(String order){
            this.order = order;
        }
        @Override
        public int compare(SensitiveWordDao sensitiveWordDao1, SensitiveWordDao sensitiveWordDao2) {
            String keyword1 = sensitiveWordDao1.getKeyword();
            int len1 = keyword1.length();
            String keyword2 = sensitiveWordDao2.getKeyword();
            int len2 = keyword2.length();
            if("desc".equals(this.order)){
                int result = Integer.compare(len2, len1);
                return result;
            }
            int result = Integer.compare(len1, len2);
            return result;
        }
    }

}