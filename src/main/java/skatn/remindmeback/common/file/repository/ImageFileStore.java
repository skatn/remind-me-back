package skatn.remindmeback.common.file.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.exception.IOException;
import skatn.remindmeback.common.file.dto.UploadFile;
import skatn.remindmeback.common.file.properties.ImageFileProperties;

import java.io.File;
import java.net.MalformedURLException;

@Component
@RequiredArgsConstructor
public class ImageFileStore implements FileStore {

    private final ImageFileProperties fileProperties;

    @Override
    @PostConstruct
    public void createDirectory() {
        String[] paths = fileProperties.path().split("/");
        StringBuilder filePath = new StringBuilder();
        for (String path : paths) {
            filePath.append(path).append("/");
            File directory = new File(filePath.toString());
            if(!directory.exists()) {
                boolean isCreated = directory.mkdir();
                if(!isCreated) {
                    throw new IllegalStateException("Failed to create directory: " + filePath);
                }
            }
        }
    }

    @Override
    public UploadFile upload(MultipartFile file, String storeName) {
        validationFile(file);

        String originalFilename = file.getOriginalFilename();
        String storeFilename = createStoreFileName(originalFilename, storeName);

        try {
            file.transferTo(new File(getFullPath(storeFilename)));
        } catch (Exception exception) {
            throw new IOException(ErrorCode.FILE_NOT_UPLOADED, exception);
        }

        return new UploadFile(originalFilename, storeFilename, getRequestUrl(storeFilename));
    }

    @Override
    public Resource download(String filename) {
        try {
            return new UrlResource("file:" + getFullPath(filename));
        } catch (MalformedURLException e) {
            throw new IOException(ErrorCode.FILE_NOT_LOADED, e);
        }
    }

    @Override
    public boolean delete(String filename) {
        return new File(getFullPath(filename)).delete();
    }

    private boolean hasFile(MultipartFile file) {
        return (file != null && !file.isEmpty());
    }

    private String getRequestUrl(String filename) {
        return fileProperties.requestUrl() + "/" + filename;
    }

    private String getFullPath(String filename) {
        return fileProperties.path() + filename;
    }

    private String createStoreFileName(String originalFilename, String storeName) {
        String ext = extractExt(originalFilename);
        return storeName + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private void validationFile(MultipartFile multipartFile) {
        if (!hasFile(multipartFile) || !multipartFile.getContentType().startsWith("image/")) {
            throw new IOException(ErrorCode.INVALID_FILE);
        }
    }
}
