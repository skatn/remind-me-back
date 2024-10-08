package skatn.remindmeback.common.fcm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.fcm.controller.dto.AddFcmTokenRequest;
import skatn.remindmeback.common.fcm.service.FcmService;
import skatn.remindmeback.common.security.annotation.AuthUser;
import skatn.remindmeback.common.security.dto.AccountDto;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToken(@AuthUser AccountDto accountDto, @Valid @RequestBody AddFcmTokenRequest request) {
        fcmService.addToken(accountDto.id(), request.token(), request.refreshToken());

    }

    @PostMapping("/test")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void test(@RequestBody TestRequest request) {
        fcmService.send(request.data(), request.token());
    }

    public record TestRequest (String token, Map<String, String> data){
    }
}
