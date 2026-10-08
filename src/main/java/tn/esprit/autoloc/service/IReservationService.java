package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;

import java.util.List;

public interface IReservationService {

    Reservation addReservation(Reservation reservation);

    Reservation updateReservation(Reservation reservation);

    Reservation retrieveReservation(Long id);

    List<Reservation> retrieveAllReservations();

    void removeReservation(Long id);
}
