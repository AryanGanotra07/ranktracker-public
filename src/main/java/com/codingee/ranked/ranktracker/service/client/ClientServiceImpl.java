package com.codingee.ranked.ranktracker.service.client;

import com.codingee.ranked.ranktracker.dto.client.AddClientDeviceRequestDTO;
import com.codingee.ranked.ranktracker.dto.client.AddClientRequestDTO;
import com.codingee.ranked.ranktracker.dto.client.UpdateClientRequestDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.repo.client.IClientRepo;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import com.codingee.ranked.ranktracker.util.exceptions.UniqueConstraintException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService, UserDetailsService {


    private final IClientRepo clientRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final static String USER_WITH_ID_NOT_FOUND_MSG =
            "user with id %s not found";

    private final static String USER_EXISTS_ERROR =
            "user with email %s already exists";


    @Override
    public Client addClient(AddClientRequestDTO addClientRequestDTO) {
        if (this.clientRepo.findByEmail(addClientRequestDTO.getEmail()).isPresent()) {
            throw new UniqueConstraintException(String.format(USER_EXISTS_ERROR, addClientRequestDTO.getEmail()));
        }
        Client client = new Client(addClientRequestDTO.getFullName(), addClientRequestDTO.getEmail(), addClientRequestDTO.getCompany());
        if (Objects.nonNull(addClientRequestDTO.getPassword())) {
            client.setPassword(this.bCryptPasswordEncoder.encode(addClientRequestDTO.getPassword()));
        }
        return this.clientRepo.save(client);
    }

    @Override
    public Client updateClient(Long id, UpdateClientRequestDTO updateClientRequestDTO) {
        Optional<Client> optionalClient = this.clientRepo.findById(id);
        if (optionalClient.isEmpty()) {
            throw new ResourceNotFoundException(String.format(USER_WITH_ID_NOT_FOUND_MSG, id));
        }
        Client client = optionalClient.get();
        if (updateClientRequestDTO.getFullName() != null) {
            client.setFullName(updateClientRequestDTO.getFullName());
        }
        if (updateClientRequestDTO.getPassword() != null) {
            client.setPassword(this.bCryptPasswordEncoder.encode(updateClientRequestDTO.getPassword()));
        }
        if (updateClientRequestDTO.getCompany() != null) {
            client.setCompany(updateClientRequestDTO.getCompany());
        }
        return this.clientRepo.save(client);
    }

    @Override
    public Client deleteClient(Long id) {
        return null;
    }

    @Override
    public Client getClient(Long id) {
        return this.clientRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format(USER_NOT_FOUND_MSG, id)
        ));
    }

    @Override
    public Client getClientByEmail(String email) throws ResourceNotFoundException {
        return (Client) this.loadUserByUsername(email);
    }

    @Override
    public Boolean addClientDeviceIds(Long id, AddClientDeviceRequestDTO clientDeviceRequestDTO) {
        Client client = this.getClient(id);
        client.getDeviceIds().addAll(clientDeviceRequestDTO.getDeviceIds());
        this.clientRepo.save(client);
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {
        return this.clientRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                String.format(USER_NOT_FOUND_MSG, email)));
    }
}
