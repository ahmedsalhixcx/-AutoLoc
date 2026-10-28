# Atelier 5 : Spring MVC et SpringDoc : exposer les services en API REST, tester avec Postman puis documenter avec Swagger

## Section A. Endpoints

L'ensemble des routes est préfixé par `/api` défini globalement via `server.servlet.context-path=/api` dans `application.properties`. Les contrôleurs portent uniquement les ressources au pluriel.

| Contrôleur | Verbe & Route | Service appelé | Code succès | Codes d'erreur |
|---|---|---|---|---|
| `ContratController` | `GET /api/contrats` | `contratService.findAll()` | `200 OK` | — |
| `ContratController` | `GET /api/contrats/{id}` | `contratService.findById(id)` | `200 OK` | `404 Not Found` |
| `ContratController` | `POST /api/contrats` | `contratService.create(contrat)` | `201 Created` | `400 Bad Request` |
| `ContratController` | `PUT /api/contrats/{id}` | `contratService.update(id, contrat)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `ContratController` | `DELETE /api/contrats/{id}` | `contratService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `PaiementController` | `GET /api/paiements` | `paiementService.findAll()` | `200 OK` | — |
| `PaiementController` | `GET /api/paiements/{id}` | `paiementService.findById(id)` | `200 OK` | `404 Not Found` |
| `PaiementController` | `POST /api/paiements` | *(Non implémenté - Lecture seule)* | — | `405 Method Not Allowed` |
| `AgenceController` | `GET /api/agences` | `agenceService.findAll()` | `200 OK` | — |
| `AgenceController` | `GET /api/agences/{id}` | `agenceService.findById(id)` | `200 OK` | `404 Not Found` |
| `AgenceController` | `POST /api/agences` | `agenceService.create(agence)` | `201 Created` | `400 Bad Request` |
| `AgenceController` | `PUT /api/agences/{id}` | `agenceService.update(id, agence)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `AgenceController` | `DELETE /api/agences/{id}` | `agenceService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `EmployeController` | `GET /api/employes` | `employeService.findAll()` | `200 OK` | — |
| `EmployeController` | `GET /api/employes/{id}` | `employeService.findById(id)` | `200 OK` | `404 Not Found` |
| `EmployeController` | `POST /api/employes` | `employeService.create(employe)` | `201 Created` | `400 Bad Request` |
| `EmployeController` | `PUT /api/employes/{id}` | `employeService.update(id, employe)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `EmployeController` | `DELETE /api/employes/{id}` | `employeService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `VehiculeController` | `GET /api/vehicules` | `vehiculeService.findAll()` | `200 OK` | — |
| `VehiculeController` | `GET /api/vehicules/{id}` | `vehiculeService.findById(id)` | `200 OK` | `404 Not Found` |
| `VehiculeController` | `POST /api/vehicules` | `vehiculeService.create(vehicule)` | `201 Created` | `400 Bad Request` |
| `VehiculeController` | `PUT /api/vehicules/{id}` | `vehiculeService.update(id, vehicule)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `VehiculeController` | `DELETE /api/vehicules/{id}` | `vehiculeService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `EquipementController` | `GET /api/equipements` | `equipementService.findAll()` | `200 OK` | — |
| `EquipementController` | `GET /api/equipements/{id}` | `equipementService.findById(id)` | `200 OK` | `404 Not Found` |
| `EquipementController` | `POST /api/equipements` | `equipementService.create(equipement)` | `201 Created` | `400 Bad Request` |
| `EquipementController` | `PUT /api/equipements/{id}` | `equipementService.update(id, equipement)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `EquipementController` | `DELETE /api/equipements/{id}` | `equipementService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `ClientController` | `GET /api/clients` | `clientService.findAll()` | `200 OK` | — |
| `ClientController` | `GET /api/clients/{id}` | `clientService.findById(id)` | `200 OK` | `404 Not Found` |
| `ClientController` | `POST /api/clients` | `clientService.create(client)` | `201 Created` | `400 Bad Request` |
| `ClientController` | `PUT /api/clients/{id}` | `clientService.update(id, client)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `ClientController` | `DELETE /api/clients/{id}` | `clientService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `ReservationController` | `GET /api/reservations` | `reservationService.findAll()` | `200 OK` | — |
| `ReservationController` | `GET /api/reservations/{id}` | `reservationService.findById(id)` | `200 OK` | `404 Not Found` |
| `ReservationController` | `POST /api/reservations` | `reservationService.create(reservation)` | `201 Created` | `400 Bad Request` |
| `ReservationController` | `PUT /api/reservations/{id}` | `reservationService.update(id, reservation)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `ReservationController` | `DELETE /api/reservations/{id}` | `reservationService.deleteById(id)` | `204 No Content` | `404 Not Found` |
| `MaintenanceController` | `GET /api/maintenances` | `maintenanceService.findAll()` | `200 OK` | — |
| `MaintenanceController` | `GET /api/maintenances/{id}` | `maintenanceService.findById(id)` | `200 OK` | `404 Not Found` |
| `MaintenanceController` | `POST /api/maintenances` | `maintenanceService.create(maintenance)` | `201 Created` | `400 Bad Request` |
| `MaintenanceController` | `PUT /api/maintenances/{id}` | `maintenanceService.update(id, maintenance)` | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `MaintenanceController` | `DELETE /api/maintenances/{id}` | `maintenanceService.deleteById(id)` | `204 No Content` | `404 Not Found` |

---

## Section B. Annotations Jackson

### Gestion des associations bidirectionnelles

| Association | Annotation retenue | Justification |
|---|---|---|
| `Contrat` ↔ `Paiement` | `@JsonManagedReference("contrat-paiements")` sur `Contrat.paiements` et `@JsonBackReference("contrat-paiements")` sur `Paiement.contrat` | Relation de **composition forte** : les paiements sont créés directement imbriqués dans le JSON du contrat (cascade = ALL). La paire gère la récursion tout en sérialisant la liste et en restaurant le lien parent à la désérialisation. |
| `Agence` ↔ `Employe` | `@JsonIgnore` sur `Agence.employes` | Évite la boucle infinie de sérialisation. Les employés sont créés individuellement en référençant l'agence par son identifiant (`{ "agence": {"idAgence": 1} }`). |
| `Agence` ↔ `Vehicule` | `@JsonIgnore` sur `Agence.vehicules` | Évite la boucle infinie. Les véhicules désignent leur agence de rattachement. |
| `Client` ↔ `Reservation` | `@JsonIgnore` sur `Client.reservations` | Évite la boucle infinie. Attention : on ne place pas `@JsonBackReference` sur `Reservation.client`, sinon la propriété serait ignorée lors du `POST` d'une réservation ! |
| `Vehicule` ↔ `Maintenance` | `@JsonIgnore` sur `Vehicule.maintenances` | Évite la boucle lors de la consultation des véhicules. |
| `Vehicule` ↔ `Reservation` | `@JsonIgnore` sur `Vehicule.reservations` | Évite la boucle lors de la consultation des véhicules. |
| `Equipement` ↔ `Vehicule` | `@JsonIgnore` sur `Equipement.vehicules` | Évite la boucle sur l'association ManyToMany inverse. |
| `Reservation` ↔ `Contrat` | `@JsonIgnore` sur `Reservation.contrat` | Évite la boucle OneToOne bidirectionnelle. |

### Différence entre `@JsonBackReference` et `@JsonIgnore`
- **`@JsonBackReference`** : Fonctionne obligatoirement en couple avec `@JsonManagedReference`. Elle omet le champ lors de la **sérialisation** (Java vers JSON) pour éviter la boucle, mais le restaure automatiquement lors de la **désérialisation** (JSON vers Java) en rattachant l'enfant à son parent englobant.
- **`@JsonIgnore`** : Ignore totalement le champ dans **les deux sens** (sérialisation et désérialisation). Si un client envoie cette propriété dans le JSON, Jackson ne la lira pas et laissera le champ à `null`.

---

## Section C. Postman et Swagger

| Critère | Postman | Swagger UI (SpringDoc OpenAPI) |
|---|---|---|
| **Rôle principal** | **Client de test et d'automatisation** : envoyer des requêtes réelles, rejouer des scénarios, scripter des assertions et valider les codes de retour HTTP. | **Documentation interactive vivante** : contrat d'API généré automatiquement à partir du code, décrivant les modèles de données (schémas), paramètres et endpoints. |
| **Exemple concret rencontré** | Validation de la création d'un contrat avec ses paiements (`POST /api/contrats` avec statut `201`), mémorisation de l'id généré dans `{{contratId}}`, puis test de suppression en cascade (`DELETE` renvoyant `204`, suivi d'un `GET /api/paiements/1` constatant l'erreur `404`). | Visualisation immédiate de tous les contrôleurs sous `/api/swagger-ui.html`, inspection des schémas de `Contrat`, et test direct via le bouton *"Try it out"* sans quitter le navigateur. |
| **Complémentarité** | Postman permet de valider le comportement dynamique et les régressions ; Swagger sert de contrat de communication officiel entre développeurs backend et frontend. |

---

## Section D. Anomalies SonarQube for IDE

| Règle SonarQube | Description & explication | Correction apportée |
|---|---|---|
| **`java:S6813`** | *"Field dependency injection should not be used"* : L'injection par attribut (`@Autowired`) dans les contrôleurs masque les dépendances et empêche l'immuabilité (champs non `final`). | Utilisation de l'injection par constructeur avec des champs `final` typés par les interfaces de service (`I...Service`) et `@RequiredArgsConstructor` de Lombok. |
| **`java:S1186` / Routes REST non conformes** | Méthodes vides ou nommage d'actions dans les URL (ex: `/api/getContrats` ou `/api/deleteVehicule`). Dans l'architecture REST, ce sont les verbes HTTP (`GET`, `DELETE`) qui portent l'action, et les URI doivent désigner des collections de ressources au pluriel. | Adoption stricte des conventions REST : `/api/contrats`, `/api/vehicules`, utilisation exclusive des verbes adaptés (`GET`, `POST`, `PUT`, `DELETE`). |

---

## Section E. Questions de compréhension

1. **Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?**
   C'est le **`DispatcherServlet`** (le *Front Controller* central de Spring MVC). Dès qu'une requête HTTP arrive, il interroge le **`HandlerMapping`** (notamment `RequestMappingHandlerMapping`), qui maintient en mémoire la table de correspondance construite au démarrage entre les URLs, les verbes HTTP (`@GetMapping`, `@PostMapping`...) et les méthodes des classes annotées `@RestController`. Une fois la méthode identifiée, il délègue l'exécution via un `HandlerAdapter`.

2. **Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?**
   - **Non au Repository** : Le contrôleur web ne doit jamais accéder directement aux données sans passer par le service, car c'est le service qui applique les règles de gestion, les vérifications d'intégrité et la logique métier.
   - **Non à `ContratServiceImpl`** : Pour respecter le principe d'Inversion des Dépendances (DIP des principes SOLID). Dépendre de l'abstraction (`IContratService`) assure un couplage lâche, facilitant les tests unitaires (mocking) et permettant de substituer l'implémentation sans toucher au contrôleur.

3. **Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?**
   - **`POST → 201 Created`** : Indique explicitement qu'une nouvelle ressource a été créée avec succès sur le serveur et lui attribue son identifiant/URI.
   - **`DELETE → 204 No Content`** : Indique que l'opération de suppression a été accomplie avec succès et qu'il n'y a plus aucun contenu à renvoyer dans le corps de la réponse.
   - **`404 vs 500`** : Une ressource absente est une situation normale de consultation client (*Not Found* = `404`), ce n'est pas un dysfonctionnement du serveur. Le code `500 Internal Server Error` est réservé aux erreurs inattendues ou aux bugs non gérés côté serveur.

4. **Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?**
   - **Uniquement des GET** : Le cycle de vie d'un paiement est indissociable de son contrat de location (relation de composition). Les ajouts ou suppressions de paiements modifient le solde et la validité du contrat ; ils doivent donc impérativement passer par le service `Contrat`.
   - **Réponse reçue sur POST `/api/paiements`** : Spring MVC renvoie un code HTTP **`405 Method Not Allowed`**, car aucune méthode `@PostMapping` n'est déclarée sur ce chemin.

5. **Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?**
   - **Risques de l'exposition directe des entités JPA** :
     1. *Couplage fort* entre le schéma de la base de données et l'API publique (toute modification de colonne casse les clients de l'API).
     2. *Fuite de données sensibles* ou techniques (champs internes, mots de passe, clés étrangères exposés sans filtre).
     3. *Risque de boucle infinie de sérialisation* (`StackOverflowError`) et déclenchement inopiné de requêtes SQL supplémentaires liées au Lazy Loading. (Ces problèmes seront résolus au Chapitre 10 avec les DTO).
   - **`@JsonManagedReference` vs `@JsonIgnore`** :
     - `Contrat.paiements` forme une **composition** : on veut afficher les paiements lorsqu'on consulte un contrat et permettre de créer un contrat avec ses paiements imbriqués en une seule requête.
     - Pour `Agence.vehicules`, les véhicules existent en tant qu'entités indépendantes créées séparément. Sérialiser la liste des véhicules dans chaque agence (et inversement) alourdirait inutilement la réponse et créerait une boucle. On pose donc `@JsonIgnore` sur la liste côté inverse.
