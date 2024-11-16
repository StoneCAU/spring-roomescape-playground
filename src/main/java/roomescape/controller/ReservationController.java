package roomescape.controller;

import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequestDto;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        String sql = "select * from reservation";

        List<Reservation> reservations = this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Reservation reservation = Reservation.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .date(resultSet.getString("date"))
                            .time(resultSet.getString("time"))
                            .build();

                    return reservation;
                });

        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequestDto request) {
        if (request.name() == null || request.name().isEmpty() ||
                request.date() == null || request.date().isEmpty() ||
                request.time() == null || request.time().isEmpty()) {

            throw new IllegalArgumentException("Valid Input");
        }

        String sql = "insert into reservation (name, date, time) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, request.name());
            ps.setString(2, request.date());
            ps.setString(3, request.time());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        Reservation newReservation = new Reservation(id, request.name(), request.date(), request.time());

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(newReservation);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        String valid = "select count(*) from reservation where id = ?";
        Integer count = jdbcTemplate.queryForObject(valid, Integer.class, reservationId);

        if (count == null || count == 0) {
            throw new IllegalArgumentException("Invalid reservation");
        }

        String sql = "delete from reservation where id = ?";

        jdbcTemplate.update(sql, reservationId);

        return ResponseEntity.noContent().build();
    }

}
