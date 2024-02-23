package com.codingee.ranked.ranktracker.repo;

import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRequestRepository extends JpaRepository<TrackRequest, Long> {

    Page<TrackRequest> findByDomainIdOrderByCreatedAtDesc(Long domainId, Pageable pageable);

    Page<TrackRequest> findByDomainIdAndStatusInOrderByCreatedAtDesc(Long domainId, List<TrackRequest.TrackRequestStatus> statuses, Pageable pageable);

    @Override
    Optional<TrackRequest> findById(Long aLong);
}