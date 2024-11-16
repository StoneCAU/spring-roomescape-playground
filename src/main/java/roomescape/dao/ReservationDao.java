package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@RequiredArgsConstructor
@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public void insert(Reservation reservation) {

        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate());
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        });
    }
}
