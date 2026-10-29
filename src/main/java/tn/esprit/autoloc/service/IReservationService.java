package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.ReservationDTO;

import java.util.List;

public interface IReservationService {
    ReservationDTO create(ReservationDTO reservationDTO);
    ReservationDTO findById(Long id);
    List<ReservationDTO> findAll();
    ReservationDTO update(Long id, ReservationDTO reservationDTO);
    void deleteById(Long id);
}
