package skatn.remindmeback.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.security.annotation.AuthUser;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.member.controller.dto.MemberProfileGetResponse;
import skatn.remindmeback.member.controller.dto.MemberProfileUpdateRequest;
import skatn.remindmeback.member.service.MemberCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberCommandService memberCommandService;


    @GetMapping("/me")
    public MemberProfileGetResponse getProfile(@AuthUser AccountDto accountDto) {
        return new MemberProfileGetResponse(accountDto.id(), accountDto.username(), accountDto.name());
    }

    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@AuthUser AccountDto accountDto, @Valid @RequestBody MemberProfileUpdateRequest request) {
        memberCommandService.update(accountDto.id(), request.name());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@AuthUser AccountDto accountDto) {
        memberCommandService.deleteAccount(accountDto.id());
    }
}
