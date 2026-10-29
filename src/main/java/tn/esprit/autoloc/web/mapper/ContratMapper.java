package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.PaiementDTO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContratMapper {

    private final PaiementMapper paiementMapper;

    public ContratDTO toDTO(Contrat contrat) {
        if (contrat == null) {
            return null;
        }
        List<PaiementDTO> paiements = new ArrayList<>();
        if (contrat.getPaiements() != null) {
            paiements = contrat.getPaiements().stream()
                    .map(paiementMapper::toDTO)
                    .toList();
        }

        return ContratDTO.builder()
                .idContrat(contrat.getIdContrat())
                .dateSignature(contrat.getDateSignature())
                .montantTotal(contrat.getMontantTotal())
                .valide(contrat.isValide())
                .idReservation(contrat.getReservation() != null ? contrat.getReservation().getIdReservation() : null)
                .paiements(paiements)
                .build();
    }

    public Contrat toEntity(ContratDTO dto) {
        if (dto == null) {
            return null;
        }
        Contrat contrat = new Contrat();
        contrat.setIdContrat(dto.getIdContrat());
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.isValide());

        if (dto.getIdReservation() != null) {
            Reservation reservation = new Reservation();
            reservation.setIdReservation(dto.getIdReservation());
            contrat.setReservation(reservation);
        }

        if (dto.getPaiements() != null && !dto.getPaiements().isEmpty()) {
            List<Paiement> paiements = new ArrayList<>();
            for (PaiementDTO pDto : dto.getPaiements()) {
                Paiement paiement = paiementMapper.toEntity(pDto);
                paiement.setContrat(contrat);
                paiements.add(paiement);
            }
            contrat.setPaiements(paiements);
        }

        return contrat;
    }

    public List<ContratDTO> toDTOList(List<Contrat> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toDTO).toList();
    }
}
