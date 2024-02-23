package com.codingee.ranked.ranktracker.service.domain;

import com.codingee.ranked.ranktracker.dto.domain.AddDomainDTO;
import com.codingee.ranked.ranktracker.dto.domain.DomainDTO;
import com.codingee.ranked.ranktracker.model.domain.Domain;

import java.util.List;

public interface IDomainService {
    Domain addDomain(Long clientId, AddDomainDTO domain);
    Domain updateDomain(Long id, Domain domain);
    void deleteDomain(Long id);
    Domain getDomainById(Long id);

    DomainDTO getDomainDTOById(Long id);

    DomainDTO trackDomain(Long clientId, Long domainId);

    Domain saveDomain(Domain domain);

    List<Domain> getDomains(Long clientId);

    List<DomainDTO> getDomainDTOsByClient(Long clientId);

}
