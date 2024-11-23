package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record TimeRequestDto(
        @NotBlank String time
) {
}
