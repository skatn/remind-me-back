package skatn.remindmeback.common.similarirty;

public interface SimilarityAnalyzer {

    /**
     * 두 문자열의 유사도를 0 ~ 100 사이의 수로 나타냅니다.
     * @return 유사도
     */
    int calculate(String str1, String str2);

    /**
     * 두 문장의 의미가 같은지 비교 합니다.
     * @return 의미가 같으면 true, 아니면 false
     */
    boolean compare(String str1, String str2);
}
