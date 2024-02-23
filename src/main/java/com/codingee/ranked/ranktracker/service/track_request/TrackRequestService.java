package com.codingee.ranked.ranktracker.service.track_request;

import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.codingee.ranked.ranktracker.repo.TrackRequestRepository;
import com.codingee.ranked.ranktracker.util.ValidationUtil;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class TrackRequestService implements ITrackRequestService{

    private final TrackRequestRepository trackRequestRepository;
    private static final String TRACK_REQUEST_WITH_ID_NOT_FOUND_MSG = "Track request with id %s not found!";
    @Override
    public TrackRequest addTrackingRequest(TrackRequest trackRequest) {
        return trackRequestRepository.save(trackRequest);
    }

    @Override
    public TrackRequest getTrackRequestById(Long id) {
        Optional<TrackRequest> trackRequest = this.trackRequestRepository.findById(id);
        if (trackRequest.isEmpty()) {
            throw new ResourceNotFoundException(String.format(TRACK_REQUEST_WITH_ID_NOT_FOUND_MSG, id));
        }
        return trackRequest.get();
    }

    @Override
    public List<TrackRequest> getTrackRequestsByDomainId(Long domainId, Pageable pageable, Set<TrackRequest.TrackRequestStatus> trackRequestStatusSet) {
        ValidationUtil.ensureNotNull(domainId, "Domain Id");
        if (Objects.isNull(trackRequestStatusSet) || trackRequestStatusSet.isEmpty()) {
            return trackRequestRepository.findByDomainIdOrderByCreatedAtDesc(domainId, pageable).get().collect(Collectors.toList());
        }
        return trackRequestRepository.findByDomainIdAndStatusInOrderByCreatedAtDesc(domainId, trackRequestStatusSet.stream().toList(), pageable).get().collect(Collectors.toList());
    }
}
