package skatn.remindmeback.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import skatn.remindmeback.common.controlleradvice.dto.ErrorResponse;
import skatn.remindmeback.common.exception.ErrorCode;

import java.io.IOException;

public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorCode.PERMISSION_DENIED.getStatus());
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(ErrorCode.PERMISSION_DENIED));
    }
}
