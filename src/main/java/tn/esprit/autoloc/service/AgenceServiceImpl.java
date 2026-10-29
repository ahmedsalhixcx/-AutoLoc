package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    private void validateAgence(AgenceDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'agence ne peut pas être vide");
        }
        if (dto.getVille() == null || dto.getVille().trim().isEmpty()) {
            throw new IllegalArgumentException("La ville de l'agence ne peut pas être vide");
        }
        if (dto.getAdresse() == null || dto.getAdresse().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse de l'agence ne peut pas être vide");
        }
    }

    @Override
    public AgenceDTO create(AgenceDTO agenceDTO) {
        if (agenceDTO.getIdAgence() != null) {
            throw new IllegalArgumentException("Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        validateAgence(agenceDTO);
        Agence agence = agenceMapper.toEntity(agenceDTO);
        Agence saved = agenceRepository.save(agence);
        return agenceMapper.toDTO(saved);
    }

    @Override
    public AgenceDTO findById(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        return agenceMapper.toDTO(agence);
    }

    @Override
    public List<AgenceDTO> findAll() {
        return agenceMapper.toDTOList(agenceRepository.findAll());
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO agenceDTO) {
        validateAgence(agenceDTO);
        Agence existant = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        existant.setNom(agenceDTO.getNom());
        existant.setVille(agenceDTO.getVille());
        existant.setAdresse(agenceDTO.getAdresse());
        existant.setTelephone(agenceDTO.getTelephone());
        Agence saved = agenceRepository.save(existant);
        return agenceMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }
}
