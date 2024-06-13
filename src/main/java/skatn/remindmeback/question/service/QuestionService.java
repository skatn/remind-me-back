package skatn.remindmeback.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.similarirty.SimilarityAnalyzer;
import skatn.remindmeback.question.dto.QuestionCreateDto;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.dto.QuestionUpdateDto;
import skatn.remindmeback.question.entity.Answer;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.QuestionRepository;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.SubjectRepository;
import skatn.remindmeback.submithistory.entity.HistoryStatus;
import skatn.remindmeback.submithistory.entity.QuestionSubmitHistory;
import skatn.remindmeback.submithistory.repository.QuestionSubmitHistoryRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final SimilarityAnalyzer similarityAnalyzer;
    private final QuestionSubmitHistoryRepository questionSubmitHistoryRepository;

    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasWritePermission(authentication, #createDto.subjectId)")
    public long create(QuestionCreateDto createDto) {
        Subject subject = subjectRepository.findById(createDto.subjectId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        Question question = Question.builder()
                .question(createDto.question())
                .questionType(createDto.questionType())
                .explanation(createDto.explanation())
                .subject(subject)
                .build();

        Set<Answer> answers = createDto.answers().stream()
                .map(answer -> Answer.builder()
                        .answer(answer.answer())
                        .isAnswer(answer.isAnswer())
                        .build())
                .collect(Collectors.toSet());

        question.changeAnswers(answers);

        return questionRepository.save(question).getId();
    }

    @PreAuthorize("@questionAuthorizationManager.hasReadPermission(authentication, #questionId)")
    public QuestionDto findOne(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        return new QuestionDto(question);
    }

    @Transactional
    @PreAuthorize("@questionAuthorizationManager.hasWritePermission(authentication, #updateDto.questionId())")
    public void update(QuestionUpdateDto updateDto) {
        Question question = questionRepository.findById(updateDto.questionId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        question.changeQuestion(updateDto.question());
        question.changeQuestionType(updateDto.questionType());
        question.changeExplanation(updateDto.explanation());
        question.changeAnswers(updateDto.answers().stream()
                .map(answerDto -> Answer.builder()
                        .answer(answerDto.answer())
                        .isAnswer(answerDto.isAnswer())
                        .build())
                .collect(Collectors.toSet()));
    }

    @Transactional
    @PreAuthorize("@questionAuthorizationManager.hasDeletePermission(authentication, #questionId)")
    public void delete(long questionId) {
        questionRepository.findById(questionId)
                .ifPresent(questionRepository::delete);
    }

    @Transactional
    @PreAuthorize("@questionAuthorizationManager.hasReadPermission(authentication, #questionId)")
    public boolean submit(long questionId, String submittedAnswer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        QuestionType questionType = question.getQuestionType();

        QuestionSubmitHistory history = switch(questionType) {
            case CHOICE -> markingChoice(question, submittedAnswer);
            case DESCRIPTIVE -> markingDescriptive(question, submittedAnswer);
        };

        questionSubmitHistoryRepository.save(history);

        return history.getStatus() == HistoryStatus.CORRECT;
    }

    private QuestionSubmitHistory markingChoice(Question question, String submittedAnswer) {
        QuestionSubmitHistory.QuestionSubmitHistoryBuilder<?, ?> builder = QuestionSubmitHistory.builder()
                .question(question)
                .submittedAnswer(submittedAnswer);

        for (Answer answer : question.getAnswers()) {
            if (answer.isAnswer() && answer.getAnswer().equals(submittedAnswer)) {
                return builder.status(HistoryStatus.CORRECT).build();
            }
        }

        return builder.status(HistoryStatus.INCORRECT).build();
    }

    private QuestionSubmitHistory markingDescriptive(Question question, String submittedAnswer) {
        Answer answer = question.getAnswers().stream().findAny()
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANSWER_NOT_FOUND));

        boolean correct = similarityAnalyzer.compare(answer.getAnswer(), submittedAnswer);

        return QuestionSubmitHistory.builder()
                .status(correct ? HistoryStatus.CORRECT : HistoryStatus.INCORRECT)
                .question(question)
                .submittedAnswer(submittedAnswer)
                .build();
    }

}