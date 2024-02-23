package com.codingee.ranked.ranktracker.repo.domain;

import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDomainRepo extends JpaRepository<Domain, Long> {

    Optional<Domain> findByIdAndIsDeletedFalse(Long id);

    Optional<List<Domain>> findByClientAndIsDeletedFalse(Client client);
}
