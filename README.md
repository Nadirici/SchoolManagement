# School Management Application

## Technologies utilisées

- **Java :** Langage de programmation principal utilisé pour développer l'application.
- **Apache Tomcat :** Serveur d'applications pour déployer et exécuter l'application web.
- **Hibernate :** Framework ORM (Mapping Objet-Relationnel) permettant une interaction fluide avec la base de données.
- **MySQL :** Base de données relationnelle utilisée pour stocker et gérer les données de l'application.
- **Maven :** Outil de gestion de build et des dépendances pour automatiser la configuration du projet et assurer une intégration fluide des bibliothèques.
- **IntelliJ IDEA :** Environnement de Développement Intégré (IDE) pour un codage, un débogage et une gestion de projet efficaces.

## Comment tester l'application en local ?

1. **Cloner le dépôt**  
   ```bash
   git clone https://github.com/Nadirici/SchoolManagement.git
   ```
   
2. **Se rendre dans le répertoire du projet**  
   ```bash
   cd SchoolManagement
   ```
   
3. **Basculer sur la branche sans Spring**  
   ```bash
   git checkout without-spring-version
   ```

4. **Créer la base de données avec les données de test**  
   - Importer le script `init.sql` dans votre base de données MySQL pour créer la structure et insérer les données de test.

## Fonctionnalités

### Tous les utilisateurs

- **Page d'inscription et de connexion** :  
  Système d'authentification sécurisé avec vérification par email et récupération de mot de passe.

### Administrateur

Les administrateurs disposent d'outils pour gérer l'ensemble de l'application :

- **Statistiques globales** :
    - Vue d'ensemble du nombre total d'étudiants, d'enseignants, pourcentage d'étudiants au dessus 10 de moyenne...
    - Diagrammes et graphiques sur les inscriptions, performances et activités des utilisateurs.

- **Gestion des étudiants** :
    - Opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
    - Inscrire les étudiants à des cours directement via le tableau de bord selon les disponibilités.
    - Barre de recherche
    - Performances d'un étudiant (notes + stats)

- **Gestion des enseignants** :
    - Opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
    - Attribution de cours aux enseignants en fonction des disponibilités.
    - Barre de recherche
    - Liste des cours enseignés

- **Gestion des cours** :
    - Opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
    - Liaison automatique entre cours et enseignants/étudiants concernés.
    - Liste des devoirs associés à ce cours (stats)
    - Liste des étudiants inscrits (stats)
    - Planification d'un cours en fonction des disponibilités de l'enseignant

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
