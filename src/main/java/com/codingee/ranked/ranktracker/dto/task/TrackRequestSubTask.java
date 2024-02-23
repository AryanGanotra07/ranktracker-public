package com.codingee.ranked.ranktracker.dto.task;

import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;


@Getter
public class TrackRequestSubTask {

    private final String id;
    private final String parentTaskId;
    private final Set<Keyword> keywords;

    public TrackRequestSubTask(String parentTaskId, Set<Keyword> keywords) {
        this.id = UUID.randomUUID().toString();
        this.parentTaskId = parentTaskId;
        this.keywords = keywords;
    }
}
