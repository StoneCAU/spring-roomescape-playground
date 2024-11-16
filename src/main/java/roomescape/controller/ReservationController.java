package roomescape.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequestDto;
import roomescape.service.ReservationService;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/reservation")
    public String reservation() {
        return "new-reservation";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.findAll();

        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequestDto request) {

        if (request.name() == null || request.name().isEmpty() ||
                request.date() == null || request.date().isEmpty() ||
                request.time() == null) {

            throw new IllegalArgumentException("Valid Input");
        }

        Reservation newReservation = reservationService.addReservation(request);

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(newReservation);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
//        String valid = "select count(*) from reservation where id = ?";
//        Integer count = jdbcTemplate.queryForObject(valid, Integer.class, reservationId);
//
//        if (count == null || count == 0) {
//            throw new IllegalArgumentException("Invalid reservation");
//        }

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.noContent().build();
    }

}
