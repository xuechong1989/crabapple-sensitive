package xc.crabapple.sensitive.module.sensitiveWord;

import xc.crabapple.sensitive.common.configuration.MM;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xc.crabapple.core.net.http.ContentTypeUtil;

/**
 * 系统参数控制器
 */
@Controller
@RequestMapping(value = "/api/crabapple/sensitive/sensitiveWord")
public class SensitiveWordController {

    @RequestMapping(value="/insert", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto insert() {
        S2cDto s2cDto = MM.sensitiveWordService.insert();
        return s2cDto;
    }

    @RequestMapping(value="/insertByTxt", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto insertByTxt() {
        S2cDto s2cDto = MM.sensitiveWordService.insertByTxt();
        return s2cDto;
    }

    @RequestMapping(value="/upload", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto upload(MultipartFile file) {
        S2cDto s2cDto = MM.sensitiveWordService.upload(file);
        return s2cDto;
    }

    @RequestMapping(value="/delete", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto delete() {
        S2cDto s2cDto = MM.sensitiveWordService.delete();
        return s2cDto;
    }

    @RequestMapping(value="/deleteRepeatKeyword", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto deleteRepeatKeyword() {
        S2cDto s2cDto = MM.sensitiveWordService.deleteRepeatKeyword();
        return s2cDto;
    }

    @RequestMapping(value="/update", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto update() {
        S2cDto s2cDto = MM.sensitiveWordService.update();
        return s2cDto;
    }

    @RequestMapping(value="/getList", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto getList() {
        S2cDto s2cDto = MM.sensitiveWordService.getList();
        return s2cDto;
    }

    @RequestMapping(value="/sortByCount", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto sortByCount() {
        S2cDto s2cDto = MM.sensitiveWordService.sortByCount();
        return s2cDto;
    }

    @RequestMapping(value="/check", method={RequestMethod.POST}, produces={ContentTypeUtil.application_json})
    @ResponseBody
    public S2cDto check() {
        S2cDto s2cDto = MM.sensitiveWordService.check();
        return s2cDto;
    }

}