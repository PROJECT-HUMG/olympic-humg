package me.nghlong3004.olympic.api.learning.service.impl;

import me.nghlong3004.olympic.api.common.exception.DuplicateResourceException;
import me.nghlong3004.olympic.api.common.exception.ResourceNotFoundException;
import me.nghlong3004.olympic.api.learning.dto.CreateObjectiveRequest;
import me.nghlong3004.olympic.api.learning.dto.ObjectiveResponse;
import me.nghlong3004.olympic.api.learning.dto.UpdateObjectiveRequest;
import me.nghlong3004.olympic.api.learning.entity.LearningObjective;
import me.nghlong3004.olympic.api.learning.entity.Topic;
import me.nghlong3004.olympic.api.learning.mapper.ObjectiveMapper;
import me.nghlong3004.olympic.api.learning.repository.LearningObjectiveRepository;
import me.nghlong3004.olympic.api.learning.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ObjectiveServiceImpl}.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ObjectiveServiceImpl")
class ObjectiveServiceImplTest {

    @Mock private LearningObjectiveRepository objectiveRepository;
    @Mock private TopicRepository topicRepository;
    @Mock private ObjectiveMapper objectiveMapper;

    @InjectMocks private ObjectiveServiceImpl objectiveService;

    private Topic testTopic;
    private LearningObjective testObjective;
    private ObjectiveResponse testResponse;
    private UUID topicPublicId;
    private UUID objectivePublicId;

    @BeforeEach
    void setUp() {
        topicPublicId = UUID.randomUUID();
        objectivePublicId = UUID.randomUUID();

        testTopic = Topic.builder()
                .name("Đại số")
                .build();
        testTopic.setId(1L);
        testTopic.setPublicId(topicPublicId);

        testObjective = LearningObjective.builder()
                .topic(testTopic)
                .code("MATH-ALG-01")
                .description("Hiểu phép nhân ma trận")
                .bloomLevel("UNDERSTAND")
                .build();
        testObjective.setId(1L);
        testObjective.setPublicId(objectivePublicId);

        testResponse = new ObjectiveResponse(
                objectivePublicId, topicPublicId, "MATH-ALG-01", "Hiểu phép nhân ma trận", "UNDERSTAND", Instant.now()
        );
    }

    @Nested
    @DisplayName("getByPublicId()")
    class GetByPublicId {

        @Test
        @DisplayName("should return objective when found")
        void shouldReturnObjective() {
            given(objectiveRepository.findByPublicId(objectivePublicId)).willReturn(Optional.of(testObjective));
            given(objectiveMapper.toResponse(testObjective)).willReturn(testResponse);

            ObjectiveResponse result = objectiveService.getByPublicId(objectivePublicId);

            assertThat(result.code()).isEqualTo("MATH-ALG-01");
            assertThat(result.bloomLevel()).isEqualTo("UNDERSTAND");
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(objectiveRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> objectiveService.getByPublicId(unknownId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByTopic()")
    class FindByTopic {

        @Test
        @DisplayName("should return objectives for a topic")
        void shouldReturnObjectives() {
            given(topicRepository.findByPublicId(topicPublicId)).willReturn(Optional.of(testTopic));
            given(objectiveRepository.findByTopicId(testTopic.getId())).willReturn(List.of(testObjective));
            given(objectiveMapper.toResponse(testObjective)).willReturn(testResponse);

            List<ObjectiveResponse> result = objectiveService.findByTopic(topicPublicId);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).code()).isEqualTo("MATH-ALG-01");
        }

        @Test
        @DisplayName("should throw when topic not found")
        void shouldThrowWhenTopicNotFound() {
            UUID unknownTopic = UUID.randomUUID();
            given(topicRepository.findByPublicId(unknownTopic)).willReturn(Optional.empty());

            assertThatThrownBy(() -> objectiveService.findByTopic(unknownTopic))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create objective successfully")
        void shouldCreateSuccessfully() {
            var request = new CreateObjectiveRequest(topicPublicId, "MATH-ALG-02", "Áp dụng ma trận", "APPLY");
            given(objectiveRepository.existsByCode("MATH-ALG-02")).willReturn(false);
            given(topicRepository.findByPublicId(topicPublicId)).willReturn(Optional.of(testTopic));
            given(objectiveRepository.save(any(LearningObjective.class))).willAnswer(inv -> {
                LearningObjective o = inv.getArgument(0);
                o.setId(2L);
                return o;
            });
            var expected = new ObjectiveResponse(UUID.randomUUID(), topicPublicId, "MATH-ALG-02", "Áp dụng ma trận", "APPLY", Instant.now());
            given(objectiveMapper.toResponse(any(LearningObjective.class))).willReturn(expected);

            ObjectiveResponse result = objectiveService.create(request);

            assertThat(result.code()).isEqualTo("MATH-ALG-02");
            assertThat(result.bloomLevel()).isEqualTo("APPLY");
            verify(objectiveRepository).save(any(LearningObjective.class));
        }

        @Test
        @DisplayName("should throw when code already exists")
        void shouldThrowWhenDuplicateCode() {
            var request = new CreateObjectiveRequest(topicPublicId, "MATH-ALG-01", "desc", "REMEMBER");
            given(objectiveRepository.existsByCode("MATH-ALG-01")).willReturn(true);

            assertThatThrownBy(() -> objectiveService.create(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("code");
        }

        @Test
        @DisplayName("should throw when topic not found")
        void shouldThrowWhenTopicNotFound() {
            UUID unknownTopic = UUID.randomUUID();
            var request = new CreateObjectiveRequest(unknownTopic, "NEW-01", "desc", null);
            given(objectiveRepository.existsByCode("NEW-01")).willReturn(false);
            given(topicRepository.findByPublicId(unknownTopic)).willReturn(Optional.empty());

            assertThatThrownBy(() -> objectiveService.create(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Topic");
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("should update objective fields")
        void shouldUpdateSuccessfully() {
            var request = new UpdateObjectiveRequest("Mô tả mới", "ANALYZE");
            given(objectiveRepository.findByPublicId(objectivePublicId)).willReturn(Optional.of(testObjective));
            given(objectiveRepository.save(testObjective)).willReturn(testObjective);
            given(objectiveMapper.toResponse(testObjective)).willReturn(testResponse);

            objectiveService.update(objectivePublicId, request);

            assertThat(testObjective.getDescription()).isEqualTo("Mô tả mới");
            assertThat(testObjective.getBloomLevel()).isEqualTo("ANALYZE");
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("should delete objective successfully")
        void shouldDeleteSuccessfully() {
            given(objectiveRepository.findByPublicId(objectivePublicId)).willReturn(Optional.of(testObjective));

            objectiveService.delete(objectivePublicId);

            verify(objectiveRepository).delete(testObjective);
        }

        @Test
        @DisplayName("should throw when objective not found")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(objectiveRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> objectiveService.delete(unknownId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
