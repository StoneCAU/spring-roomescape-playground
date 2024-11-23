package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.ErrorMessage;
import roomescape.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeService timeService;

    @Transactional
    public ReservationResponseDto addReservation(ReservationRequestDto request) {

        Time time = timeService.findById(request.time());
        Reservation reservation = Reservation.builder()
                .name(request.name())
                .date(request.date())
                .time(time)
                .build();

        reservationDao.insert(reservation);
        return ReservationResponseDto.from(reservation);
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = findById(reservationId);
        reservationDao.delete(reservationId);
    }

    public Reservation findById(Long reservationId) {
        return reservationDao.findById(reservationId).orElseThrow(() -> new GeneralException(ErrorMessage.NOT_FOUND_RESERVATION.getMessage()));
    }

    public List<ReservationResponseDto> findAll() {
        return reservationDao.findAll().stream()
                .map(ReservationResponseDto::from)
                .toList();
    }
}
