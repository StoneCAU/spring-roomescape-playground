package roomescape.dto;

import jakarta.validation.constraints.NotNull;

public record TimeRequestDto(
        @NotNull String time
) {
}
