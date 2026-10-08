package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Agence;

import java.util.List;

public interface IAgenceService {

    Agence addAgence(Agence agence);

    Agence updateAgence(Agence agence);

    Agence retrieveAgence(Long id);

    List<Agence> retrieveAllAgences();

    void removeAgence(Long id);
}
