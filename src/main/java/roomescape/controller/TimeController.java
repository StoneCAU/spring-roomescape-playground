package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.dto.TimeRequestDto;
import roomescape.dto.TimeResponseDto;
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
    public ResponseEntity<List<TimeResponseDto>> getTimes() {
        return ResponseEntity.ok().body(timeService.findAll());
    }

    @PostMapping("/times")
    public ResponseEntity<TimeResponseDto> addTime(@RequestBody @Valid TimeRequestDto request) {
        TimeResponseDto timeResponseDto = timeService.addTime(request);

        return ResponseEntity.created(URI.create("/times/" + timeResponseDto.id())).body(timeResponseDto);
    }

    @DeleteMapping("/times/{timeId}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long timeId) {
        timeService.deleteTime(timeId);

        return ResponseEntity.noContent().build();
    }
}

