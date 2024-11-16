package roomescape.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Time;

@RequiredArgsConstructor
@Repository
public class TimeDao {

    private final JdbcTemplate jdbcTemplate;

    public Time findById(Long timeId) {
        String sql = "select * from time where id = ?";

        return jdbcTemplate.query(sql,
                (rs,rowNum) -> new Time(rs.getLong("id"), rs.getString("time")), timeId)
                .stream().findFirst().orElse(null);
    }
}
