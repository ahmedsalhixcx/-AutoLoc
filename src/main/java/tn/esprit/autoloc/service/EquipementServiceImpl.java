package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement addEquipement(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement updateEquipement(Equipement equipement) {
        Long id = equipement.getIdEquipement();
        if (id == null || !equipementRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipement introuvable : " + id);
        }
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement retrieveEquipement(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> retrieveAllEquipements() {
        return equipementRepository.findAll();
    }

    @Override
    public void removeEquipement(Long id) {
        equipementRepository.deleteById(id);
    }
}
