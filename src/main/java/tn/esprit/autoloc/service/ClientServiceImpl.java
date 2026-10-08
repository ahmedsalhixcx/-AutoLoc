package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Client client) {
        Long id = client.getIdClient();
        if (id == null || !clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client introuvable : " + id);
        }
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Client retrieveClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> retrieveAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void removeClient(Long id) {
        clientRepository.deleteById(id);
    }
}
