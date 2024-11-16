package roomescape.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.service.ReservationService;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final JdbcTemplate jdbcTemplate;
    private final ReservationService reservationService;

    @GetMapping("/reservation")
    public String reservation() {
        return "new-reservation";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        String sql = "SELECT \n"
                + "    r.id as reservation_id, \n"
                + "    r.name, \n"
                + "    r.date, \n"
                + "    t.id as time_id, \n"
                + "    t.time as time_value \n"
                + "FROM reservation as r inner join time as t on r.time_id = t.id";

        List<Reservation> reservations = jdbcTemplate.query(sql, (rs, rowNum) -> Reservation.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .date(rs.getString("date"))
                .time(Time.builder().time(rs.getString("time")).build())
                .build());

        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequestDto request) {

        if (request.name() == null || request.name().isEmpty() ||
                request.date() == null || request.date().isEmpty() ||
                request.time() == null) {

            throw new IllegalArgumentException("Valid Input");
        }

        Reservation newReservation = reservationService.addReservation(request);

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(newReservation);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        String valid = "select count(*) from reservation where id = ?";
        Integer count = jdbcTemplate.queryForObject(valid, Integer.class, reservationId);

        if (count == null || count == 0) {
            throw new IllegalArgumentException("Invalid reservation");
        }

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.noContent().build();
    }

}
