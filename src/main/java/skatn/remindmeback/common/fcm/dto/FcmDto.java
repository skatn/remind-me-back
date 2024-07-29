package skatn.remindmeback.common.fcm.dto;

public record FcmDto(boolean validateOnly, Message message) {
    public record Message(Data data, String token) {
    }

    public record Data(String title, String body, String questionId) {
    }

}
