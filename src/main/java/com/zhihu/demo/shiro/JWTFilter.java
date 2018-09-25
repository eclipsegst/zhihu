package com.zhihu.demo.shiro;

import com.zhihu.demo.util.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 请求头是否带有auth字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken token = new JWTToken(authorization);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token); //token verify失败时抛出 AuthenticationException异常
        String url = httpServletRequest.getRequestURI();
        if (!url.equals("/user/logout")) {
            String newToken = JWTUtil.refreshToken(authorization);
            if (!StringUtils.isEmpty(newToken)) {
                httpServletResponse.setHeader("access_token", newToken);
            }
        }
        return true;
    }

    /**
     * 调用顺序为 preHandle() => isAccessAllowed() => ...
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            executeLogin(request, response);
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        logger.info("Shiro Filter ＝＞　" + httpServletRequest.getRequestURI());
        return true;
    }

    /**
     * 提供跨域支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /401
     * 作为初版处理filter异常的方案并没有生效 如今由ErrorController处理
     */
    private void response401(ServletRequest request, ServletResponse response) {
        try {
            logger.warn("response401");
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
