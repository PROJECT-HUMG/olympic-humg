package me.nghlong3004.olympic.api.learning.service.impl;

import me.nghlong3004.olympic.api.common.exception.ResourceNotFoundException;
import me.nghlong3004.olympic.api.learning.dto.CreateTopicRequest;
import me.nghlong3004.olympic.api.learning.dto.TopicResponse;
import me.nghlong3004.olympic.api.learning.dto.UpdateTopicRequest;
import me.nghlong3004.olympic.api.learning.entity.Subject;
import me.nghlong3004.olympic.api.learning.entity.Topic;
import me.nghlong3004.olympic.api.learning.mapper.TopicMapper;
import me.nghlong3004.olympic.api.learning.repository.SubjectRepository;
import me.nghlong3004.olympic.api.learning.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
 * Unit tests for {@link TopicServiceImpl}.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TopicServiceImpl")
class TopicServiceImplTest {

    @Mock private TopicRepository topicRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private TopicMapper topicMapper;

    @InjectMocks private TopicServiceImpl topicService;

    private Subject testSubject;
    private Topic testTopic;
    private TopicResponse testResponse;
    private UUID subjectPublicId;
    private UUID topicPublicId;

    @BeforeEach
    void setUp() {
        subjectPublicId = UUID.randomUUID();
        topicPublicId = UUID.randomUUID();

        testSubject = Subject.builder()
                .name("Toán học")
                .code("MATH")
                .build();
        testSubject.setId(1L);
        testSubject.setPublicId(subjectPublicId);

        testTopic = Topic.builder()
                .subject(testSubject)
                .name("Đại số")
                .description("Chương Đại số")
                .active(true)
                .build();
        testTopic.setId(1L);
        testTopic.setPublicId(topicPublicId);

        testResponse = new TopicResponse(
                topicPublicId, subjectPublicId, null, "Đại số", "Chương Đại số", 0, true, Instant.now()
        );
    }

    @Nested
    @DisplayName("getByPublicId()")
    class GetByPublicId {

        @Test
        @DisplayName("should return topic when found")
        void shouldReturnTopic() {
            given(topicRepository.findByPublicId(topicPublicId)).willReturn(Optional.of(testTopic));
            given(topicMapper.toResponse(testTopic)).willReturn(testResponse);

            TopicResponse result = topicService.getByPublicId(topicPublicId);

            assertThat(result.name()).isEqualTo("Đại số");
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(topicRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> topicService.getByPublicId(unknownId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findBySubject()")
    class FindBySubject {

        @Test
        @DisplayName("should return topics for a subject")
        void shouldReturnTopics() {
            var pageable = PageRequest.of(0, 10);
            Page<Topic> page = new PageImpl<>(List.of(testTopic));
            given(topicRepository.findBySubjectPublicId(subjectPublicId, pageable)).willReturn(page);
            given(topicMapper.toResponse(testTopic)).willReturn(testResponse);

            Page<TopicResponse> result = topicService.findBySubject(subjectPublicId, pageable);

            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create root topic successfully")
        void shouldCreateRootTopic() {
            var request = new CreateTopicRequest(subjectPublicId, null, "Hình học", "Chương Hình");
            given(subjectRepository.findByPublicId(subjectPublicId)).willReturn(Optional.of(testSubject));
            given(topicRepository.save(any(Topic.class))).willAnswer(inv -> {
                Topic t = inv.getArgument(0);
                t.setId(2L);
                return t;
            });
            var expected = new TopicResponse(UUID.randomUUID(), subjectPublicId, null, "Hình học", "Chương Hình", 0, true, Instant.now());
            given(topicMapper.toResponse(any(Topic.class))).willReturn(expected);

            TopicResponse result = topicService.create(request);

            assertThat(result.name()).isEqualTo("Hình học");
            assertThat(result.parentId()).isNull();
            verify(topicRepository).save(any(Topic.class));
        }

        @Test
        @DisplayName("should create child topic with parent")
        void shouldCreateChildTopic() {
            UUID parentId = topicPublicId;
            var request = new CreateTopicRequest(subjectPublicId, parentId, "Đại số tuyến tính", null);
            given(subjectRepository.findByPublicId(subjectPublicId)).willReturn(Optional.of(testSubject));
            given(topicRepository.findByPublicId(parentId)).willReturn(Optional.of(testTopic));
            given(topicRepository.save(any(Topic.class))).willAnswer(inv -> {
                Topic t = inv.getArgument(0);
                t.setId(3L);
                return t;
            });
            var expected = new TopicResponse(UUID.randomUUID(), subjectPublicId, parentId, "Đại số tuyến tính", null, 0, true, Instant.now());
            given(topicMapper.toResponse(any(Topic.class))).willReturn(expected);

            TopicResponse result = topicService.create(request);

            assertThat(result.parentId()).isEqualTo(parentId);
        }

        @Test
        @DisplayName("should throw when subject not found")
        void shouldThrowWhenSubjectNotFound() {
            UUID unknownSubject = UUID.randomUUID();
            var request = new CreateTopicRequest(unknownSubject, null, "Topic", null);
            given(subjectRepository.findByPublicId(unknownSubject)).willReturn(Optional.empty());

            assertThatThrownBy(() -> topicService.create(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Subject");
        }

        @Test
        @DisplayName("should throw when parent topic not found")
        void shouldThrowWhenParentNotFound() {
            UUID unknownParent = UUID.randomUUID();
            var request = new CreateTopicRequest(subjectPublicId, unknownParent, "Topic", null);
            given(subjectRepository.findByPublicId(subjectPublicId)).willReturn(Optional.of(testSubject));
            given(topicRepository.findByPublicId(unknownParent)).willReturn(Optional.empty());

            assertThatThrownBy(() -> topicService.create(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("parent");
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("should update topic fields")
        void shouldUpdateSuccessfully() {
            var request = new UpdateTopicRequest("Đại số mới", null, 5);
            given(topicRepository.findByPublicId(topicPublicId)).willReturn(Optional.of(testTopic));
            given(topicRepository.save(testTopic)).willReturn(testTopic);
            given(topicMapper.toResponse(testTopic)).willReturn(testResponse);

            topicService.update(topicPublicId, request);

            assertThat(testTopic.getName()).isEqualTo("Đại số mới");
            assertThat(testTopic.getDisplayOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("toggleActive()")
    class ToggleActive {

        @Test
        @DisplayName("should toggle active from true to false")
        void shouldToggle() {
            given(topicRepository.findByPublicId(topicPublicId)).willReturn(Optional.of(testTopic));
            given(topicRepository.save(testTopic)).willReturn(testTopic);

            topicService.toggleActive(topicPublicId);

            assertThat(testTopic.isActive()).isFalse();
        }
    }
}
