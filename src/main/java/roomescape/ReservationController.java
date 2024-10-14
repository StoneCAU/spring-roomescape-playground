package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ReservationController {
    List<Reservation> reservations = List.of(
            new Reservation(1L, "브라운", "2023-01-01", "10:00"),
            new Reservation(2L, "브라운", "2023-01-02", "11:00"),
            new Reservation(3L, "브라운", "2023-01-03", "12:00")
            );

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok().body(reservations);
    }
}
