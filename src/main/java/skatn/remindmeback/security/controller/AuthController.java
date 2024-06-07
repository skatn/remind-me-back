package skatn.remindmeback.security.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.security.controller.dto.LogoutRequest;
import skatn.remindmeback.security.controller.dto.ReissueTokenRequest;
import skatn.remindmeback.security.controller.dto.SignupRequest;
import skatn.remindmeback.security.jwt.TokenDto;
import skatn.remindmeback.security.service.AuthService;
import skatn.remindmeback.security.service.RefreshTokenService;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) throws BindException {
        if(!Objects.equals(request.password(), request.passwordConfirm())) {
            bindingResult.reject("passwordNotMatch", "패스워드가 일치 하지 않습니다.");
        }

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        authService.join(request.name(), request.username(), request.password());
    }

    @PostMapping("/reissue-token")
    public TokenDto reissueToken(@RequestBody ReissueTokenRequest request) {
        return refreshTokenService.reissue(request.refreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody LogoutRequest request) {
        refreshTokenService.deleteRefreshTokenGroup(request.refreshToken());
    }
}
