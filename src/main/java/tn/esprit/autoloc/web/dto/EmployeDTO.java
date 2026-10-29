package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autoloc.domain.RoleEmploye;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeDTO {

    private Long idEmploye;

    @NotBlank(message = "Le nom de l'employé est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom de l'employé est obligatoire")
    private String prenom;

    @NotNull(message = "Le rôle de l'employé est obligatoire")
    private RoleEmploye role;

    @NotNull(message = "L'identifiant de l'agence est obligatoire")
    private Long idAgence;

    private String nomAgence;
}
