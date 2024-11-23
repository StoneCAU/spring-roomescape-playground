package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservationRequestDto(
        @NotBlank String name,
        @NotBlank String date,
        Long time
) {
}
