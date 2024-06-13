package skatn.remindmeback.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skatn.remindmeback.common.security.annotation.AuthUser;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.member.controller.dto.MemberProfileGetResponse;
import skatn.remindmeback.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/me")
    public MemberProfileGetResponse getProfile(@AuthUser AccountDto accountDto) {
        return new MemberProfileGetResponse(accountDto.id(), accountDto.username(), accountDto.name());
    }

    @DeleteMapping("/me")
    public void deleteAccount(@AuthUser AccountDto accountDto) {
        memberService.deleteAccount(accountDto.id());
    }
}
