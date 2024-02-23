package com.codingee.ranked.ranktracker.model.track_request;


import com.codingee.ranked.ranktracker.model.domain.Domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Entity
@Table(
        name = "track_requests"
)
public class TrackRequest {

    public enum TrackRequestStatus {
        SCANNING, COMPLETED, FAILED, PENDING
    }

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @Column(name = "domain_id", insertable = false, updatable = false)
    @Getter
    private Long domainId;

    @Setter
    private Double averageRank;

    @Setter
    private Double lastAverageRank;


    @Getter @Setter
    private Date scannedDate;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrackRequestStatus status;
;
    public TrackRequest(Domain domain) {
        this.domain = domain;
        this.status = TrackRequestStatus.PENDING;
    }

    public Double getAverageRank() {
        return averageRank != null ? averageRank : 99;
    }

    public Double getLastAverageRank() {
        return lastAverageRank != null ? lastAverageRank : 99;
    }

    public void updateAverageRank(Double newAvgRank) {
        lastAverageRank = this.averageRank != null ? Double.valueOf(this.averageRank.doubleValue()) : newAvgRank != null ? Double.valueOf(newAvgRank.doubleValue()) : null;
        averageRank = newAvgRank != null ? Double.valueOf(newAvgRank.doubleValue()) : null;
    }
}
