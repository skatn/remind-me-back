package skatn.remindmeback.security.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.security.controller.dto.SignupRequest;
import skatn.remindmeback.security.service.AuthService;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) throws BindException {
        if(!Objects.equals(request.password(), request.passwordConfirm())) {
            bindingResult.reject("passwordNotMatch", "패스워드가 일치 하지 않습니다.");
            throw new BindException(bindingResult);
        }

        authService.join(request.name(), request.username(), request.password());
    }
}
