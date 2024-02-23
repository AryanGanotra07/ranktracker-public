package com.codingee.ranked.ranktracker.model.rank;

import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@Table(name = "ranking")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Setter
    @Column(name = "rank")
    private Integer rank;

    @Getter @Setter
    @Column(name = "domain")
    private String domain;

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

    @Getter @Setter
    @Column(name = "keyword_name")
    private String keywordName;

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

    public Ranking(Keyword keyword, Integer rank, String domain, String title, String description, String url, String breadcrumb) {
        this.keyword = keyword;
        this.rank = rank;
        this.domain = domain;
        this.title = title;
        this.description = description;
        this.url = url;
        this.breadcrumb = breadcrumb;
        this.keywordName = keyword.getName();
        this.lowCompetitorRankings = new ArrayList<>();
        this.highCompetitorRankings = new ArrayList<>();
    }

    public Integer getRank() {
        return rank != null ? rank : 99;
    }

    public List<CompetitorRanking> getLowCompetitorRankings() {
        Gson gson = new Gson();
        return this.lowCompetitorRankings.stream().map(v -> gson.fromJson(v, CompetitorRanking.class)).collect(Collectors.toList());
    }

    public void setHighCompetitorRankings(List<CompetitorRanking> competitorRankings) {
        Gson gson = new Gson();
        this.highCompetitorRankings = competitorRankings.stream().map(gson::toJson).collect(Collectors.toList());
    }

    public List<CompetitorRanking> getHighCompetitorRankings() {
        Gson gson = new Gson();
        return this.highCompetitorRankings.stream().map(v -> gson.fromJson(v, CompetitorRanking.class)).collect(Collectors.toList());
    }

    public void setLowCompetitorRankings(List<CompetitorRanking> competitorRankings) {
        Gson gson = new Gson();
        this.lowCompetitorRankings = competitorRankings.stream().map(gson::toJson).collect(Collectors.toList());
    }


    public record CompetitorRanking(String domain, String url, String breadcrumb, Integer rank, String title,
                             String description) {
    }

}
