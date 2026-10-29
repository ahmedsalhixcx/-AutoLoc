# Atelier 6 : DTO, Mappers, Validation des données et Gestion centralisée des erreurs

## 1. Utilisation des DTO (Data Transfer Objects) et Mappers

### 1.1 Pourquoi utiliser des DTO ?
L'exposition directe des entités JPA aux clients de l'API (comme dans les ateliers précédents) présente plusieurs risques majeurs :
- **Couplage fort** : toute modification du modèle de données (schéma relationnel en base) impacte directement l'API publique consommée par les clients.
- **Sécurité et fuite de données** : risque d'exposer des attributs internes, techniques ou sensibles.
- **Problèmes de sérialisation** : cycles de dépendances bidirectionnels (`StackOverflowError`) et déclenchement inopiné de requêtes SQL supplémentaires par le Lazy Loading Hibernate.
- **Optimisation des échanges** : les DTOs permettent de n'échanger que les données strictement nécessaires (ex. identifiants plutôt que graphes d'objets complets).

### 1.2 Organisation des DTOs (`tn.esprit.autoloc.web.dto`)
Chaque entité du domaine métier dispose d'un DTO correspondant portant les annotations de validation :

| Entité | DTO | Attributs principaux & Liens |
|---|---|---|
| `Agence` | `AgenceDTO` | `idAgence`, `nom`, `ville`, `adresse`, `telephone` |
| `Employe` | `EmployeDTO` | `idEmploye`, `nom`, `prenom`, `role`, `idAgence`, `nomAgence` |
| `Vehicule` | `VehiculeDTO` | `idVehicule`, `immatriculation`, `marque`, `modele`, `categorie`, `tarifJournalier`, `statut`, `idAgence`, `equipementIds` |
| `Equipement` | `EquipementDTO` | `idEquipement`, `libelle` |
| `Client` | `ClientDTO` | `idClient`, `nom`, `prenom`, `email`, `telephone`, `numPermis`, `dateInscription` |
| `Reservation` | `ReservationDTO` | `idReservation`, `dateDebut`, `dateFin`, `statut`, `idVehicule`, `idClient` |
| `Contrat` | `ContratDTO` | `idContrat`, `dateSignature`, `montantTotal`, `valide`, `idReservation`, `paiements` (`List<PaiementDTO>`) |
| `Paiement` | `PaiementDTO` | `idPaiement`, `montant`, `datePaiement`, `modePaiement`, `idContrat` |
| `Maintenance` | `MaintenanceDTO` | `idMaintenance`, `dateDebut`, `dateFin`, `description`, `idVehicule` |

### 1.3 Mappers (`tn.esprit.autoloc.web.mapper`)
Les 9 composants mappers (annotés `@Component` et injectés dans les services) assurent la conversion bidirectionnelle :
- `toDTO(Entity entity)` : convertit l'entité JPA en DTO pour les réponses.
- `toEntity(DTO dto)` : convertit le DTO entrant en entité JPA pour la persistance.
- `toDTOList(List<Entity> list)` : convertit les collections de résultats.

---

## 2. Validation des données avec Bean Validation

### 2.1 Dépendance
La dépendance officielle Spring Boot Validation Starter est intégrée dans le `pom.xml` :
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2.2 Règles de validation appliquées
Les DTOs intègrent les contraintes de saisie déclaratives :
- `@NotBlank(message = "...")` : chaînes non nulles et contenant au moins un caractère non blanc (`nom`, `prenom`, `email`, `immatriculation`, etc.).
- `@NotNull(message = "...")` : dates, énumérations, et identifiants de relations obligatoires (`role`, `categorie`, `statut`, `idAgence`, `idVehicule`, etc.).
- `@Email(message = "Le format de l'adresse email est invalide")` : validation automatique du format RFC de l'adresse email client.
- `@PositiveOrZero(message = "...")` et `@Positive(message = "...")` : montants et tarifs positifs.

Dans chaque contrôleur REST, l'annotation **`@Valid`** est placée devant `@RequestBody` pour déclencher la validation automatique avant l'exécution du service.

---

## 3. Gestion centralisée des erreurs (`GlobalExceptionHandler`)

La classe [`GlobalExceptionHandler`](file:///c:/Users/LOQ/IdeaProjects/autoloc-api/src/main/java/tn/esprit/autoloc/web/controller/GlobalExceptionHandler.java) annotée `@RestControllerAdvice` intercepte et transforme toutes les exceptions en réponses JSON uniformes :

1. **`MethodArgumentNotValidException` (Statut HTTP 400 Bad Request)** :  
   Déclenchée automatiquement lorsqu'une contrainte Bean Validation échoue sur un DTO.  
   Format de retour :
   ```json
   {
     "statut": 400,
     "message": "Erreur de validation des données",
     "horodatage": "2026-10-29T11:45:00",
     "erreurs": {
       "email": "Le format de l'adresse email est invalide",
       "nom": "Le nom du client est obligatoire"
     }
   }
   ```
2. **`ResourceNotFoundException` (Statut HTTP 404 Not Found)** :  
   Levée lorsqu'un identifiant est introuvable.
3. **`IllegalArgumentException` (Statut HTTP 400 Bad Request)** :  
   Levée lors de règles métier spécifiques (ex: présence d'un ID lors d'un `POST`, date de fin antérieure à date de début).
4. **`Exception` (Statut HTTP 500 Internal Server Error)** :  
   Interception globale de sécurité pour masquer les erreurs techniques non gérées.
