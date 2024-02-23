package com.codingee.ranked.ranktracker.model.client;

import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.rank.LiveRanking;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
public class Client implements UserDetails {
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column(length = 128)
    private String fullName;


    @Getter
    @Column(unique = true, length = 128)
    private String email;

    @Getter @Setter
    @Column(length = 128)
    private String company;

   @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private EClientRole clientRole = EClientRole.FREE;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean locked = false;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean enabled = true;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Domain> domains;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LiveRanking> liveRankings;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Getter
    @ElementCollection
    @CollectionTable(name = "client_device_ids", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "device_id", nullable = false)
    @JsonIgnore()
    private final Set<String> deviceIds = new HashSet<>();

    public Client(String fullName, String email, String company) {
        this.fullName = fullName;
        this.email = email;
        this.company = company;
        this.domains = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        this.clientRole.getAuthorities().forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.name())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
