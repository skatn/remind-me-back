package skatn.remindmeback.subject.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import skatn.remindmeback.common.fixture.TagFixture;
import skatn.remindmeback.subject.entity.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagQueryRepositoryTest {

    @Autowired
    TagQueryRepository tagQueryRepository;

    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("저장된 모든 태그를 가져 온다")
    void findAll() {
        // given
        tagRepository.saveAll(TagFixture.tagsWithoutId("tag1", "tag2", "tag3"));

        // when
        List<String> findTags = tagQueryRepository.findAll(3, null);

        // then
        assertThat(findTags).containsExactly("tag1", "tag2", "tag3");
    }

    @Test
    @DisplayName("키워드가 포함된 태그를 가져 온다")
    void findAllWithKeyword() {
        // given
        tagRepository.saveAll(TagFixture.tagsWithoutId("tag1", "tag2", "tag3"));
        List<Tag> tags = tagRepository.saveAll(TagFixture.tagsWithoutId("abc1", "abc2", "abc3"));

        // when
        List<String> findTags = tagQueryRepository.findAll(tags.size(), "abc");

        // then
        assertThat(findTags).containsExactly("abc1", "abc2", "abc3");
    }


    @TestConfiguration
    static class Config {
        @Bean
        public TagQueryRepository tagQueryRepository(EntityManager entityManager) {
            return new TagQueryRepository(entityManager);
        }
    }

}