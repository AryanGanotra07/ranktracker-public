package com.codingee.ranked.ranktracker.model.rank;

import com.codingee.ranked.ranktracker.model.client.Client;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Table(name = "live_ranking")
public class LiveRanking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ElementCollection
    @Column(length = 564)
    private List<String> lowCompetitorRankings;

    @ElementCollection
    @Column(length = 564)
    private List<String> highCompetitorRankings;


    @Getter @Setter
    @Column(length = 256)
    private String keywordName;

    @Setter
    @Column(name = "rank")
    private Integer rank;

    @Getter @Setter
    @Column(length = 128)
    private String domain;

    @Column() @Getter @Setter
    private String location;

    @Column() @Getter @Setter
    private String deviceType;

    @Column() @Getter @Setter
    private String languageCode;

    @Getter @Setter
    @Column(name = "title")
    private String title;

    @Getter @Setter
    @Column(name = "description")
    private String description;

    @Getter @Setter
    @Column(name = "url")
    private String url;

    @Getter @Setter
    @Column(name = "breadcrumb")
    private String breadcrumb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public LiveRanking(String keywordName, Integer rank, String domainName, String title, String description, String url, String breadcrumb, Client client, String location, String deviceType, String languageCode) {
        this.keywordName = keywordName;
        this.rank = rank;
        this.domain = domainName;
        this.title = title;
        this.description = description;
        this.url = url;
        this.breadcrumb = breadcrumb;
        this.lowCompetitorRankings = new ArrayList<>();
        this.highCompetitorRankings = new ArrayList<>();
        this.client = client;
        this.languageCode = languageCode;
        this.location = location;
        this.deviceType = deviceType;
    }


    public Integer getRank() {
        return rank != null ? rank : 99;
    }


    public List<Ranking.CompetitorRanking> getLowCompetitorRankings() {
        Gson gson = new Gson();
        return this.lowCompetitorRankings.stream().map(v -> gson.fromJson(v, Ranking.CompetitorRanking.class)).collect(Collectors.toList());
    }

    public void setHighCompetitorRankings(List<Ranking.CompetitorRanking> competitorRankings) {
        Gson gson = new Gson();
        this.highCompetitorRankings = competitorRankings.stream().map(gson::toJson).collect(Collectors.toList());
    }

    public List<Ranking.CompetitorRanking> getHighCompetitorRankings() {
        Gson gson = new Gson();
        return this.highCompetitorRankings.stream().map(v -> gson.fromJson(v, Ranking.CompetitorRanking.class)).collect(Collectors.toList());
    }

    public void setLowCompetitorRankings(List<Ranking.CompetitorRanking> competitorRankings) {
        Gson gson = new Gson();
        this.lowCompetitorRankings = competitorRankings.stream().map(gson::toJson).collect(Collectors.toList());
    }



}
