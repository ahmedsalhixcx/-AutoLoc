package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementDTO;

import java.util.List;

@Component
public class PaiementMapper {

    public PaiementDTO toDTO(Paiement paiement) {
        if (paiement == null) {
            return null;
        }
        return PaiementDTO.builder()
                .idPaiement(paiement.getIdPaiement())
                .montant(paiement.getMontant())
                .datePaiement(paiement.getDatePaiement())
                .modePaiement(paiement.getModePaiement())
                .idContrat(paiement.getContrat() != null ? paiement.getContrat().getIdContrat() : null)
                .build();
    }

    public Paiement toEntity(PaiementDTO dto) {
        if (dto == null) {
            return null;
        }
        Paiement paiement = new Paiement();
        paiement.setIdPaiement(dto.getIdPaiement());
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setModePaiement(dto.getModePaiement());
        if (dto.getIdContrat() != null) {
            Contrat contrat = new Contrat();
            contrat.setIdContrat(dto.getIdContrat());
            paiement.setContrat(contrat);
        }
        return paiement;
    }

    public List<PaiementDTO> toDTOList(List<Paiement> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(this::toDTO).toList();
    }
}
