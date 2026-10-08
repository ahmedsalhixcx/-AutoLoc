package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence addAgence(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    public Agence updateAgence(Agence agence) {
        Long id = agence.getIdAgence();
        if (id == null || !agenceRepository.existsById(id)) {
            throw new EntityNotFoundException("Agence introuvable : " + id);
        }
        return agenceRepository.save(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public Agence retrieveAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> retrieveAllAgences() {
        return agenceRepository.findAll();
    }

    @Override
    public void removeAgence(Long id) {
        agenceRepository.deleteById(id);
    }
}
