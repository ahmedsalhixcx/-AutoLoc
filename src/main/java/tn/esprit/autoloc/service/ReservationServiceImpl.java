package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IClientRepository clientRepository;
    private final ReservationMapper reservationMapper;

    private void validateReservation(ReservationDTO dto) {
        if (dto.getDateDebut() != null && dto.getDateFin() != null
                && dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    @Override
    public ReservationDTO create(ReservationDTO reservationDTO) {
        if (reservationDTO.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        validateReservation(reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);

        if (reservationDTO.getIdVehicule() != null) {
            Vehicule vehicule = vehiculeRepository.findById(reservationDTO.getIdVehicule())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", reservationDTO.getIdVehicule()));
            reservation.setVehicule(vehicule);
        }

        if (reservationDTO.getIdClient() != null) {
            Client client = clientRepository.findById(reservationDTO.getIdClient())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", reservationDTO.getIdClient()));
            reservation.setClient(client);
        }

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDTO(saved);
    }

    @Override
    public ReservationDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public List<ReservationDTO> findAll() {
        return reservationMapper.toDTOList(reservationRepository.findAll());
    }

    @Override
    public ReservationDTO update(Long id, ReservationDTO reservationDTO) {
        validateReservation(reservationDTO);
        Reservation existant = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));

        existant.setDateDebut(reservationDTO.getDateDebut());
        existant.setDateFin(reservationDTO.getDateFin());
        existant.setStatut(reservationDTO.getStatut());

        if (reservationDTO.getIdVehicule() != null) {
            Vehicule vehicule = vehiculeRepository.findById(reservationDTO.getIdVehicule())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", reservationDTO.getIdVehicule()));
            existant.setVehicule(vehicule);
        }

        if (reservationDTO.getIdClient() != null) {
            Client client = clientRepository.findById(reservationDTO.getIdClient())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", reservationDTO.getIdClient()));
            existant.setClient(client);
        }

        Reservation saved = reservationRepository.save(existant);
        return reservationMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }
}
