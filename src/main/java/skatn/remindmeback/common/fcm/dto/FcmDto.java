package skatn.remindmeback.common.fcm.dto;

import java.util.Map;

public record FcmDto(boolean validateOnly, Message message) {
    public record Message(Map<String, String> data, String token) {
    }

}
