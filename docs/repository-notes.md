# Atelier 3 : notes sur la couche Repository

## 1. Choix de l'interface

Les neuf repositories d'AutoLoc étendent `JpaRepository<Entité, Long>` : toutes les clés primaires sont de type `Long` (`GenerationType.IDENTITY`).

| Interface | Étend | Justification |
|---|---|---|
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet, `findAll` renvoie une `List`, tri et pagination disponibles. |
| IEmployeRepository | JpaRepository<Employe, Long> | Même besoin : CRUD complet avec listes. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD complet, tri et pagination utiles pour un catalogue de véhicules. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet avec listes. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet, tri et pagination pour la liste des clients. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet, tri et pagination pour l'historique des réservations. |
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, `findAll` renvoie une `List`, `saveAndFlush` disponible. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Créé pour lire les paiements. Le code métier crée ou retire un paiement via le `Contrat` (composition, cascade et orphanRemoval). |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet avec listes. |

### Pourquoi JpaRepository plutôt que CrudRepository ?

- `findAll`, `findAllById` et `saveAll` renvoient une `List` (et non un `Iterable`).
- Le tri et la pagination sont inclus (`findAll(Sort)`, `findAll(Pageable)`).
- Il ajoute `flush`, `saveAndFlush` et `getReferenceById`.

### Attention aux suppressions en lot

`deleteAllInBatch` et `deleteAllByIdInBatch` contournent le contexte de persistance : la cascade et l'orphanRemoval ne s'appliquent pas. Supprimer ainsi des contrats laisserait des paiements orphelins, ou ferait échouer la requête sur la contrainte de clé étrangère.

## 2. Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Aucune anomalie détectée sur les packages `domain` et `repository` | Analyse complète avec SonarQube for IDE | Aucune correction nécessaire |