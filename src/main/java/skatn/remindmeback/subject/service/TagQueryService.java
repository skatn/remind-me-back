package skatn.remindmeback.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.subject.repository.TagQueryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagQueryService {

    private final TagQueryRepository tagQueryRepository;

    public List<String> getTags(int size, String keyword) {
        return tagQueryRepository.findAll(size, keyword);
    }
}
