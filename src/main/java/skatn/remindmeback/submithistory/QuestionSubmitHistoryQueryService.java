package skatn.remindmeback.submithistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.submithistory.repository.QuestionSubmitHistoryQueryRepository;
import skatn.remindmeback.submithistory.repository.dto.QuestionSubmitHistoryCountDto;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionSubmitHistoryQueryService {
    private  final QuestionSubmitHistoryQueryRepository questionSubmitHistoryQueryRepository;


    public Map<String, List<QuestionSubmitHistoryCountDto>> getDailyWithinYear(long memberId, int year) {
        return questionSubmitHistoryQueryRepository.getDailyWithinYear(memberId, year);
    }


    public List<QuestionSubmitHistoryCountDto> getLast30Days(long memberId) {
        return questionSubmitHistoryQueryRepository.getLast30Days(memberId);
    }

}
