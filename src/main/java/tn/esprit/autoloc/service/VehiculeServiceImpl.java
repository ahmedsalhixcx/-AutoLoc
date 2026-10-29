package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final VehiculeMapper vehiculeMapper;

    private void validateVehicule(VehiculeDTO dto) {
        if (dto.getTarifJournalier() != null && dto.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
    }

    @Override
    public VehiculeDTO create(VehiculeDTO vehiculeDTO) {
        if (vehiculeDTO.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        validateVehicule(vehiculeDTO);
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);

        if (vehiculeDTO.getIdAgence() != null) {
            Agence agence = agenceRepository.findById(vehiculeDTO.getIdAgence())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence", vehiculeDTO.getIdAgence()));
            vehicule.setAgence(agence);
        }

        if (vehiculeDTO.getEquipementIds() != null && !vehiculeDTO.getEquipementIds().isEmpty()) {
            Set<Equipement> equipements = new HashSet<>(equipementRepository.findAllById(vehiculeDTO.getEquipementIds()));
            vehicule.setEquipements(equipements);
        }

        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDTO(saved);
    }

    @Override
    public VehiculeDTO findById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        return vehiculeMapper.toDTO(vehicule);
    }

    @Override
    public List<VehiculeDTO> findAll() {
        return vehiculeMapper.toDTOList(vehiculeRepository.findAll());
    }

    @Override
    public VehiculeDTO update(Long id, VehiculeDTO vehiculeDTO) {
        validateVehicule(vehiculeDTO);
        Vehicule existant = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));

        existant.setImmatriculation(vehiculeDTO.getImmatriculation());
        existant.setMarque(vehiculeDTO.getMarque());
        existant.setModele(vehiculeDTO.getModele());
        existant.setCategorie(vehiculeDTO.getCategorie());
        existant.setTarifJournalier(vehiculeDTO.getTarifJournalier());
        existant.setStatut(vehiculeDTO.getStatut());

        if (vehiculeDTO.getIdAgence() != null) {
            Agence agence = agenceRepository.findById(vehiculeDTO.getIdAgence())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence", vehiculeDTO.getIdAgence()));
            existant.setAgence(agence);
        }

        if (vehiculeDTO.getEquipementIds() != null) {
            Set<Equipement> equipements = new HashSet<>(equipementRepository.findAllById(vehiculeDTO.getEquipementIds()));
            existant.setEquipements(equipements);
        }

        Vehicule saved = vehiculeRepository.save(existant);
        return vehiculeMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }
}
