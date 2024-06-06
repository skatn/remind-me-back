package skatn.remindmeback.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import skatn.remindmeback.common.controlleradvice.dto.ErrorResponse;
import skatn.remindmeback.common.exception.ErrorCode;

import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");

        ErrorCode errorCode = ErrorCode.valueOf(exception);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus());

        objectMapper.writeValue(response.getWriter(), new ErrorResponse(errorCode));
    }
}
