package com.codingee.ranked.ranktracker.model.keyword;


import com.codingee.ranked.ranktracker.dto.keyword.AddKeywordDTO;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.rank.Ranking;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(
        name = "keyword",
        uniqueConstraints = @UniqueConstraint(columnNames = {"domain_id", "name"})
)
public class Keyword implements Serializable {
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String location;

    @Getter @Setter
    private String deviceType;

    @Getter @Setter
    private String languageCode;


     @Setter
    private Integer rank;

     @Setter
    private Integer lastRank;

    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Ranking> rankings = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @Column(name = "domain_id", insertable = false, updatable = false)
    @Getter
    private Long domainId;

    public Integer getRank() {
        return rank != null ? rank : 99;
    }

    public Integer getLastRank() {
        return lastRank != null ? lastRank : 99;
    }

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Keyword(String name, Domain domain, String location, String deviceType) {
        this.name = name;
        this.domain = domain;
        this.deviceType = deviceType;
        this.location = location;
    }



    public Keyword(AddKeywordDTO addKeywordDTO, Domain domain) {
        this.name = addKeywordDTO.getName();
        this.domain = domain;
        this.deviceType = domain.getDeviceType();
        this.location = domain.getLocation();
        this.languageCode = domain.getLanguageCode();
    }


    public void updateRanking(Integer newRank) {
        this.lastRank = rank != null ? Integer.valueOf(rank.intValue()) : newRank != null ? Integer.valueOf(newRank.intValue()): null;
        this.rank = newRank != null ? Integer.valueOf(newRank.intValue()): null;
    }


}
