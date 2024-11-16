package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Time;

@RequiredArgsConstructor
@Repository
public class TimeDao {

    private final JdbcTemplate jdbcTemplate;

    public void insert(Time time) {
        String sql = "insert into time (time) values ?";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, time.getTime());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        time.setId(id);
    }

    public void delete(Long timeId) {
        String sql = "delete from time where id = ?";

        jdbcTemplate.update(sql, timeId);
    }

    public Time findById(Long timeId) {
        String sql = "select * from time where id = ?";

        return jdbcTemplate.query(sql,
                (rs,rowNum) -> new Time(rs.getLong("id"), rs.getString("time")), timeId)
                .stream().findFirst().orElse(null);
    }

    public List<Time> findAll() {
        String sql = "select * from time";

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Time.builder()
                        .id(resultSet.getLong("id"))
                        .time(resultSet.getString("time"))
                        .build());
    }
}
