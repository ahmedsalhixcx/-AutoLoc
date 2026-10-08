package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Paiement;

import java.util.List;

public interface IPaiementService {

    Paiement addPaiement(Paiement paiement);

    Paiement updatePaiement(Paiement paiement);

    Paiement retrievePaiement(Long id);

    List<Paiement> retrieveAllPaiements();

    void removePaiement(Long id);
}
