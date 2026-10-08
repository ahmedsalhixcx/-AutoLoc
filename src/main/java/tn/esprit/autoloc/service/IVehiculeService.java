package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    Vehicule addVehicule(Vehicule vehicule);

    Vehicule updateVehicule(Vehicule vehicule);

    Vehicule retrieveVehicule(Long id);

    List<Vehicule> retrieveAllVehicules();

    void removeVehicule(Long id);
}
