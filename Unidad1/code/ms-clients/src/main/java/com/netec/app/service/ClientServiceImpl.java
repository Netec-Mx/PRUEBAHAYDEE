package com.netec.app.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netec.app.entities.Client;
import com.netec.app.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements IClientService {

 
    private final ClientRepository clientRepository;
	
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
	@Override
	public List<Client> findAll() {
		return (List<Client>)clientRepository.findAll();
	}

	@Override
	public Optional<Client> findById(Long id) {
		return clientRepository.findById(id);
	}

	@Override
	public Client save(Client client) {
		 return clientRepository.save(client);
	}

	@Override
	public void deleteById(Long id) {
		clientRepository.deleteById(id);
	}
   
}
