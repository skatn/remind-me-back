package skatn.remindmeback.common.log.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class ReqResLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put("request_id", UUID.randomUUID().toString().substring(0, 8));
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        long endTime = System.currentTimeMillis();

        String params = getParams(cachingRequestWrapper);
        String body = cachingRequestWrapper.getContentAsString();

        log.info("[REQ] method=[{}], uri=[{}], params=[{}], body=[{}]", request.getMethod(), request.getRequestURI(), params, body);
        log.info("[RES] method=[{}], uri=[{}], status=[{}], time=[{}ms]", request.getMethod(), request.getRequestURI(), response.getStatus(), endTime - startTime);

        cachingResponseWrapper.copyBodyToResponse();

        MDC.clear();
    }

    private String getParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] parameterValues = request.getParameterValues(paramName);
            params.append(paramName).append("=").append(String.join(",", parameterValues));

            if(parameterNames.hasMoreElements())
                params.append(", ");
        }

        return params.toString();
    }
}
