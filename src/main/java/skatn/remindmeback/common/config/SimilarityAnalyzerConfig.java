package skatn.remindmeback.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import skatn.remindmeback.common.similarirty.LevenshteinSimilarityAnalyzer;
import skatn.remindmeback.common.similarirty.SimilarityAnalyzer;

@Configuration
public class SimilarityAnalyzerConfig {

    @Bean
    public SimilarityAnalyzer similarityAnalyzer() {
        return new LevenshteinSimilarityAnalyzer();
    }

}
