package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.ReservationRequestDto;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeService timeService;

    @Transactional
    public Reservation addReservation(ReservationRequestDto request) {
        validate(request);

        Time time = timeService.findById(request.time());
        Reservation reservation = Reservation.builder()
                .name(request.name())
                .date(request.date())
                .time(time)
                .build();

        reservationDao.insert(reservation);
        return reservation;
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        validateReservation(reservationId);

        reservationDao.delete(reservationId);
    }

    @Transactional
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    private void validate(ReservationRequestDto request) {
        validateName(request.name());
        validateDate(request.date());
        validateTime(request.time());
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름을 입력해주세요");
        }
    }

    private void validateDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("날짜를 입력해주세요");
        }
    }

    private void validateTime(Long time) {
        if (time == null) {
            throw new IllegalArgumentException("시간을 입력해주세요");
        }
    }

    private void validateReservation(Long reservationId) {
        if (reservationDao.findById(reservationId) == null) {
            throw new IllegalArgumentException("해당 예약이 존재하지 않습니다");
        }
    }
}
