package com.netec.app.service;
import java.util.List;
import java.util.Optional;

import com.netec.app.entities.Client;

public interface IClientService {
    List<Client> findAll();
    Optional<Client> findById(Long id);
    Client save(Client client);
    void deleteById(Long id);
}
