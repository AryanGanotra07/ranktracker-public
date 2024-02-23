package com.codingee.ranked.ranktracker.model.domain;

import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@NoArgsConstructor
@Table(
        name = "domains",
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "name", "is_deleted"})

)
public class Domain {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(length = 128)
    private String name;

    @Getter @Setter
    private String location;

    @Getter @Setter
    private Boolean isDraft;


    @Getter @Setter
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Getter @Setter
    private String deviceType;


    @Getter @Setter
    private String languageCode;

//    @Getter @Setter
//    private Date lastScannedDate;
//
//    @Getter @Setter
//    private Double averageRank;
//
//    @Getter @Setter
//    private Double lastAverageRank;

    @OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore()
    @Setter
    private Set<Keyword> keywords = new HashSet<>();

    @OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore()
    private Set<TrackRequest> trackRequests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "client_id", insertable = false, updatable = false)
    @Getter
    private Long clientId;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

//    @Transient
//    @Getter @Setter
//    private TrackRequest latestTrackRequest;
//
//    @Transient
//    @Getter @Setter
//    private TrackRequest latestCompletedTrackRequest;

//    @Getter
//    @Setter
//    @Column(name = "is_tracking")
//    private Boolean isTracking;



    public Domain(String name, Client client, String deviceType, String location, String languageCode) {
        this.name = name;
        this.client = client;
        this.deviceType = deviceType;
        this.location = location;
        this.isDraft = true;
        this.isDeleted = false;
        this.languageCode = languageCode;
//        this.isTracking = false;
    }


//    public Boolean getIsTracking() {
//        return !Objects.isNull(this.getLatestTrackRequest())
//                && (Objects.equals(this.getLatestTrackRequest().getStatus(), TrackRequest.TrackRequestStatus.PENDING)
//                || Objects.equals(this.getLatestTrackRequest().getStatus(), TrackRequest.TrackRequestStatus.SCANNING));
//    }
}
