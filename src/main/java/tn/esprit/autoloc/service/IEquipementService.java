package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.EquipementDTO;

import java.util.List;

public interface IEquipementService {
    EquipementDTO create(EquipementDTO equipementDTO);
    EquipementDTO findById(Long id);
    List<EquipementDTO> findAll();
    EquipementDTO update(Long id, EquipementDTO equipementDTO);
    void deleteById(Long id);
}
