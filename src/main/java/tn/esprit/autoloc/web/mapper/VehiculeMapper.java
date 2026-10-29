package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class VehiculeMapper {

    public VehiculeDTO toDTO(Vehicule vehicule) {
        if (vehicule == null) {
            return null;
        }
        List<Long> equipementIds = null;
        if (vehicule.getEquipements() != null) {
            equipementIds = vehicule.getEquipements().stream()
                    .map(Equipement::getIdEquipement)
                    .toList();
        }

        return VehiculeDTO.builder()
                .idVehicule(vehicule.getIdVehicule())
                .immatriculation(vehicule.getImmatriculation())
                .marque(vehicule.getMarque())
                .modele(vehicule.getModele())
                .categorie(vehicule.getCategorie())
                .tarifJournalier(vehicule.getTarifJournalier())
                .statut(vehicule.getStatut())
                .idAgence(vehicule.getAgence() != null ? vehicule.getAgence().getIdAgence() : null)
                .equipementIds(equipementIds)
                .build();
    }

    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) {
            return null;
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(dto.getIdVehicule());
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut());

        if (dto.getIdAgence() != null) {
            Agence agence = new Agence();
            agence.setIdAgence(dto.getIdAgence());
            vehicule.setAgence(agence);
        }

        if (dto.getEquipementIds() != null) {
            Set<Equipement> equipements = new HashSet<>();
            for (Long eqId : dto.getEquipementIds()) {
                Equipement eq = new Equipement();
                eq.setIdEquipement(eqId);
                equipements.add(eq);
            }
            vehicule.setEquipements(equipements);
        }

        return vehicule;
    }

    public List<VehiculeDTO> toDTOList(List<Vehicule> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toDTO).toList();
    }
}
