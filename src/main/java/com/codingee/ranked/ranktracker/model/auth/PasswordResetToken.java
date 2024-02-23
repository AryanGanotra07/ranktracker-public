package com.codingee.ranked.ranktracker.model.auth;


import com.codingee.ranked.ranktracker.model.client.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(
        name = "password_reset_token",
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "token"})

)
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @Getter()
    @OneToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;

    @Getter()
    private LocalDateTime expiryDate;

    public PasswordResetToken(Client client, String token) {
        this.client = client;
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusDays(1);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
