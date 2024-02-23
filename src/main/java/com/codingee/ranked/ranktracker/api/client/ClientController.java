package com.codingee.ranked.ranktracker.api.client;


import com.codingee.ranked.ranktracker.dto.client.AddClientDeviceRequestDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.dto.client.AddClientRequestDTO;
import com.codingee.ranked.ranktracker.dto.client.UpdateClientRequestDTO;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final IClientService clientService;

    @GetMapping("")
    public BaseResponse<Client> getClient(@RequestAttribute(name = "clientId") Long clientId) {
        Client client = this.clientService.getClient(clientId);
        return BaseResponse.success(client);
    }

    @PostMapping("/create")
    public BaseResponse<Client> createClient(@Valid @RequestBody AddClientRequestDTO addClientRequestDTO) {
        return BaseResponse.created(this.clientService.addClient(addClientRequestDTO));
    }

    @PatchMapping("")
    public BaseResponse<Client> updateClient(@RequestAttribute(name = "clientId") Long clientId, @Valid @RequestBody UpdateClientRequestDTO updateClientRequestDTO) {
        return BaseResponse.success(this.clientService.updateClient(clientId, updateClientRequestDTO));
    }

    @PostMapping("/device")
    public BaseResponse<Boolean> addDevice(@RequestAttribute(name = "clientId") Long clientId, @Valid @RequestBody AddClientDeviceRequestDTO clientDeviceRequestDTO) {
        return BaseResponse.success(this.clientService.addClientDeviceIds(clientId, clientDeviceRequestDTO));
    }
}
