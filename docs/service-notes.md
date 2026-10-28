# Atelier 4 : Inversion de contrôle, injection de dépendances et couche Service

## Section A. Modes d'injection comparés

| Critère | Injection par constructeur (recommandée) | Injection par attribut (`@Autowired`) |
|---|---|---|
| **Champ `final` possible ?** | **Oui** : les dépendances sont assignées dès l'instanciation, garantissant l'immuabilité du bean. | **Non** : le champ ne peut pas être `final`, car Spring injecte la dépendance par réflexion après l'appel du constructeur. |
| **Dépendance visible dans la signature ?** | **Oui** : les dépendances requises apparaissent explicitement en paramètres du constructeur. | **Non** : les dépendances sont masquées à l'intérieur de la classe (effet boîte noire). |
| **Utilisable avec `new` hors conteneur sans dépendance `null` ?** | **Oui** : le compilateur exige la fourniture des collaborateurs, idéal pour les tests unitaires isolés sans Spring. | **Non** : `new Classe()` laisse les champs injectés à `null`, risquant des `NullPointerException` imprévisibles hors conteneur. |
| **Signalé par SonarQube for IDE ?** | **Non** : respecte les règles Clean Code et les recommandations officielles Spring. | **Oui** : déclenche la règle SonarQube **`java:S6813`** (*"Field dependency injection should not be used"*). |

---

## Section B. Services

Tous les services respectent les conventions AutoLoc : annotés `@Service`, champs de repositories `final`, injection par constructeur généré via `@RequiredArgsConstructor` de Lombok, et dépendance directe vers l'abstraction (`I...Repository`).

| Service | Dépendances injectées | Mode d'injection | Justification |
|---|---|---|---|
| `ContratServiceImpl` | `IContratRepository` | Constructeur (`@RequiredArgsConstructor`) | Centralise la logique métier des contrats (rejet d'id à la création, refus de montants négatifs, mise à jour ciblée, cascade vers paiements). |
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur (`@RequiredArgsConstructor`) | Valide les attributs obligatoires de l'agence (nom, ville, adresse non vides) et orchestre l'accès aux données. |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur (`@RequiredArgsConstructor`) | Valide la présence des informations obligatoires (nom, prénom, rôle) de l'employé avant toute persistance. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur (`@RequiredArgsConstructor`) | Contrôle les valeurs numériques (tarif journalier positif ou nul) et gère le catalogue de véhicules. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur (`@RequiredArgsConstructor`) | Assure qu'aucun équipement n'est persisté avec un libellé manquant ou vide. |
| `ClientServiceImpl` | `IClientRepository` | Constructeur (`@RequiredArgsConstructor`) | Vérifie les données d'identification obligatoires du client (nom, e-mail) et gère le cycle de vie client. |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur (`@RequiredArgsConstructor`) | Applique la règle de cohérence temporelle : la date de fin ne doit pas être antérieure à la date de début. |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur (`@RequiredArgsConstructor`) | Service en **lecture seule** (`findById`, `findAll`) ; toute modification ou création passe obligatoirement par le `Contrat`. |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur (`@RequiredArgsConstructor`) | Vérifie la cohérence temporelle (date de début et date de fin) des interventions de maintenance. |

---

## Section C. Messages d'erreur du conteneur

### Message 1
```text
APPLICATION FAILED TO START
Description:
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.ContratController
required a bean of type 'tn.esprit.autoloc.service.IContratService'
that could not be found.
Action:
Consider defining a bean of type 'tn.esprit.autoloc.service.IContratService'
in your configuration.
```

- **Exception levée** : `NoSuchBeanDefinitionException` (enveloppée dans une `UnsatisfiedDependencyException`).
- **Bean concerné** : `tn.esprit.autoloc.service.IContratService` requis par `ContratController`.
- **Deux causes les plus probables côté `ContratServiceImpl`** :
  1. *Omission de l'annotation de stéréotype* : la classe `ContratServiceImpl` n'a pas été annotée avec `@Service` (ou `@Component`), le conteneur ne l'a donc pas enregistrée comme bean.
  2. *Classe hors du package de scan* : la classe `ContratServiceImpl` se trouve dans un package non couvert par le scan de composants de `@SpringBootApplication` (située en dehors de `tn.esprit.autoloc` ou d'un de ses sous-packages).
- **Correction** :
  Ajouter l'annotation `@Service` sur `ContratServiceImpl` et s'assurer que la classe réside bien dans le package `tn.esprit.autoloc.service`.

### Message 2
```text
Description:
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.NotificationController
required a single bean, but 2 were found:
 - emailNotificateur
 - smsNotificateur
Action:
Consider marking one of the beans as @Primary, updating the consumer to accept
multiple beans, or using @Qualifier to identify the bean that should be consumed
```

- **Exception levée** : `NoUniqueBeanDefinitionException` (enveloppée dans une `UnsatisfiedDependencyException`).
- **Bean concerné** : Dépendance injectée dans `NotificationController` pour laquelle deux beans éligibles coexistent (`emailNotificateur` et `smsNotificateur`).
- **Deux corrections différentes** :
  1. *Correction par `@Primary`* : Poser l'annotation `@Primary` sur la classe du bean que l'on souhaite injecter par défaut (par exemple sur `EmailNotificateur`). Cela indique à Spring quel candidat choisir en priorité en l'absence de précision.
     ```java
     @Service
     @Primary
     public class EmailNotificateur implements INotificateur { ... }
     ```
  2. *Correction par `@Qualifier`* : Poser l'annotation `@Qualifier("nomDuBean")` au niveau du point d'injection, c'est-à-dire directement sur le paramètre du constructeur de `NotificationController`.
     ```java
     public NotificationController(@Qualifier("emailNotificateur") INotificateur notificateur) {
         this.notificateur = notificateur;
     }
     ```

### Message 3
```text
The dependencies of some of the beans in the application context form a cycle:
 clientServiceImpl
 | ^
 v |
 reservationServiceImpl
```

- **Exception levée** : `BeanCurrentlyInCreationException`.
- **Pourquoi l'injection par constructeur rend-elle ce cycle impossible à contourner ?**
  Lors d'une injection par constructeur, Spring doit instancier tous les paramètres avant d'appeler le constructeur du bean cible. Pour créer `ClientServiceImpl`, Spring a besoin d'une instance complète de `ReservationServiceImpl`. Mais pour créer `ReservationServiceImpl`, Spring a besoin d'une instance de `ClientServiceImpl`, qui n'est pas encore créée. L'injection par constructeur interdit tout état partiellement instancié : l'application est donc bloquée dans une impasse circulaire dès le démarrage.
- **Piste de conception pour le supprimer** :
  Revoir la conception applicative en respectant le principe de responsabilité unique (SRP) :
  - Extraire la logique partagée ou croisée dans un troisième service (ex. `ClientReservationOrchestrator` ou `ReservationValidationService`).
  - Faire dépendre les services uniquement des repositories inférieurs (`IClientRepository`, `IReservationRepository`) plutôt que d'injecter un service métier dans un autre de même niveau.
  - Utiliser un mécanisme de publication/abonnement d'événements applicatifs (`ApplicationEventPublisher` de Spring).

---

## Section D. Anomalies SonarQube for IDE

| Règle SonarQube | Description & explication | Correction apportée |
|---|---|---|
| **`java:S6813`** | *"Field dependency injection should not be used"* : L'injection par attribut (`@Autowired` sur champ privé) masque les dépendances, interdit les champs `final` (perte d'immuabilité) et rend les tests unitaires hors conteneur fragiles et dépendants de la réflexion. | Remplacement par l'injection par constructeur avec des champs `final` et l'annotation Lombok `@RequiredArgsConstructor`. |
| **`java:S112`** | *"Generic exceptions should never be thrown"* : Lancer une exception générique (`RuntimeException`, `Exception`) nuit à la lisibilité, empêche les gestionnaires d'exceptions de filtrer l'erreur précisément et dégrade la maintenabilité. | Remplacement systématique par une exception métier explicite (`ResourceNotFoundException`) ou une exception standard adaptée (`IllegalArgumentException`). |
| **`java:S1118` / Problème `@Data` sur entités** | Utilisation de `@Data` sur des entités JPA : génère des méthodes `equals`, `hashCode` et `toString` incluant toutes les relations bidirectionnelles, provoquant des boucles infinies (`StackOverflowError`) et des chargements inattendus. | Remplacement de `@Data` par des annotations ciblées (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) dans le package `domain`. |

---

## Section E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` et qui l'exécute ?**
   Il n'y a aucun `new ContratServiceImpl(...)` écrit manuellement dans notre code source. Le `new` est exécuté dynamiquement par le conteneur Spring IoC (`ApplicationContext`) par réflexion au démarrage de l'application. Spring détecte l'annotation `@Service` grâce au scan de composants, résout la dépendance `IContratRepository` (le proxy généré par Spring Data JPA) et appelle le constructeur généré par Lombok.

2. **Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?**
   Pour respecter le principe d'Inversion des Dépendances (DIP des principes SOLID) et favoriser un couplage lâche. En dépendant d'une abstraction (`IContratService`), le contrôleur ne connaît pas les détails d'implémentation. Cela permet de remplacer facilement l'implémentation (ex. pour des mocks ou stubs dans les tests, ou pour une nouvelle version du service) sans modifier une seule ligne du contrôleur.

3. **Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.**
   Par défaut, les beans Spring sont de portée `singleton` : une seule et unique instance est partagée entre tous les threads de l'application (requêtes concurrentes). Si le service stockait un état mutable propre à une requête (ex. `private Long idContratEnCours;`), des requêtes HTTP simultanées écraseraient cette variable partagée. Par exemple, un thread traitant le contrat #42 pourrait voir sa variable remplacée par le contrat #99 d'un autre utilisateur, entraînant une corruption de données majeure (ex. enregistrement d'un paiement sur le mauvais contrat).

4. **Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?**
   Passer directement un objet détaché à `save()` déclenche un `merge` qui écrase tous les attributs en base de données. Si le client n'a envoyé qu'une partie des champs, les champs omis seraient écrasés par `null`. De plus, avec l'option `orphanRemoval = true` sur les collections (comme les paiements d'un contrat), un objet détaché possédant une collection non initialisée ou vide entraînerait la suppression immédiate et accidentelle de tous les enregistrements enfants en cascade. Charger l'entité existante garantit que l'on ne modifie que les champs autorisés tout en préservant l'intégrité du graphe d'objets.

5. **Quelle différence entre `@Component` et `@Bean` ? Entre `@Primary` et `@Qualifier` ? Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?**
   - **`@Component` vs `@Bean`** : `@Component` est une annotation de classe permettant la détection automatique par le composant scanner de Spring. `@Bean` est une annotation de méthode dans une classe `@Configuration`, utilisée pour déclarer explicitement un bean, notamment lorsque la classe provient d'une bibliothèque tierce non modifiable ou requiert une instanciation sur mesure.
   - **`@Primary` vs `@Qualifier`** : `@Primary` se place sur une classe candidate pour la désigner comme choix par défaut en cas d'ambiguïté. `@Qualifier("nom")` se place au point d'injection pour désigner explicitement le bean précis à injecter.
   - **Absence de mutation sur `IPaiementService`** : Dans le domaine métier AutoLoc, le paiement fait partie intégrante du cycle de vie du contrat (relation de composition forte). Créer, modifier ou supprimer un paiement de manière isolée contournerait la cohérence du contrat (solde, statut de validation). Toutes les mutations de paiement sont donc pilotées à travers l'agrégat racine `Contrat` (`CascadeType.ALL`, `orphanRemoval = true`).
