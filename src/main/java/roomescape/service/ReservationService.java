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
        reservationDao.delete(reservationId);
    }

    @Transactional
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }
}