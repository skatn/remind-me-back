package skatn.remindmeback.common.file.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import skatn.remindmeback.common.file.dto.UploadFile;

public interface FileStore {
    void createDirectory();
    UploadFile upload(MultipartFile file, String storeName);
    Resource download(String filename);
    boolean delete(String filename);
}
