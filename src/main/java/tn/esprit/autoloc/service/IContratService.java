package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;

import java.util.List;

public interface IContratService {

    Contrat addContrat(Contrat contrat);

    Contrat updateContrat(Contrat contrat);

    Contrat retrieveContrat(Long id);

    List<Contrat> retrieveAllContrats();

    void removeContrat(Long id);
}
