package com.codingee.ranked.ranktracker.repo.client;

import com.codingee.ranked.ranktracker.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);
}
