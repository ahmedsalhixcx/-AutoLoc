package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementDTO;

import java.util.List;

@Component
public class EquipementMapper {

    public EquipementDTO toDTO(Equipement equipement) {
        if (equipement == null) {
            return null;
        }
        return EquipementDTO.builder()
                .idEquipement(equipement.getIdEquipement())
                .libelle(equipement.getLibelle())
                .build();
    }

    public Equipement toEntity(EquipementDTO dto) {
        if (dto == null) {
            return null;
        }
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(dto.getIdEquipement());
        equipement.setLibelle(dto.getLibelle());
        return equipement;
    }

    public List<EquipementDTO> toDTOList(List<Equipement> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toDTO).toList();
    }
}
