package xc.crabapple.sensitive.common.configuration;

import xc.crabapple.sensitive.common.domain.dao.UserDao;
import xc.crabapple.sensitive.common.domain.dto.S2cDto;
import xc.crabapple.sensitive.common.utils.ContextUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import xc.crabapple.core.collection.StringUtil;
import xc.crabapple.core.log.LogUtil;
import xc.crabapple.core.servlet.ServletUtil;
import xc.crabapple.ssm.spring.SpringUtil;
import xc.crabapple.ssm.spring.SsmFilter;
import xc.crabapple.ssm.spring.SsmRequestUtil;
import xc.crabapple.ssm.spring.SsmRequestWrapper;

@Component
public class AppFilter extends SsmFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            SsmRequestWrapper request = SsmRequestUtil.getMyHttpServletRequestWrapper(httpServletRequest, response);
            SpringUtil.setRequestResponse(request, response);
            //
            final String requestURI = request.getRequestURI();
            //
            if("/favicon.ico".equals(requestURI)){
                return;
            }
            //
            String method = httpServletRequest.getMethod();
            method = StringUtil.toUpperCase(method);
            if(StringUtil.containOfOne(method, "OPTIONS")){
                filterChain.doFilter(request, response);
                return;
            }
            // 放行接口
            if (StringUtil.containOfOne(requestURI,
                    "/api/crabapple/sensitive/auth/login"  // 登录
            )) {
                filterChain.doFilter(request, response);
                return;
            }
            //校验用户是否登录
            UserDao currentUser = ContextUtil.getCurrentUser();
            if (currentUser == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                LogUtil.error("AppFilter_doFilter_51", "用户未登录");
                return;
            }
            //查看用户是否会话超时
            S2cDto s2cDto = ContextUtil.tokenMonitor();
            String success = s2cDto.getSuccess();
            if (!"yes".equals(success)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                LogUtil.error("AppFilter_doFilter_e55", "用户登录超时");
                return;
            }
            //校验用户权限
            if (!StringUtil.containOfOne(requestURI,
                    "/api/crabapple/sensitive/log/conversation",
                    "/api/crabapple/sensitive/log/getSessions",
                    "/api/crabapple/sensitive/log/delSessions")) {
                Long id = currentUser.getId();
                if (id != 1) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    LogUtil.error("AppFilter_doFilter_58", "用户权限不足",requestURI);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }catch (Throwable throwable) {
            final String requestURI = ServletUtil.getRequestURI();
            LogUtil.error("AppFilter_doFilter_t1", "过滤器异常", requestURI);
            AppResponseBodyAdvice.sendJson2client(false, "filter_exception", "系统可能被非正常访问");
        }
    }

}