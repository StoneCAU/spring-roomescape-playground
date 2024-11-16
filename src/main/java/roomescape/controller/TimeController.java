package roomescape.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;
import roomescape.service.TimeService;

@Controller
@RequiredArgsConstructor
public class TimeController {
    private final TimeService timeService;

    @GetMapping("/time")
    public String time() {
        return "time";
    }

    @GetMapping("/times")
    public ResponseEntity<List<Time>> getTimes() {
        return ResponseEntity.ok().body(timeService.findAll());
    }

    @PostMapping("/times")
    public ResponseEntity<Time> addTime(@RequestBody TimeRequestDto request) {
        Time newTime = timeService.addTime(request);

        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
    }

    @DeleteMapping("/times/{timeId}")
    public ResponseEntity<Time> deleteTime(@PathVariable Long timeId) {
        timeService.deleteTime(timeId);

        return ResponseEntity.noContent().build();
    }
}

