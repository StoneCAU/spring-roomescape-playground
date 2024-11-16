package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.service.TimeService;

@RequiredArgsConstructor
@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final TimeService timeService;

    public void insert(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate());
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        reservation.setId(id);
    }

    public void delete(Long reservationId) {
        String sql = "delete from reservation where id = ?";

        jdbcTemplate.update(sql, reservationId);
    }

    public List<Reservation> findAll() {
        String sql = "SELECT \n"
                + "    r.id as reservation_id, \n"
                + "    r.name, \n"
                + "    r.date, \n"
                + "    t.id as time_id, \n"
                + "    t.time as time_value \n"
                + "FROM reservation as r inner join time as t on r.time_id = t.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> Reservation.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .date(rs.getString("date"))
                .time(timeService.findByTime(rs.getString("time_value")))
                .build());
    }

    public Reservation findById(Long reservationId) {
        String sql = "select * from reservation where id = ?";

        return jdbcTemplate.query(sql,
                        (rs, rowNum) -> Reservation.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .date(rs.getString("date"))
                                .time(timeService.findByTime(rs.getString("time_value")))
                                .build(), reservationId)
                .stream().findFirst().orElse(null);
    }
}
