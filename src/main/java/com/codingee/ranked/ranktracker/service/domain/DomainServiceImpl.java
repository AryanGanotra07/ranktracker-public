package com.codingee.ranked.ranktracker.service.domain;

import com.codingee.ranked.ranktracker.dto.domain.AddDomainDTO;
import com.codingee.ranked.ranktracker.dto.domain.DomainDTO;
import com.codingee.ranked.ranktracker.dto.task.TrackRequestQueueTask;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.codingee.ranked.ranktracker.repo.domain.IDomainRepo;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.service.task.ITaskSchedularService;
import com.codingee.ranked.ranktracker.service.track_request.ITrackRequestService;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import com.codingee.ranked.ranktracker.util.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DomainServiceImpl implements IDomainService {

    private static final String DOMAIN_NOT_FOUND_MSG = "Domain with name %s not found!";
    private static final String DOMAIN_WITH_ID_NOT_FOUND_MSG = "Domain with id %s not found!";

    private final IDomainRepo domainRepo;
    private final IClientService clientService;
    private final ITaskSchedularService taskSchedularService;
    private final ITrackRequestService trackRequestService;

    @Override
    public Domain addDomain(Long clientId, AddDomainDTO addDomainDTO) {
        addDomainDTO.setName(extractDomain(addDomainDTO.getName()));
        Client client = this.clientService.getClient(clientId);
        Domain domain = new Domain(addDomainDTO.getName(), client, addDomainDTO.getDeviceType(), addDomainDTO.getLocation(), addDomainDTO.getLanguageCode());
        domainRepo.save(domain);
//        Set<String> keywordsName = Objects.nonNull(addDomainDTO.getKeywords()) ? new HashSet<>(addDomainDTO.getKeywords()): new HashSet<>();
//        String deviceType = addDomainDTO.getDeviceType();
//        String location = addDomainDTO.getLocation();
//        Set<Keyword> keywordsDTOs = keywordsName.stream().map((name) -> new Keyword(name, domain, deviceType, location)).collect(Collectors.toSet());
////        this.keywordRepo.saveAll(keywordsDTOs);
//        domain.setKeywords(keywordsDTOs);
//        if (!keywordsDTOs.isEmpty()) {
//            domain.setIsDraft(false);
//        }
        return domainRepo.save(domain);
    }


    private static String extractDomain(String inputDomain) {
        // Use regex to extract domain from the input
        String regex = "^(?:https?://)?(?:www\\.)?([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputDomain);

        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            throw new ValidationException("Invalid domain format");
        }
    }

//    private static boolean isValidDomain(String domain, Boolean https) {
//        // Use RestTemplate to send a GET request and check the status code
//        RestTemplate restTemplate = new RestTemplate();
//
//        try {
//            String url = (https ? "https://" : "http://") + domain; // You can also use "https://" if needed
//            HttpStatus statusCode = restTemplate.execute(URI.create(url), org.springframework.http.HttpMethod.GET,
//                    (RequestCallback) null, (ResponseExtractor<HttpStatus>) ClientHttpResponse::getStatusCode);
//
//            // Check if the status code is in the range 200 to 300
//            return Objects.requireNonNull(statusCode).is2xxSuccessful();
//        } catch (Exception e) {
//            // Handle exceptions, e.g., invalid domain or network issues
//            return false;
//        }
//    }

    @Override
    public Domain updateDomain(Long id, Domain domain) {
        return this.domainRepo.save(domain);
    }

    @Override
    public void deleteDomain(Long id) {
        Optional<Domain> optionalDomain = this.domainRepo.findByIdAndIsDeletedFalse(id);
        if (optionalDomain.isEmpty()) {
            throw new ResourceNotFoundException(String.format(DOMAIN_NOT_FOUND_MSG, id));
        }
        optionalDomain.get().setIsDeleted(true);
        return;
    }


    @Override
    public Domain getDomainById(Long id) {
        Optional<Domain> optionalDomain = this.domainRepo.findByIdAndIsDeletedFalse(id);
        if (optionalDomain.isEmpty() || optionalDomain.get().getIsDeleted()) {
            throw new ResourceNotFoundException(String.format(DOMAIN_NOT_FOUND_MSG, id));
        }
        return optionalDomain.get();
    }

    @Override
    public DomainDTO getDomainDTOById(Long id) {
        Optional<Domain> optionalDomain = this.domainRepo.findByIdAndIsDeletedFalse(id);
        if (optionalDomain.isEmpty() || optionalDomain.get().getIsDeleted()) {
            throw new ResourceNotFoundException(String.format(DOMAIN_NOT_FOUND_MSG, id));
        }
        Domain domain = optionalDomain.get();
        List<TrackRequest> trackRequests = trackRequestService.getTrackRequestsByDomainId(domain.getId(), PageRequest.of(0, 1), null);
        List<TrackRequest> completedTrackRequests = trackRequestService.getTrackRequestsByDomainId(domain.getId(),
                PageRequest.of(0, 1),
                new HashSet<>(Collections.singletonList(TrackRequest.TrackRequestStatus.COMPLETED)));
        TrackRequest trackRequest = trackRequests.isEmpty() ? null : trackRequests.get(0);
        TrackRequest completedTrackRequest = completedTrackRequests.isEmpty() ? null : completedTrackRequests.get(0);
        DomainDTO domainDTO = new DomainDTO(domain);
        domainDTO.setLatestTrackRequest(trackRequest);
        domainDTO.setLatestCompletedTrackRequest(completedTrackRequest);
        return domainDTO;
    }

    @Override
    public List<DomainDTO> getDomainDTOsByClient(Long clientId) {
        List<Domain> domains = this.getDomains(clientId);
        return domains.stream().map(d -> this.getDomainDTOById(d.getId())).collect(Collectors.toList());
    }


    @Override
    public DomainDTO trackDomain(Long clientId, Long domainId) {
        DomainDTO domainDTO = this.getDomainDTOById(domainId);
        if (domainDTO.getIsTracking()) {
            throw new ValidationException("We are already processing rankings for this domain");
        }

        try {
            log.info("Sending domain track request {}", domainId);
            Domain domain = this.getDomainById(domainId);
            TrackRequest trackRequest = this.trackRequestService.addTrackingRequest(new TrackRequest(domain));
            this.taskSchedularService.sendTrackRequest(new TrackRequestQueueTask(trackRequest.getId(), domainId, clientId));
            return this.getDomainDTOById(domainId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Domain saveDomain(Domain domain) {
        return this.domainRepo.save(domain);
    }

    @Override
    public List<Domain> getDomains(Long clientId) {
        Client client = this.clientService.getClient(clientId);
        return domainRepo.findByClientAndIsDeletedFalse(client).orElse(new ArrayList<>());
    }
}
