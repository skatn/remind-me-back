package skatn.remindmeback.common.fcm.dto;

public record FcmDto(boolean validateOnly, Message message) {
    public record Message(Notification notification, String token) {
    }

    public record Notification(String title, String body, String image) {
    }

}
