package com.codingee.ranked.ranktracker.api.pingback;

import com.codingee.ranked.ranktracker.dto.task.TrackJobCompletedQueueTask;
import com.codingee.ranked.ranktracker.service.task.ITaskSchedularService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ping-back/task")
@RequiredArgsConstructor
@Slf4j
public class PingbackController {

    private final ITaskSchedularService taskSchedulerService;

    @GetMapping("")
    public void processTask(@RequestParam String id, @RequestParam String tag) {
        log.info("Received task ping back {} {}", id, tag);
        this.taskSchedulerService.processTrackJobCompleted(new TrackJobCompletedQueueTask(id, tag));
    }

}
