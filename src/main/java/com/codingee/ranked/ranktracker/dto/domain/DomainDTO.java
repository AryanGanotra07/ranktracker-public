package com.codingee.ranked.ranktracker.dto.domain;

import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
public class DomainDTO extends Domain {
    private Long id;
    private String name;
    private String location;
    private String deviceType;
    private TrackRequest latestTrackRequest;
    private TrackRequest latestCompletedTrackRequest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isTracking;
    private Boolean isDraft;


    public DomainDTO(Domain domain) {
        this.id = domain.getId();
        this.name = domain.getName();
        this.location = domain.getLocation();
        this.deviceType = domain.getDeviceType();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
        this.isDraft = domain.getIsDraft();
    }

    public Boolean getIsTracking() {
        return !Objects.isNull(this.getLatestTrackRequest())
                && (Objects.equals(this.getLatestTrackRequest().getStatus(), TrackRequest.TrackRequestStatus.PENDING)
                || Objects.equals(this.getLatestTrackRequest().getStatus(), TrackRequest.TrackRequestStatus.SCANNING));
    }

}
