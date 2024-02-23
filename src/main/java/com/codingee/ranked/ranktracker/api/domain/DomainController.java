package com.codingee.ranked.ranktracker.api.domain;


import com.codingee.ranked.ranktracker.dto.domain.AddDomainDTO;
import com.codingee.ranked.ranktracker.dto.domain.DomainDTO;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.codingee.ranked.ranktracker.service.domain.IDomainService;
import com.codingee.ranked.ranktracker.service.track_request.ITrackRequestService;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/domain")
@RequiredArgsConstructor
@Slf4j
public class DomainController {
    private final IDomainService domainService;
    private final ITrackRequestService trackRequestService;

    @GetMapping("/{id}")
    public BaseResponse<DomainDTO> getDomain(@PathVariable Long id) {
        return BaseResponse.success(this.domainService.getDomainDTOById(id));
    }

    @PostMapping("")
    public BaseResponse<Domain> addDomain(@RequestAttribute(name = "clientId") Long clientId, @Valid @RequestBody AddDomainDTO addDomainDTO) {
        return BaseResponse.created(this.domainService.addDomain(clientId, addDomainDTO));
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteDomain(@PathVariable Long id) {
        this.domainService.deleteDomain(id);
        return BaseResponse.success(true);
    }

    @PostMapping("/{domainId}/track")
    public BaseResponse<DomainDTO> trackDomain(@RequestAttribute(name = "clientId") Long clientId, @PathVariable Long domainId) {
        return BaseResponse.success(this.domainService.trackDomain(clientId, domainId));
    }

    @GetMapping("")
    public BaseResponse<List<DomainDTO>> getDomains(@RequestAttribute(name = "clientId") Long clientId) {
        return BaseResponse.success(this.domainService.getDomainDTOsByClient(clientId));
    }

    @GetMapping("/{domainId}/ranking/average")
    public BaseResponse<List<TrackRequest>> getTrackRequests(@RequestAttribute(name = "clientId") Long clientId, @PathVariable Long domainId,  @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return BaseResponse.success(this.trackRequestService.getTrackRequestsByDomainId(domainId, PageRequest.of(page, size), null));
    }
}
