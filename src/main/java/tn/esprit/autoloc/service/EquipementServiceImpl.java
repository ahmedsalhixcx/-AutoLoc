package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.web.dto.EquipementDTO;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    private void validateEquipement(EquipementDTO dto) {
        if (dto.getLibelle() == null || dto.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé de l'équipement ne peut pas être vide");
        }
    }

    @Override
    public EquipementDTO create(EquipementDTO equipementDTO) {
        if (equipementDTO.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        validateEquipement(equipementDTO);
        Equipement equipement = equipementMapper.toEntity(equipementDTO);
        Equipement saved = equipementRepository.save(equipement);
        return equipementMapper.toDTO(saved);
    }

    @Override
    public EquipementDTO findById(Long id) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
        return equipementMapper.toDTO(equipement);
    }

    @Override
    public List<EquipementDTO> findAll() {
        return equipementMapper.toDTOList(equipementRepository.findAll());
    }

    @Override
    public EquipementDTO update(Long id, EquipementDTO equipementDTO) {
        validateEquipement(equipementDTO);
        Equipement existant = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
        existant.setLibelle(equipementDTO.getLibelle());
        Equipement saved = equipementRepository.save(existant);
        return equipementMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }
}
