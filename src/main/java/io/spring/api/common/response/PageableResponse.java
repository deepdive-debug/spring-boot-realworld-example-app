package io.spring.api.common.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record PageableResponse(
    @Schema(
            defaultValue = "0",
            description = "페이지 인덱스로, 0부터 시작합니다. 별도의 값 없이 요청 시, 0으로 설정됩니다.",
            requiredMode = REQUIRED)
        int page,
    @Schema(
            defaultValue = "10",
            description = "페이지 내 최대 응답 개수입니다. 별도의 값 없이 요청 시, 10으로 설정됩니다.",
            requiredMode = REQUIRED)
        int size,
    @Schema(defaultValue = "3", description = "전체 페이지 수 입니다.", requiredMode = REQUIRED)
        int totalPages,
    @Schema(defaultValue = "30", description = "전체 요소 개수 입니다.", requiredMode = REQUIRED)
        int totalElements,
    @Schema(
            defaultValue = "false",
            description = "현재 응답하는 페이지가 마지막 일 시, true로 설정됩니다.",
            requiredMode = REQUIRED)
        boolean isEnd) {

  public static <T> PageableResponse of(Pageable pageable, List<T> totalElements) {
    int totalPageSize = (int) Math.ceil((double) totalElements.size() / pageable.getPageSize());
    boolean isEnd = pageable.getPageNumber() + 1 >= totalPageSize;
    return PageableResponse.builder()
        .page(pageable.getPageNumber())
        .size(pageable.getPageSize())
        .totalPages(totalPageSize)
        .totalElements(totalElements.size())
        .isEnd(isEnd)
        .build();
  }
}
