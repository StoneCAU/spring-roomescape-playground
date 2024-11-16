package roomescape.dto.request;

public record ReservationRequestDto(
        String name,
        String date,
        Long time
) {
}
