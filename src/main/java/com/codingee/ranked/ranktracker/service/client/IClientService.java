package com.codingee.ranked.ranktracker.service.client;


import com.codingee.ranked.ranktracker.dto.client.AddClientDeviceRequestDTO;
import com.codingee.ranked.ranktracker.dto.client.AddClientRequestDTO;
import com.codingee.ranked.ranktracker.dto.client.UpdateClientRequestDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;

public interface IClientService {
    Client addClient(AddClientRequestDTO client);
    Client updateClient(Long id, UpdateClientRequestDTO client);
    Client deleteClient(Long id);
    Client getClient(Long id);

    Client getClientByEmail(String email) throws ResourceNotFoundException;

    Boolean addClientDeviceIds(Long id, AddClientDeviceRequestDTO clientDeviceRequestDTO);
}
