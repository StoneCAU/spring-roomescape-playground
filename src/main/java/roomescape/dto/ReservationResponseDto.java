package roomescape.dto;

import roomescape.domain.Reservation;

public record ReservationResponseDto(
        Long id,
        String name,
        String date,
        String time
) {
    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTime().toString());
    }
}
