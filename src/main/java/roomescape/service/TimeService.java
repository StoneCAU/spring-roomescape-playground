package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeDao timeDao;

    @Transactional
    public Time findById(Long timeId) {
        return timeDao.findById(timeId);
    }

    @Transactional
    public Time findByTime(String timeString) {
        return timeDao.findByTime(timeString);
    }

    @Transactional
    public Time addTime(TimeRequestDto request) {
        Time time = Time.builder()
                .time(request.time())
                .build();

        timeDao.insert(time);
        return time;
    }

    @Transactional
    public void deleteTime(Long timeId) {
        timeDao.delete(timeId);
    }

    @Transactional
    public List<Time> findAll() {
        return timeDao.findAll();
    }

}