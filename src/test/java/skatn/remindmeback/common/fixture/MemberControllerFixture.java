package skatn.remindmeback.common.fixture;

import skatn.remindmeback.member.controller.dto.MemberProfileUpdateRequest;

public class MemberControllerFixture {

    public static MemberProfileUpdateRequest profileUpdateRequest() {
        return new MemberProfileUpdateRequest("newName");
    }
}
