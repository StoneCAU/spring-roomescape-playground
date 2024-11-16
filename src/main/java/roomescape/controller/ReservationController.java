package roomescape.controller;

import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.ReservationRequestDto;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final JdbcTemplate jdbcTemplate;

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

        // time 찾기
        Long timeId = Long.parseLong(request.time());
        String findTimeSql = "SELECT * from time where id = ?";
        Time time = null;
        try {
            time = jdbcTemplate.queryForObject(findTimeSql, new Object[]{timeId}, new BeanPropertyRowMapper<>(Time.class));
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Time with ID " + timeId + " not found.");
        }


        // reservation 추가
        String insertSql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
            ps.setString(1, request.name());
            ps.setString(2, request.date());
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        Reservation newReservation = Reservation.builder()
                .id(id)
                .name(request.name())
                .date(request.date())
                .time(Time.builder()
                        .id(timeId)
                        .time(time.getTime())
                        .build())
                .build();

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
