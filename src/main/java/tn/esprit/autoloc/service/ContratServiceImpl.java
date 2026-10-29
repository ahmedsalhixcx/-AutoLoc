package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.PaiementDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;
    private final PaiementMapper paiementMapper;

    private void validateContrat(ContratDTO dto) {
        if (dto.getMontantTotal() != null && dto.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
    }

    @Override
    public ContratDTO create(ContratDTO contratDTO) {
        if (contratDTO.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        validateContrat(contratDTO);
        Contrat contrat = contratMapper.toEntity(contratDTO);

        if (contratDTO.getIdReservation() != null) {
            Reservation reservation = reservationRepository.findById(contratDTO.getIdReservation())
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation", contratDTO.getIdReservation()));
            contrat.setReservation(reservation);
        }

        if (contratDTO.getPaiements() != null && !contratDTO.getPaiements().isEmpty()) {
            List<Paiement> paiements = new ArrayList<>();
            for (PaiementDTO pDto : contratDTO.getPaiements()) {
                Paiement p = paiementMapper.toEntity(pDto);
                p.setContrat(contrat);
                paiements.add(p);
            }
            contrat.setPaiements(paiements);
        }

        Contrat saved = contratRepository.save(contrat);
        return contratMapper.toDTO(saved);
    }

    @Override
    public ContratDTO findById(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        return contratMapper.toDTO(contrat);
    }

    @Override
    public List<ContratDTO> findAll() {
        return contratMapper.toDTOList(contratRepository.findAll());
    }

    @Override
    public ContratDTO update(Long id, ContratDTO contratDTO) {
        validateContrat(contratDTO);
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));

        existant.setDateSignature(contratDTO.getDateSignature());
        existant.setMontantTotal(contratDTO.getMontantTotal());
        existant.setValide(contratDTO.isValide());

        if (contratDTO.getIdReservation() != null) {
            Reservation reservation = reservationRepository.findById(contratDTO.getIdReservation())
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation", contratDTO.getIdReservation()));
            existant.setReservation(reservation);
        }

        Contrat saved = contratRepository.save(existant);
        return contratMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }
}
