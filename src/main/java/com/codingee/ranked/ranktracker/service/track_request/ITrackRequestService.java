package com.codingee.ranked.ranktracker.service.track_request;

import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;


public interface ITrackRequestService {

    TrackRequest addTrackingRequest(TrackRequest trackRequest);

    TrackRequest getTrackRequestById(Long id);

    List<TrackRequest> getTrackRequestsByDomainId(Long domainId, Pageable page, Set<TrackRequest.TrackRequestStatus> statuses);
}
