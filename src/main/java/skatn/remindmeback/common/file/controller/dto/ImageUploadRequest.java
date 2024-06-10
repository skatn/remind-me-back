package skatn.remindmeback.common.file.controller.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequest(MultipartFile image) {
}
