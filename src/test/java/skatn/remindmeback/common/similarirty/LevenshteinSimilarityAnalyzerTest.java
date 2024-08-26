package skatn.remindmeback.common.similarirty;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevenshteinSimilarityAnalyzerTest {

    LevenshteinSimilarityAnalyzer levenshteinSimilarityAnalyzer = new LevenshteinSimilarityAnalyzer();

    @Test
    @DisplayName("문자열 유사도가 70% 보다 높으면 정답이다")
    void correct() {
        // given
        String str1 = "Hello world";
        String str2 = "Hello wolrd";

        // when
        int calculate = levenshteinSimilarityAnalyzer.calculate(str1, str2);
        boolean correct = levenshteinSimilarityAnalyzer.compare(str1, str2);

        // then
        assertThat(calculate).isGreaterThan(70);
        assertThat(correct).isTrue();
    }
    @Test
    @DisplayName("문자열 유사도가 70% 이하 이면 오답이다")
    void incorrect() {
        // given
        String str1 = "Hello world";
        String str2 = "world hello";

        // when
        int calculate = levenshteinSimilarityAnalyzer.calculate(str1, str2);
        boolean correct = levenshteinSimilarityAnalyzer.compare(str1, str2);

        // then
        assertThat(calculate).isLessThanOrEqualTo(70);
        assertThat(correct).isFalse();
    }

}