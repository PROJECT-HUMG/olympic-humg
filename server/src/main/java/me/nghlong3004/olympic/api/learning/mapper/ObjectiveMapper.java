package me.nghlong3004.olympic.api.learning.mapper;

import me.nghlong3004.olympic.api.learning.dto.ObjectiveResponse;
import me.nghlong3004.olympic.api.learning.entity.LearningObjective;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for {@link LearningObjective} entity and DTOs.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ObjectiveMapper {

    @Mapping(source = "publicId", target = "id")
    @Mapping(source = "topic.publicId", target = "topicId")
    ObjectiveResponse toResponse(LearningObjective objective);
}
