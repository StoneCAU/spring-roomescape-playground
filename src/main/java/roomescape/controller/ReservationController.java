package roomescape.controller;

import java.sql.PreparedStatement;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;

@RestController
public class ReservationController {
    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);
    private final JdbcTemplate jdbcTemplate;

    public ReservationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


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
                    Reservation reservation = new Reservation(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("date"),
                            resultSet.getString("time")
                    );
                    return reservation;
                });

        System.out.println(reservations);

        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) {
        String sql = "insert into reservation (name, date, time) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate());
            ps.setString(3, reservation.getTime());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        Reservation newReservation = new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(newReservation);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, reservationId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
