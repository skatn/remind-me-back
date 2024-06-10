package skatn.remindmeback.common.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.file.controller.dto.ImageUploadRequest;
import skatn.remindmeback.common.file.controller.dto.ImageUploadResponse;
import skatn.remindmeback.common.file.dto.UploadFile;
import skatn.remindmeback.common.file.repository.FileStore;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    @GetMapping(value = "${file.image.request-url}/{filename}", produces = "image/*")
    public Resource downloadImage(@PathVariable("filename") String filename) {
        return fileStore.download(filename);
    }

    @PostMapping(value = "${file.image.request-url}")
    @ResponseStatus(HttpStatus.CREATED)
    public ImageUploadResponse uploadImage(ImageUploadRequest request) {
        UploadFile uploadFile = fileStore.upload(request.image(), UUID.randomUUID().toString());
        return new ImageUploadResponse(uploadFile.url());
    }

}
