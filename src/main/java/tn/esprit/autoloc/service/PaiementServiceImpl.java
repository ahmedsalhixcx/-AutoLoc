package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement addPaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement updatePaiement(Paiement paiement) {
        Long id = paiement.getIdPaiement();
        if (id == null || !paiementRepository.existsById(id)) {
            throw new EntityNotFoundException("Paiement introuvable : " + id);
        }
        return paiementRepository.save(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement retrievePaiement(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> retrieveAllPaiements() {
        return paiementRepository.findAll();
    }

    @Override
    public void removePaiement(Long id) {
        paiementRepository.deleteById(id);
    }
}
