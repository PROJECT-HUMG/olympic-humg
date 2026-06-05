package me.nghlong3004.olympic.api.learning.service.impl;

import me.nghlong3004.olympic.api.common.exception.DuplicateResourceException;
import me.nghlong3004.olympic.api.common.exception.ResourceNotFoundException;
import me.nghlong3004.olympic.api.learning.dto.CreateSubjectRequest;
import me.nghlong3004.olympic.api.learning.dto.SubjectResponse;
import me.nghlong3004.olympic.api.learning.dto.UpdateSubjectRequest;
import me.nghlong3004.olympic.api.learning.entity.Subject;
import me.nghlong3004.olympic.api.learning.mapper.SubjectMapper;
import me.nghlong3004.olympic.api.learning.repository.SubjectRepository;
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
import org.springframework.data.domain.Pageable;

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
 * Unit tests for {@link SubjectServiceImpl}.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SubjectServiceImpl")
class SubjectServiceImplTest {

    @Mock private SubjectRepository subjectRepository;
    @Mock private SubjectMapper subjectMapper;

    @InjectMocks private SubjectServiceImpl subjectService;

    private Subject testSubject;
    private SubjectResponse testResponse;
    private UUID publicId;

    @BeforeEach
    void setUp() {
        publicId = UUID.randomUUID();
        testSubject = Subject.builder()
                .name("Toán học")
                .code("MATH")
                .description("Môn Toán")
                .active(true)
                .build();
        testSubject.setId(1L);
        testSubject.setPublicId(publicId);

        testResponse = new SubjectResponse(
                publicId, "Toán học", "MATH", "Môn Toán", null, 0, true, Instant.now()
        );
    }

    @Nested
    @DisplayName("getByPublicId()")
    class GetByPublicId {

        @Test
        @DisplayName("should return subject when found")
        void shouldReturnSubject() {
            given(subjectRepository.findByPublicId(publicId)).willReturn(Optional.of(testSubject));
            given(subjectMapper.toResponse(testSubject)).willReturn(testResponse);

            SubjectResponse result = subjectService.getByPublicId(publicId);

            assertThat(result.code()).isEqualTo("MATH");
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when not found")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(subjectRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> subjectService.getByPublicId(unknownId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("should return paginated subjects ordered by displayOrder")
        void shouldReturnPagedSubjects() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Subject> page = new PageImpl<>(List.of(testSubject));
            given(subjectRepository.findAllOrdered(pageable)).willReturn(page);
            given(subjectMapper.toResponse(testSubject)).willReturn(testResponse);

            Page<SubjectResponse> result = subjectService.findAll(pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).code()).isEqualTo("MATH");
        }
    }

    @Nested
    @DisplayName("findAllActive()")
    class FindAllActive {

        @Test
        @DisplayName("should return only active subjects")
        void shouldReturnActiveSubjects() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Subject> page = new PageImpl<>(List.of(testSubject));
            given(subjectRepository.findByActiveTrue(pageable)).willReturn(page);
            given(subjectMapper.toResponse(testSubject)).willReturn(testResponse);

            Page<SubjectResponse> result = subjectService.findAllActive(pageable);

            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create subject successfully")
        void shouldCreateSuccessfully() {
            var request = new CreateSubjectRequest("Vật lý", "PHYS", "Môn Lý", null);
            given(subjectRepository.existsByCode("PHYS")).willReturn(false);
            given(subjectRepository.existsByName("Vật lý")).willReturn(false);
            given(subjectRepository.save(any(Subject.class))).willAnswer(inv -> {
                Subject s = inv.getArgument(0);
                s.setId(2L);
                return s;
            });
            var expectedResponse = new SubjectResponse(
                    UUID.randomUUID(), "Vật lý", "PHYS", "Môn Lý", null, 0, true, Instant.now()
            );
            given(subjectMapper.toResponse(any(Subject.class))).willReturn(expectedResponse);

            SubjectResponse result = subjectService.create(request);

            assertThat(result.code()).isEqualTo("PHYS");
            verify(subjectRepository).save(any(Subject.class));
        }

        @Test
        @DisplayName("should throw DuplicateResourceException when code exists")
        void shouldThrowWhenDuplicateCode() {
            var request = new CreateSubjectRequest("Toán mới", "MATH", "desc", null);
            given(subjectRepository.existsByCode("MATH")).willReturn(true);

            assertThatThrownBy(() -> subjectService.create(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("code");
        }

        @Test
        @DisplayName("should throw DuplicateResourceException when name exists")
        void shouldThrowWhenDuplicateName() {
            var request = new CreateSubjectRequest("Toán học", "MATH2", "desc", null);
            given(subjectRepository.existsByCode("MATH2")).willReturn(false);
            given(subjectRepository.existsByName("Toán học")).willReturn(true);

            assertThatThrownBy(() -> subjectService.create(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("name");
        }

        @Test
        @DisplayName("should throw NullPointerException for null request")
        void shouldThrowForNullRequest() {
            assertThatThrownBy(() -> subjectService.create(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("should update subject fields")
        void shouldUpdateSuccessfully() {
            var request = new UpdateSubjectRequest("Toán cao cấp", null, null, null);
            given(subjectRepository.findByPublicId(publicId)).willReturn(Optional.of(testSubject));
            given(subjectRepository.save(testSubject)).willReturn(testSubject);
            given(subjectMapper.toResponse(testSubject)).willReturn(testResponse);

            SubjectResponse result = subjectService.update(publicId, request);

            assertThat(testSubject.getName()).isEqualTo("Toán cao cấp");
            verify(subjectRepository).save(testSubject);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException for unknown subject")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            var request = new UpdateSubjectRequest("name", null, null, null);
            given(subjectRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> subjectService.update(unknownId, request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("toggleActive()")
    class ToggleActive {

        @Test
        @DisplayName("should toggle active status from true to false")
        void shouldToggleStatus() {
            given(subjectRepository.findByPublicId(publicId)).willReturn(Optional.of(testSubject));
            given(subjectRepository.save(testSubject)).willReturn(testSubject);

            subjectService.toggleActive(publicId);

            assertThat(testSubject.isActive()).isFalse();
            verify(subjectRepository).save(testSubject);
        }
    }
}
