package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat addContrat(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat updateContrat(Contrat contrat) {
        Long id = contrat.getIdContrat();
        if (id == null || !contratRepository.existsById(id)) {
            throw new EntityNotFoundException("Contrat introuvable : " + id);
        }
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat retrieveContrat(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> retrieveAllContrats() {
        return contratRepository.findAll();
    }

    @Override
    public void removeContrat(Long id) {
        contratRepository.deleteById(id);
    }
}
