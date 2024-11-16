package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeDao timeDao;

    @Transactional
    public Time findById(Long timeId) {
        return timeDao.findById(timeId);
    }

}
