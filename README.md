# School Management Application

## Fonctionnalités

### Tous les utilisateurs

- **Page d'inscription et de connexion** :  
  Système d'authentification sécurisé avec vérification par email et récupération de mot de passe.

---

### Administrateur

Les administrateurs disposent d'outils pour gérer l'ensemble de l'application :

- **Statistiques globales** :
    - Vue d'ensemble du nombre total d'étudiants, d'enseignants, pourcentage d'étudiants au dessus 10 de moyenne...
    - Diagrammes et graphiques sur les inscriptions, performances et activités des utilisateurs. (En cours de développement)

- **Gestion des étudiants** :
    - Opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
    - Inscrire les étudiants à des cours directement via le tableau de bord.
    - Barre de recherche
    - Performances d'un étudiant (notes + stats)

- **Gestion des enseignants** :
    - Opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
    - Attribution de cours aux enseignants.
    - Barre de recherche
    - Liste des cours enseignés

- **Gestion des cours** :
    - Opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
    - Liaison automatique entre cours et enseignants/étudiants concernés.
    - Liste des devoirs associés à ce cours (stats)
    - Liste des étudiants inscrits (stats)

- **Gestion des demandes d'inscription** :
    - Consulter toutes les demandes d'inscription des étudiants ou enseignants.
    - Accepter ou refuser les demandes.

---

### Enseignant

Les enseignants peuvent gérer leurs cours et suivre la progression de leurs étudiants :

- **Aperçu personnel** :
    - Affichage de leurs informations (nom, email, département, etc.).
    - Statistiques globales sur leurs cours (nombre d'étudiants inscrits, moyenne générale, etc.).

- **Gestion des cours** :
    - Liste de tous les cours qu'ils enseignent.
    - Détails de chaque cours, incluant :
        - Liste des étudiants inscrits.
        - Liste des devoirs associés au cours.
        - Statistiques avancées par étudiant ou par devoir.

- **Gestion des devoirs** :
    - Opérations CRUD pour créer, éditer ou supprimer des devoirs.
    - Attribution de notes aux devoirs avec des seuils personnalisables.

---

### Étudiant

Les étudiants bénéficient d'une interface simple pour suivre leurs résulats :

- **Profil personnel** :
    - Consultation de leurs informations personnelles (nom, email, date de naissance, etc.).
    - Aperçu des statistiques personnelles (moyenne générale, progression dans les cours).

- **Gestion des cours** :
    - Liste complète des cours auxquels ils sont inscrits avec des statistiques
    - Fonctionnalité d'inscription aux cours disponibles

- **Détails d'un cours** :
    - Consultation des devoirs et des notes obtenues pour chaque cours.
    - Statistiques détaillées sur leurs performances dans le cours

- **Bulletin de notes** :
    - Téléchargement en PDF de leur bulletin de notes, incluant :
        - Liste des cours.
        - Moyenne de l'étudiant par cours
        - Statistique pour chaque cours (moyenne de classe, note minimum et maximum)
