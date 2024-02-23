package com.codingee.ranked.ranktracker.service.task;

import com.codingee.ranked.ranktracker.dto.ranking.SeoTasksCompletedResponse;
import com.codingee.ranked.ranktracker.dto.task.TrackJobCompletedQueueTask;
import com.codingee.ranked.ranktracker.service.seo.ISeoService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class ScheduledJob {


    private final ISeoService seoService;
    private final ITaskSchedularService taskSchedularService;


    @Scheduled(cron = "0 * * * * *") // Cron expression for running every minute
    public void execute() {
        SeoTasksCompletedResponse tasksCompletedResponse = this.seoService.getCompletedTasks();
        if (tasksCompletedResponse != null) {
            tasksCompletedResponse.tasks().forEach(v -> {
                System.out.println(v.result_count());
                if (Objects.nonNull(v.result())) {
                    v.result().forEach(r -> {
                        System.out.println("Processing for" + r.id() + " " + r.tag());
                        this.taskSchedularService.processTrackJobCompleted(new TrackJobCompletedQueueTask(r.id(), r.tag()));
                    });
                }
            });
        }
    }
}
