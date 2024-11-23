package roomescape.service;

import java.security.GeneralSecurityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;
import roomescape.exception.ErrorMessage;
import roomescape.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeDao timeDao;

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
        Time time = findById(timeId);
        timeDao.delete(timeId);
    }

    public Time findById(Long timeId) {
        return timeDao.findById(timeId).orElseThrow(() -> new GeneralException(ErrorMessage.NOT_FOUND_TIME.getMessage()));
    }

    public Time findByTime(String timeString) {
        return timeDao.findByTime(timeString).orElseThrow(() -> new GeneralException(ErrorMessage.NOT_FOUND_TIME.getMessage()));
    }

    public List<Time> findAll() {
        return timeDao.findAll();
    }
}
