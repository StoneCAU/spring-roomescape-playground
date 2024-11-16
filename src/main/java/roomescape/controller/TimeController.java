package roomescape.controller;

import java.net.URI;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;

@Controller
@RequiredArgsConstructor
public class TimeController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/time")
    public String time() {
        return "time";
    }

    @GetMapping("/times")
    public ResponseEntity<List<Time>> getTimes() {
        String sql = "select * from time";

        List<Time> times = this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Time.builder()
                        .id(resultSet.getLong("id"))
                        .time(resultSet.getString("time"))
                        .build());

        return ResponseEntity.ok().body(times);
    }

    @PostMapping("/times")
    public ResponseEntity<Time> addTime(@RequestBody TimeRequestDto request) {
        if (request.time() == null) {
            throw new IllegalArgumentException("Time must not be null");
        }

        String sql = "insert into time (time) values ?";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, request.time());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        Time newTime = Time.builder()
                .id(id)
                .time(request.time())
                .build();

        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
    }

    @DeleteMapping("/times/{timeId}")
    public ResponseEntity<Time> deleteTime(@PathVariable Long timeId) {
        String valid = "select count(*) from time where id = ?";
        Integer count = jdbcTemplate.queryForObject(valid, Integer.class, timeId);

        if (count == null || count == 0) {
            throw new IllegalArgumentException("Invalid time id");
        }

        String sql = "delete from time where id = ?";

        jdbcTemplate.update(sql, timeId);

        return ResponseEntity.noContent().build();
    }
}

