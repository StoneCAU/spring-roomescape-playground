package roomescape.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationRequestDto(
        @NotNull String name,
        @NotNull String date,
        @NotNull Long time
) {
}
