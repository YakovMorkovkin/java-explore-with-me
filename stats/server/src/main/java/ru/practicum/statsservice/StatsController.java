package ru.practicum.statsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsdto.HitDtoIn;
import ru.practicum.statsdto.HitDtoOut;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.statsservice.Constants.DT_PATTERN;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody HitDtoIn hitDtoIn) {
        statsRepository.save(hitMapper.toModel(hitDtoIn));
    }

    @GetMapping("/stats")
    public List<HitDtoOut> getStats(@RequestParam @DateTimeFormat(pattern = DT_PATTERN) LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = DT_PATTERN) LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false",
                                            required = false) boolean unique) {
        if (unique) {
            if (uris == null) {
                return statsRepository.getStatisticsUniq(start, end);
            } else {
                return statsRepository.getStatisticsUniqAndUris(start, end, uris);
            }
        } else {
            if (uris == null) {
                return statsRepository.getStatistics(start, end);
            } else {
                return statsRepository.getStatisticsUris(start, end, uris);
            }
        }
    }
}





