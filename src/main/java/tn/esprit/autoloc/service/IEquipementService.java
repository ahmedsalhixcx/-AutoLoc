package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

public interface IEquipementService {

    Equipement addEquipement(Equipement equipement);

    Equipement updateEquipement(Equipement equipement);

    Equipement retrieveEquipement(Long id);

    List<Equipement> retrieveAllEquipements();

    void removeEquipement(Long id);
}
