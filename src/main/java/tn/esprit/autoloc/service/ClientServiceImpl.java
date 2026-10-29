package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.web.dto.ClientDTO;
import tn.esprit.autoloc.web.mapper.ClientMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    private void validateClient(ClientDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client ne peut pas être vide");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse email du client ne peut pas être vide");
        }
    }

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        if (clientDTO.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        validateClient(clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        Client saved = clientRepository.save(client);
        return clientMapper.toDTO(saved);
    }

    @Override
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        return clientMapper.toDTO(client);
    }

    @Override
    public List<ClientDTO> findAll() {
        return clientMapper.toDTOList(clientRepository.findAll());
    }

    @Override
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        validateClient(clientDTO);
        Client existant = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));

        existant.setNom(clientDTO.getNom());
        existant.setPrenom(clientDTO.getPrenom());
        existant.setEmail(clientDTO.getEmail());
        existant.setTelephone(clientDTO.getTelephone());
        existant.setNumPermis(clientDTO.getNumPermis());
        existant.setDateInscription(clientDTO.getDateInscription());

        Client saved = clientRepository.save(existant);
        return clientMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }
}
