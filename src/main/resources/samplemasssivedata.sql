-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : lun. 18 nov. 2024 à 00:21
-- Version du serveur : 8.2.0
-- Version de PHP : 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `school_db`
--

DELIMITER $$
--
-- Procédures
--
DROP PROCEDURE IF EXISTS `AssignGrades`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `AssignGrades` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE enrollmentId BINARY(16);
    DECLARE assignmentId BINARY(16);
    
    WHILE i <= 500 DO
        -- Récupération d'un ID d'inscription et d'un devoir aléatoires
        SELECT id INTO enrollmentId FROM enrollments ORDER BY RAND() LIMIT 1;
        SELECT id INTO assignmentId FROM assignments ORDER BY RAND() LIMIT 1;
        
        INSERT INTO grades (id, score, assignment_id, enrollment_id)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            ROUND(RAND() * 20, 1), -- Note aléatoire sur 20
            assignmentId,
            enrollmentId
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `EnrollStudents`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `EnrollStudents` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE studentId BINARY(16);
    DECLARE courseId BINARY(16);
    
    WHILE i <= 200 DO
        -- Récupération d'un ID étudiant et cours aléatoires
        SELECT id INTO studentId FROM students ORDER BY RAND() LIMIT 1;
        SELECT id INTO courseId FROM courses ORDER BY RAND() LIMIT 1;
        
        INSERT INTO enrollments (id, course_id, student_id)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            courseId,
            studentId
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `InsertAssignments`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertAssignments` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE courseId BINARY(16);
    
    WHILE i <= 75 DO
        -- Récupération d'un ID de cours aléatoire
        SELECT id INTO courseId FROM courses ORDER BY RAND() LIMIT 1;
        
        INSERT INTO assignments (id, coefficient, description, title, course_id)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            ROUND(RAND() * 2, 1), -- Coefficient aléatoire entre 0 et 2
            CONCAT('Description of Assignment', i),
            CONCAT('Assignment', i),
            courseId
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `InsertCourses`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertCourses` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE teacherId BINARY(16);
    
    WHILE i <= 15 DO
        -- Récupération d'un ID de professeur aléatoire
        SELECT id INTO teacherId FROM teachers ORDER BY RAND() LIMIT 1;
        
        INSERT INTO courses (id, description, name, teacher_id)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            CONCAT('Description of Course', i),
            CONCAT('Course', i),
            teacherId
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `InsertRequests`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertRequests` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE studentId BINARY(16);
    DECLARE teacherId BINARY(16);
    
    WHILE i <= 25 DO
        -- Récupération d'un ID étudiant et professeur aléatoires
        SELECT id INTO studentId FROM students ORDER BY RAND() LIMIT 1;
        SELECT id INTO teacherId FROM teachers ORDER BY RAND() LIMIT 1;
        
        INSERT INTO requests (id, status, student_id, teacher_id)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            IF(i MOD 2 = 0, 1, 0), -- Status aléatoire : 1 (validé) ou 0 (en attente)
            studentId,
            teacherId
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `InsertRoles`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertRoles` ()   BEGIN
    INSERT INTO roles (role) VALUES ('ADMIN'), ('STUDENT'), ('TEACHER');
END$$

DROP PROCEDURE IF EXISTS `InsertStudents`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertStudents` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 50 DO
        INSERT INTO students (id, email, firstname, is_verified, lastname, password, salt, date_of_birth)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            CONCAT('student', i, '@example.com'),
            CONCAT('Student', i),
            1,
            CONCAT('Lastname', i),
            CONCAT('hashed_password', i),
            CONCAT('salt', i),
            DATE_ADD('2000-01-01', INTERVAL i DAY)
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `InsertTeachers`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertTeachers` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10 DO
        INSERT INTO teachers (id, email, firstname, is_verified, lastname, password, salt, department)
        VALUES (
            UNHEX(REPLACE(UUID(),'-','')),
            CONCAT('teacher', i, '@example.com'),
            CONCAT('Teacher', i),
            1,
            CONCAT('Lastname', i),
            CONCAT('hashed_password', i),
            CONCAT('salt', i),
            IF(i MOD 2 = 0, 'Mathematics', 'Physics')
        );
        SET i = i + 1;
    END WHILE;
END$$

DROP PROCEDURE IF EXISTS `InsertUsers`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertUsers` ()   BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE roleId INT;
    
    WHILE i <= 30 DO
        -- Récupération d'un rôle aléatoire
        SELECT id INTO roleId FROM roles ORDER BY RAND() LIMIT 1;
        
        INSERT INTO users (id, email, password, role_id)
        VALUES (
            i,
            CONCAT('user', i, '@example.com'),
            CONCAT('hashed_password', i),
            roleId
        );
        SET i = i + 1;
    END WHILE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `admins`
--

DROP TABLE IF EXISTS `admins`;
CREATE TABLE IF NOT EXISTS `admins` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `is_verified` bit(1) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK47bvqemyk6vlm0w7crc3opdd4` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `admins`
--

INSERT INTO `admins` (`id`, `email`, `firstname`, `is_verified`, `lastname`, `password`, `salt`) VALUES
(0xe2437152264841acbdd65f6f273839b0, 'admin@admin.com', 'admin', b'1', 'admin', 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg==');

-- --------------------------------------------------------

--
-- Structure de la table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
CREATE TABLE IF NOT EXISTS `assignments` (
  `id` binary(16) NOT NULL,
  `coefficient` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `course_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6p1m72jobsvmrrn4bpj4168mg` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `assignments`
--

INSERT INTO `assignments` (`id`, `coefficient`, `description`, `title`, `course_id`) VALUES
(0xafb10d95a53e11ef85cfe89c25e08459, 0.5, 'Description of Assignment1', 'Assignment1', 0xafaed8b8a53e11ef85cfe89c25e08459),
(0xafb12ca2a53e11ef85cfe89c25e08459, 2, 'Description of Assignment2', 'Assignment2', 0xafaf94bca53e11ef85cfe89c25e08459),
(0xafb14901a53e11ef85cfe89c25e08459, 1.5, 'Description of Assignment3', 'Assignment3', 0xafafc964a53e11ef85cfe89c25e08459),
(0xafb16326a53e11ef85cfe89c25e08459, 1.2, 'Description of Assignment4', 'Assignment4', 0xafb02693a53e11ef85cfe89c25e08459),
(0xafb17dafa53e11ef85cfe89c25e08459, 0.9, 'Description of Assignment5', 'Assignment5', 0xafaf068ca53e11ef85cfe89c25e08459),
(0xafb19784a53e11ef85cfe89c25e08459, 1.2, 'Description of Assignment6', 'Assignment6', 0xafaf4413a53e11ef85cfe89c25e08459),
(0xafb1b448a53e11ef85cfe89c25e08459, 0.4, 'Description of Assignment7', 'Assignment7', 0xafafe33aa53e11ef85cfe89c25e08459),
(0xafb1ce73a53e11ef85cfe89c25e08459, 0.1, 'Description of Assignment8', 'Assignment8', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb1e8d7a53e11ef85cfe89c25e08459, 0.1, 'Description of Assignment9', 'Assignment9', 0xafaf94bca53e11ef85cfe89c25e08459),
(0xafb20453a53e11ef85cfe89c25e08459, 1.7, 'Description of Assignment10', 'Assignment10', 0xafb06a61a53e11ef85cfe89c25e08459),
(0xafb21feba53e11ef85cfe89c25e08459, 0.5, 'Description of Assignment11', 'Assignment11', 0xafb00db8a53e11ef85cfe89c25e08459),
(0xafb239efa53e11ef85cfe89c25e08459, 2, 'Description of Assignment12', 'Assignment12', 0xafaf5ee3a53e11ef85cfe89c25e08459),
(0xafb253a9a53e11ef85cfe89c25e08459, 0.8, 'Description of Assignment13', 'Assignment13', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb26d9fa53e11ef85cfe89c25e08459, 1.1, 'Description of Assignment14', 'Assignment14', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb28780a53e11ef85cfe89c25e08459, 1.4, 'Description of Assignment15', 'Assignment15', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb2a1c2a53e11ef85cfe89c25e08459, 1.1, 'Description of Assignment16', 'Assignment16', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb2bcc0a53e11ef85cfe89c25e08459, 1.8, 'Description of Assignment17', 'Assignment17', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb2d6fca53e11ef85cfe89c25e08459, 1.5, 'Description of Assignment18', 'Assignment18', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb2f0daa53e11ef85cfe89c25e08459, 1.4, 'Description of Assignment19', 'Assignment19', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb319f2a53e11ef85cfe89c25e08459, 1.3, 'Description of Assignment20', 'Assignment20', 0xafaed8b8a53e11ef85cfe89c25e08459),
(0xafb332f5a53e11ef85cfe89c25e08459, 0, 'Description of Assignment21', 'Assignment21', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb35c05a53e11ef85cfe89c25e08459, 0.2, 'Description of Assignment22', 'Assignment22', 0xafafe33aa53e11ef85cfe89c25e08459),
(0xafb3769ea53e11ef85cfe89c25e08459, 1.8, 'Description of Assignment23', 'Assignment23', 0xafb06a61a53e11ef85cfe89c25e08459),
(0xafb39148a53e11ef85cfe89c25e08459, 1.1, 'Description of Assignment24', 'Assignment24', 0xafafc964a53e11ef85cfe89c25e08459),
(0xafb3ac4da53e11ef85cfe89c25e08459, 0.8, 'Description of Assignment25', 'Assignment25', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb3c6cca53e11ef85cfe89c25e08459, 1.4, 'Description of Assignment26', 'Assignment26', 0xafaf4413a53e11ef85cfe89c25e08459),
(0xafb3e0f0a53e11ef85cfe89c25e08459, 1.6, 'Description of Assignment27', 'Assignment27', 0xafaed8b8a53e11ef85cfe89c25e08459),
(0xafb3fabaa53e11ef85cfe89c25e08459, 0.4, 'Description of Assignment28', 'Assignment28', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb414c4a53e11ef85cfe89c25e08459, 1.9, 'Description of Assignment29', 'Assignment29', 0xafaeba53a53e11ef85cfe89c25e08459),
(0xafb42ec1a53e11ef85cfe89c25e08459, 1, 'Description of Assignment30', 'Assignment30', 0xafaf94bca53e11ef85cfe89c25e08459),
(0xafb44932a53e11ef85cfe89c25e08459, 0.1, 'Description of Assignment31', 'Assignment31', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb463d8a53e11ef85cfe89c25e08459, 0.3, 'Description of Assignment32', 'Assignment32', 0xafaed8b8a53e11ef85cfe89c25e08459),
(0xafb47e6da53e11ef85cfe89c25e08459, 0.2, 'Description of Assignment33', 'Assignment33', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb498c9a53e11ef85cfe89c25e08459, 1.1, 'Description of Assignment34', 'Assignment34', 0xafaf068ca53e11ef85cfe89c25e08459),
(0xafb4b3d1a53e11ef85cfe89c25e08459, 0.3, 'Description of Assignment35', 'Assignment35', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb4dd64a53e11ef85cfe89c25e08459, 1.7, 'Description of Assignment36', 'Assignment36', 0xafaed8b8a53e11ef85cfe89c25e08459),
(0xafb4fa7fa53e11ef85cfe89c25e08459, 1.6, 'Description of Assignment37', 'Assignment37', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb527dda53e11ef85cfe89c25e08459, 0.8, 'Description of Assignment38', 'Assignment38', 0xafaf068ca53e11ef85cfe89c25e08459),
(0xafb54337a53e11ef85cfe89c25e08459, 0.4, 'Description of Assignment39', 'Assignment39', 0xafafe33aa53e11ef85cfe89c25e08459),
(0xafb56d92a53e11ef85cfe89c25e08459, 0, 'Description of Assignment40', 'Assignment40', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb586e9a53e11ef85cfe89c25e08459, 1.9, 'Description of Assignment41', 'Assignment41', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb5b144a53e11ef85cfe89c25e08459, 0.7, 'Description of Assignment42', 'Assignment42', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb5ca84a53e11ef85cfe89c25e08459, 0.2, 'Description of Assignment43', 'Assignment43', 0xafaeba53a53e11ef85cfe89c25e08459),
(0xafb5f404a53e11ef85cfe89c25e08459, 0, 'Description of Assignment44', 'Assignment44', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb60d0ea53e11ef85cfe89c25e08459, 1.4, 'Description of Assignment45', 'Assignment45', 0xafb06a61a53e11ef85cfe89c25e08459),
(0xafb635e8a53e11ef85cfe89c25e08459, 1.8, 'Description of Assignment46', 'Assignment46', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb65ed7a53e11ef85cfe89c25e08459, 0.5, 'Description of Assignment47', 'Assignment47', 0xafb06a61a53e11ef85cfe89c25e08459),
(0xafb67926a53e11ef85cfe89c25e08459, 1.3, 'Description of Assignment48', 'Assignment48', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb69351a53e11ef85cfe89c25e08459, 1.3, 'Description of Assignment49', 'Assignment49', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb6af3ba53e11ef85cfe89c25e08459, 0.3, 'Description of Assignment50', 'Assignment50', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb6d8d9a53e11ef85cfe89c25e08459, 1.3, 'Description of Assignment51', 'Assignment51', 0xafafc964a53e11ef85cfe89c25e08459),
(0xafb6f261a53e11ef85cfe89c25e08459, 0, 'Description of Assignment52', 'Assignment52', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb70d23a53e11ef85cfe89c25e08459, 1.1, 'Description of Assignment53', 'Assignment53', 0xafaf4413a53e11ef85cfe89c25e08459),
(0xafb72711a53e11ef85cfe89c25e08459, 0.9, 'Description of Assignment54', 'Assignment54', 0xafaeba53a53e11ef85cfe89c25e08459),
(0xafb740f8a53e11ef85cfe89c25e08459, 0.4, 'Description of Assignment55', 'Assignment55', 0xafb00db8a53e11ef85cfe89c25e08459),
(0xafb75cfea53e11ef85cfe89c25e08459, 0.8, 'Description of Assignment56', 'Assignment56', 0xafaf4413a53e11ef85cfe89c25e08459),
(0xafb77980a53e11ef85cfe89c25e08459, 0.5, 'Description of Assignment57', 'Assignment57', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb7950ca53e11ef85cfe89c25e08459, 0.3, 'Description of Assignment58', 'Assignment58', 0xafaf068ca53e11ef85cfe89c25e08459),
(0xafb7b189a53e11ef85cfe89c25e08459, 0.2, 'Description of Assignment59', 'Assignment59', 0xafafc964a53e11ef85cfe89c25e08459),
(0xafb7cd32a53e11ef85cfe89c25e08459, 1.3, 'Description of Assignment60', 'Assignment60', 0xafaed8b8a53e11ef85cfe89c25e08459),
(0xafb7e83ba53e11ef85cfe89c25e08459, 1, 'Description of Assignment61', 'Assignment61', 0xafaf79f1a53e11ef85cfe89c25e08459),
(0xafb8124ba53e11ef85cfe89c25e08459, 1.5, 'Description of Assignment62', 'Assignment62', 0xafb00db8a53e11ef85cfe89c25e08459),
(0xafb82cc2a53e11ef85cfe89c25e08459, 1.7, 'Description of Assignment63', 'Assignment63', 0xafaf4413a53e11ef85cfe89c25e08459),
(0xafb857f7a53e11ef85cfe89c25e08459, 1.7, 'Description of Assignment64', 'Assignment64', 0xafaeba53a53e11ef85cfe89c25e08459),
(0xafb874e0a53e11ef85cfe89c25e08459, 1.8, 'Description of Assignment65', 'Assignment65', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb88fe2a53e11ef85cfe89c25e08459, 0.1, 'Description of Assignment66', 'Assignment66', 0xafae9d4aa53e11ef85cfe89c25e08459),
(0xafb8ab57a53e11ef85cfe89c25e08459, 1.8, 'Description of Assignment67', 'Assignment67', 0xafaf94bca53e11ef85cfe89c25e08459),
(0xafb8c5f9a53e11ef85cfe89c25e08459, 0.8, 'Description of Assignment68', 'Assignment68', 0xafafe33aa53e11ef85cfe89c25e08459),
(0xafb8e114a53e11ef85cfe89c25e08459, 1.9, 'Description of Assignment69', 'Assignment69', 0xafafaeb6a53e11ef85cfe89c25e08459),
(0xafb90b38a53e11ef85cfe89c25e08459, 0.3, 'Description of Assignment70', 'Assignment70', 0xafb06a61a53e11ef85cfe89c25e08459),
(0xafb927dba53e11ef85cfe89c25e08459, 0.4, 'Description of Assignment71', 'Assignment71', 0xafaf5ee3a53e11ef85cfe89c25e08459),
(0xafb94437a53e11ef85cfe89c25e08459, 0.3, 'Description of Assignment72', 'Assignment72', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb96f81a53e11ef85cfe89c25e08459, 0.6, 'Description of Assignment73', 'Assignment73', 0xafb05098a53e11ef85cfe89c25e08459),
(0xafb98cdba53e11ef85cfe89c25e08459, 1.7, 'Description of Assignment74', 'Assignment74', 0xafaeba53a53e11ef85cfe89c25e08459),
(0xafb9b99aa53e11ef85cfe89c25e08459, 1.9, 'Description of Assignment75', 'Assignment75', 0xafaf068ca53e11ef85cfe89c25e08459);

-- --------------------------------------------------------

--
-- Structure de la table `courses`
--

DROP TABLE IF EXISTS `courses`;
CREATE TABLE IF NOT EXISTS `courses` (
  `id` binary(16) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `teacher_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK468oyt88pgk2a0cxrvxygadqg` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `courses`
--

INSERT INTO `courses` (`id`, `description`, `name`, `teacher_id`) VALUES
(0xafae9d4aa53e11ef85cfe89c25e08459, 'Description of Course1', 'Course1', 0xafacf640a53e11ef85cfe89c25e08459),
(0xafaeba53a53e11ef85cfe89c25e08459, 'Description of Course2', 'Course2', 0xafad5dbba53e11ef85cfe89c25e08459),
(0xafaed8b8a53e11ef85cfe89c25e08459, 'Description of Course3', 'Course3', 0xafadb743a53e11ef85cfe89c25e08459),
(0xafaf068ca53e11ef85cfe89c25e08459, 'Description of Course4', 'Course4', 0xafad441aa53e11ef85cfe89c25e08459),
(0xafaf4413a53e11ef85cfe89c25e08459, 'Description of Course5', 'Course5', 0xafadcf2fa53e11ef85cfe89c25e08459),
(0xafaf5ee3a53e11ef85cfe89c25e08459, 'Description of Course6', 'Course6', 0xafad441aa53e11ef85cfe89c25e08459),
(0xafaf79f1a53e11ef85cfe89c25e08459, 'Description of Course7', 'Course7', 0xafad5dbba53e11ef85cfe89c25e08459),
(0xafaf94bca53e11ef85cfe89c25e08459, 'Description of Course8', 'Course8', 0xafad5dbba53e11ef85cfe89c25e08459),
(0xafafaeb6a53e11ef85cfe89c25e08459, 'Description of Course9', 'Course9', 0xafacf640a53e11ef85cfe89c25e08459),
(0xafafc964a53e11ef85cfe89c25e08459, 'Description of Course10', 'Course10', 0xafacf640a53e11ef85cfe89c25e08459),
(0xafafe33aa53e11ef85cfe89c25e08459, 'Description of Course11', 'Course11', 0xafad2b20a53e11ef85cfe89c25e08459),
(0xafb00db8a53e11ef85cfe89c25e08459, 'Description of Course12', 'Course12', 0xafacf640a53e11ef85cfe89c25e08459),
(0xafb02693a53e11ef85cfe89c25e08459, 'Description of Course13', 'Course13', 0xafadb743a53e11ef85cfe89c25e08459),
(0xafb05098a53e11ef85cfe89c25e08459, 'Description of Course14', 'Course14', 0xafacf640a53e11ef85cfe89c25e08459),
(0xafb06a61a53e11ef85cfe89c25e08459, 'Description of Course15', 'Course15', 0xafad5dbba53e11ef85cfe89c25e08459);

-- --------------------------------------------------------

--
-- Structure de la table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
CREATE TABLE IF NOT EXISTS `enrollments` (
  `id` binary(16) NOT NULL,
  `course_id` binary(16) NOT NULL,
  `student_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`),
  KEY `FK8kf1u1857xgo56xbfmnif2c51` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `enrollments`
--

INSERT INTO `enrollments` (`id`, `course_id`, `student_id`) VALUES
(0x26385e9ba53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa92159a53e11ef85cfe89c25e08459),
(0x2638e70ba53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafa8ae24a53e11ef85cfe89c25e08459),
(0x263909f9a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x26394943a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x26398690a53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x2639a243a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafabe2d3a53e11ef85cfe89c25e08459),
(0x2639be36a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafab7110a53e11ef85cfe89c25e08459),
(0x2639d91ca53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafac1503a53e11ef85cfe89c25e08459),
(0x263a0452a53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa8c77ca53e11ef85cfe89c25e08459),
(0x263a1e85a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafa908f6a53e11ef85cfe89c25e08459),
(0x263a39eda53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa92159a53e11ef85cfe89c25e08459),
(0x263a5508a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafabe2d3a53e11ef85cfe89c25e08459),
(0x263a6f5ea53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa8c77ca53e11ef85cfe89c25e08459),
(0x263a99e4a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafab58e9a53e11ef85cfe89c25e08459),
(0x263ab430a53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x263ade76a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa7ca3fa53e11ef85cfe89c25e08459),
(0x263b08b3a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x263b22f8a53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x263b4e8ea53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa9fc3fa53e11ef85cfe89c25e08459),
(0x263b68e2a53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafa79734a53e11ef85cfe89c25e08459),
(0x263b83e6a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459),
(0x263b9f2ea53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa92159a53e11ef85cfe89c25e08459),
(0x263bbb3ba53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa7ca3fa53e11ef85cfe89c25e08459),
(0x263bd743a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafa679efa53e11ef85cfe89c25e08459),
(0x263bf328a53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa81593a53e11ef85cfe89c25e08459),
(0x263c0e81a53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa679efa53e11ef85cfe89c25e08459),
(0x263c29fea53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa97f9fa53e11ef85cfe89c25e08459),
(0x263c4515a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa8c77ca53e11ef85cfe89c25e08459),
(0x263c5ff4a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa9e400a53e11ef85cfe89c25e08459),
(0x263c7b7ea53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa9fc3fa53e11ef85cfe89c25e08459),
(0x263c96b9a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafaac69da53e11ef85cfe89c25e08459),
(0x263cb303a53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafab58e9a53e11ef85cfe89c25e08459),
(0x263cce52a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa94c76a53e11ef85cfe89c25e08459),
(0x263ce91ca53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafaa6d6ba53e11ef85cfe89c25e08459),
(0x263d03fea53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafab9a16a53e11ef85cfe89c25e08459),
(0x263d1f12a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafaa149ea53e11ef85cfe89c25e08459),
(0x263d3b79a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa9e400a53e11ef85cfe89c25e08459),
(0x263d56d1a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafaa6d6ba53e11ef85cfe89c25e08459),
(0x263d71c4a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafaac69da53e11ef85cfe89c25e08459),
(0x263d8c92a53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x263da76ea53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafabe2d3a53e11ef85cfe89c25e08459),
(0x263dc28da53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafab2863a53e11ef85cfe89c25e08459),
(0x263ddf58a53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x263dfad9a53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa97f9fa53e11ef85cfe89c25e08459),
(0x263e16a0a53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafa81593a53e11ef85cfe89c25e08459),
(0x263e4361a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafac1503a53e11ef85cfe89c25e08459),
(0x263e5d9ba53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa97f9fa53e11ef85cfe89c25e08459),
(0x263e8844a53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa92159a53e11ef85cfe89c25e08459),
(0x263ea351a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa9fc3fa53e11ef85cfe89c25e08459),
(0x263ebf13a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa81593a53e11ef85cfe89c25e08459),
(0x263edac0a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa7e371a53e11ef85cfe89c25e08459),
(0x263ef84ea53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x263f2346a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa8470ba53e11ef85cfe89c25e08459),
(0x263f4d7ca53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x263f66cea53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafaa149ea53e11ef85cfe89c25e08459),
(0x263f906aa53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafaa149ea53e11ef85cfe89c25e08459),
(0x263faa19a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafaaf7aca53e11ef85cfe89c25e08459),
(0x263fd50da53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x263feef4a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafaaad96a53e11ef85cfe89c25e08459),
(0x26400872a53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa8ae24a53e11ef85cfe89c25e08459),
(0x26403244a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafaa2cfda53e11ef85cfe89c25e08459),
(0x2640505ea53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafaac69da53e11ef85cfe89c25e08459),
(0x26408edaa53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa81593a53e11ef85cfe89c25e08459),
(0x2640aa16a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa7fc9ea53e11ef85cfe89c25e08459),
(0x2640c587a53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa94c76a53e11ef85cfe89c25e08459),
(0x2640e115a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa8470ba53e11ef85cfe89c25e08459),
(0x2640fb9fa53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa6eeb9a53e11ef85cfe89c25e08459),
(0x264116b9a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafabfc6ea53e11ef85cfe89c25e08459),
(0x2641320ca53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa8797da53e11ef85cfe89c25e08459),
(0x26414cfba53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafabfc6ea53e11ef85cfe89c25e08459),
(0x2641788fa53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafaa6d6ba53e11ef85cfe89c25e08459),
(0x2641937ea53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa79734a53e11ef85cfe89c25e08459),
(0x2641bfa0a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x2641da99a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa7ca3fa53e11ef85cfe89c25e08459),
(0x2641f5c9a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459),
(0x264212ada53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa8470ba53e11ef85cfe89c25e08459),
(0x26422de7a53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa8797da53e11ef85cfe89c25e08459),
(0x264259a2a53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa893e1a53e11ef85cfe89c25e08459),
(0x26428491a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa97f9fa53e11ef85cfe89c25e08459),
(0x2642a132a53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa9fc3fa53e11ef85cfe89c25e08459),
(0x2642d13da53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafab0fc1a53e11ef85cfe89c25e08459),
(0x2642eb09a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa7e371a53e11ef85cfe89c25e08459),
(0x26430535a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa6954fa53e11ef85cfe89c25e08459),
(0x264335efa53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459),
(0x2643516ba53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafa8ae24a53e11ef85cfe89c25e08459),
(0x26436d2da53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafa7b13da53e11ef85cfe89c25e08459),
(0x2643882aa53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa8470ba53e11ef85cfe89c25e08459),
(0x2643a31ca53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafaaf7aca53e11ef85cfe89c25e08459),
(0x2643be3ba53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafaaf7aca53e11ef85cfe89c25e08459),
(0x2643d8b6a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafabe2d3a53e11ef85cfe89c25e08459),
(0x2643f386a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x26440de6a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa9cb42a53e11ef85cfe89c25e08459),
(0x2644287ca53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa8e0cea53e11ef85cfe89c25e08459),
(0x264452c3a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa9b2b7a53e11ef85cfe89c25e08459),
(0x26446dbaa53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafaaf7aca53e11ef85cfe89c25e08459),
(0x264497aea53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa79734a53e11ef85cfe89c25e08459),
(0x2644b200a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafaaad96a53e11ef85cfe89c25e08459),
(0x2644ddbfa53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa96635a53e11ef85cfe89c25e08459),
(0x2644fa0fa53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafa8e0cea53e11ef85cfe89c25e08459),
(0x264516e8a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa908f6a53e11ef85cfe89c25e08459),
(0x2645351fa53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafa8797da53e11ef85cfe89c25e08459),
(0x26455260a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x26456f5ea53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa908f6a53e11ef85cfe89c25e08459),
(0x26458b11a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafac1503a53e11ef85cfe89c25e08459),
(0x2645a6e9a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa7b13da53e11ef85cfe89c25e08459),
(0x2645d3a3a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa79734a53e11ef85cfe89c25e08459),
(0x2645f059a53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafab7110a53e11ef85cfe89c25e08459),
(0x2646228aa53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa9cb42a53e11ef85cfe89c25e08459),
(0x26463d6ea53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafab58e9a53e11ef85cfe89c25e08459),
(0x26465799a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafab58e9a53e11ef85cfe89c25e08459),
(0x26467397a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa92159a53e11ef85cfe89c25e08459),
(0x26468e2ca53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa893e1a53e11ef85cfe89c25e08459),
(0x2646b98ca53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafabe2d3a53e11ef85cfe89c25e08459),
(0x2646d52ca53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafaadf0ca53e11ef85cfe89c25e08459),
(0x26470136a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x26472d7fa53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafab4037a53e11ef85cfe89c25e08459),
(0x2647471ca53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa8c77ca53e11ef85cfe89c25e08459),
(0x264779c9a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x26479624a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa97f9fa53e11ef85cfe89c25e08459),
(0x2647b2aaa53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa679efa53e11ef85cfe89c25e08459),
(0x2647cec0a53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa94c76a53e11ef85cfe89c25e08459),
(0x2647e958a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafaa2cfda53e11ef85cfe89c25e08459),
(0x2648141da53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x26482e99a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafab9a16a53e11ef85cfe89c25e08459),
(0x264859a0a53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa9fc3fa53e11ef85cfe89c25e08459),
(0x26487369a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa92159a53e11ef85cfe89c25e08459),
(0x26488cb5a53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa82e92a53e11ef85cfe89c25e08459),
(0x2648b678a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa6954fa53e11ef85cfe89c25e08459),
(0x2648d010a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafab58e9a53e11ef85cfe89c25e08459),
(0x2648fa5aa53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa9cb42a53e11ef85cfe89c25e08459),
(0x2649243ca53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x26493de1a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa8797da53e11ef85cfe89c25e08459),
(0x264968b8a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafab7110a53e11ef85cfe89c25e08459),
(0x264982b8a53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafaa6d6ba53e11ef85cfe89c25e08459),
(0x26499c5fa53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa82e92a53e11ef85cfe89c25e08459),
(0x2649c700a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa7b13da53e11ef85cfe89c25e08459),
(0x2649e21da53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafaaad96a53e11ef85cfe89c25e08459),
(0x264a0cc2a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafaa9528a53e11ef85cfe89c25e08459),
(0x264a2666a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa679efa53e11ef85cfe89c25e08459),
(0x264a500ba53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafaa149ea53e11ef85cfe89c25e08459),
(0x264a7b47a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafab4037a53e11ef85cfe89c25e08459),
(0x264a9551a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa908f6a53e11ef85cfe89c25e08459),
(0x264abf54a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa81593a53e11ef85cfe89c25e08459),
(0x264ad8c9a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa8e0cea53e11ef85cfe89c25e08459),
(0x264b0301a53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafa728e8a53e11ef85cfe89c25e08459),
(0x264b1cfba53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafaadf0ca53e11ef85cfe89c25e08459),
(0x264b37eba53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa96635a53e11ef85cfe89c25e08459),
(0x264b530ba53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x264b6e42a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa94c76a53e11ef85cfe89c25e08459),
(0x264b892ea53f11ef85cfe89c25e08459, 0xafafaeb6a53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x264ba468a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafab7110a53e11ef85cfe89c25e08459),
(0x264bbee3a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafabfc6ea53e11ef85cfe89c25e08459),
(0x264beb08a53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafab2863a53e11ef85cfe89c25e08459),
(0x264c066ba53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa75273a53e11ef85cfe89c25e08459),
(0x264c2294a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa679efa53e11ef85cfe89c25e08459),
(0x264c3e42a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafab4037a53e11ef85cfe89c25e08459),
(0x264c59bca53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x264c74c5a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa6eeb9a53e11ef85cfe89c25e08459),
(0x264c8fb0a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa82e92a53e11ef85cfe89c25e08459),
(0x264ca9d1a53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa82e92a53e11ef85cfe89c25e08459),
(0x264cc44da53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa893e1a53e11ef85cfe89c25e08459),
(0x264cdf23a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x264d0a11a53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa6954fa53e11ef85cfe89c25e08459),
(0x264d243da53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x264d4efca53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafaa9528a53e11ef85cfe89c25e08459),
(0x264d6a17a53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa8ae24a53e11ef85cfe89c25e08459),
(0x264d85dea53f11ef85cfe89c25e08459, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa728e8a53e11ef85cfe89c25e08459),
(0x264da14ea53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafab9a16a53e11ef85cfe89c25e08459),
(0x264dbc5ba53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa79734a53e11ef85cfe89c25e08459),
(0x264dd6d8a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafac1503a53e11ef85cfe89c25e08459),
(0x264df25ca53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa94c76a53e11ef85cfe89c25e08459),
(0x264e0cffa53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa7b13da53e11ef85cfe89c25e08459),
(0x264e2826a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa9b2b7a53e11ef85cfe89c25e08459),
(0x264e4639a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa8470ba53e11ef85cfe89c25e08459),
(0x264e6407a53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafa81593a53e11ef85cfe89c25e08459),
(0x264e8016a53f11ef85cfe89c25e08459, 0xafafc964a53e11ef85cfe89c25e08459, 0xafa7b13da53e11ef85cfe89c25e08459),
(0x264e9c38a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafab7110a53e11ef85cfe89c25e08459),
(0x264eb79ea53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa893e1a53e11ef85cfe89c25e08459),
(0x264ed2e7a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa9999ca53e11ef85cfe89c25e08459),
(0x264eedfca53f11ef85cfe89c25e08459, 0xafaf4413a53e11ef85cfe89c25e08459, 0xafa76c70a53e11ef85cfe89c25e08459),
(0x264f083ca53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa893e1a53e11ef85cfe89c25e08459),
(0x264f329fa53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa94c76a53e11ef85cfe89c25e08459),
(0x264f4d5ea53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafaa9528a53e11ef85cfe89c25e08459),
(0x264f6977a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa679efa53e11ef85cfe89c25e08459),
(0x264f9444a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa9cb42a53e11ef85cfe89c25e08459),
(0x264fae73a53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa96635a53e11ef85cfe89c25e08459),
(0x264fd83ca53f11ef85cfe89c25e08459, 0xafae9d4aa53e11ef85cfe89c25e08459, 0xafa8ae24a53e11ef85cfe89c25e08459),
(0x2650036ba53f11ef85cfe89c25e08459, 0xafafe33aa53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x26501d9fa53f11ef85cfe89c25e08459, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x265048daa53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafa97f9fa53e11ef85cfe89c25e08459),
(0x265062d5a53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafabc8b8a53e11ef85cfe89c25e08459),
(0x26508ee3a53f11ef85cfe89c25e08459, 0xafaf94bca53e11ef85cfe89c25e08459, 0xafab58e9a53e11ef85cfe89c25e08459),
(0x2650acdda53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafab0fc1a53e11ef85cfe89c25e08459),
(0x2650c851a53f11ef85cfe89c25e08459, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa728e8a53e11ef85cfe89c25e08459),
(0x2650e378a53f11ef85cfe89c25e08459, 0xafaed8b8a53e11ef85cfe89c25e08459, 0xafa7fc9ea53e11ef85cfe89c25e08459),
(0x2650fe38a53f11ef85cfe89c25e08459, 0xafb06a61a53e11ef85cfe89c25e08459, 0xafa9fc3fa53e11ef85cfe89c25e08459),
(0x2651198ca53f11ef85cfe89c25e08459, 0xafb05098a53e11ef85cfe89c25e08459, 0xafa908f6a53e11ef85cfe89c25e08459),
(0x265134cea53f11ef85cfe89c25e08459, 0xafb00db8a53e11ef85cfe89c25e08459, 0xafaa5540a53e11ef85cfe89c25e08459),
(0x26514fa9a53f11ef85cfe89c25e08459, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa893e1a53e11ef85cfe89c25e08459),
(0x26516a95a53f11ef85cfe89c25e08459, 0xafb02693a53e11ef85cfe89c25e08459, 0xafa7e371a53e11ef85cfe89c25e08459),
(0x4e093100e7f145aea0286b7d6f16f97d, 0xafaf79f1a53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459),
(0x7088e3506bc74d46a00b1ef142ebc2ca, 0xafaf068ca53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459),
(0xe747436c886c407d8c6f6f7c4fc9f28e, 0xafaf5ee3a53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459),
(0xf5c29986643949a29cfa7fedb5dc684c, 0xafaeba53a53e11ef85cfe89c25e08459, 0xafa64618a53e11ef85cfe89c25e08459);

-- --------------------------------------------------------

--
-- Structure de la table `grades`
--

DROP TABLE IF EXISTS `grades`;
CREATE TABLE IF NOT EXISTS `grades` (
  `id` binary(16) NOT NULL,
  `score` double DEFAULT NULL,
  `assignment_id` binary(16) DEFAULT NULL,
  `enrollment_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK287de6bfabv9mj8r4flun01jd` (`assignment_id`),
  KEY `FK354vwjlgqmgi7tveernv2ylp6` (`enrollment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `grades`
--

INSERT INTO `grades` (`id`, `score`, `assignment_id`, `enrollment_id`) VALUES
(0x4b45fac3a53f11ef85cfe89c25e08459, 8.8, 0xafb332f5a53e11ef85cfe89c25e08459, 0x264beb08a53f11ef85cfe89c25e08459),
(0x4b4627e9a53f11ef85cfe89c25e08459, 5.2, 0xafb47e6da53e11ef85cfe89c25e08459, 0x2646228aa53f11ef85cfe89c25e08459),
(0x4b46482ba53f11ef85cfe89c25e08459, 16.1, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x263b9f2ea53f11ef85cfe89c25e08459),
(0x4b468772a53f11ef85cfe89c25e08459, 18.4, 0xafb10d95a53e11ef85cfe89c25e08459, 0x2643a31ca53f11ef85cfe89c25e08459),
(0x4b46d4bfa53f11ef85cfe89c25e08459, 1.8, 0xafb586e9a53e11ef85cfe89c25e08459, 0x264e0cffa53f11ef85cfe89c25e08459),
(0x4b46f2c4a53f11ef85cfe89c25e08459, 4.9, 0xafb28780a53e11ef85cfe89c25e08459, 0x2651198ca53f11ef85cfe89c25e08459),
(0x4b471e60a53f11ef85cfe89c25e08459, 4.7, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x263bbb3ba53f11ef85cfe89c25e08459),
(0x4b474eefa53f11ef85cfe89c25e08459, 11.3, 0xafb6d8d9a53e11ef85cfe89c25e08459, 0x26446dbaa53f11ef85cfe89c25e08459),
(0x4b476980a53f11ef85cfe89c25e08459, 11.2, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x263feef4a53f11ef85cfe89c25e08459),
(0x4b4799e8a53f11ef85cfe89c25e08459, 12.3, 0xafb16326a53e11ef85cfe89c25e08459, 0x2648141da53f11ef85cfe89c25e08459),
(0x4b47c559a53f11ef85cfe89c25e08459, 14.8, 0xafb67926a53e11ef85cfe89c25e08459, 0x2641788fa53f11ef85cfe89c25e08459),
(0x4b47df94a53f11ef85cfe89c25e08459, 1.3, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x264c8fb0a53f11ef85cfe89c25e08459),
(0x4b480d0ea53f11ef85cfe89c25e08459, 2.8, 0xafb498c9a53e11ef85cfe89c25e08459, 0x264c8fb0a53f11ef85cfe89c25e08459),
(0x4b4829dfa53f11ef85cfe89c25e08459, 0, 0xafb4fa7fa53e11ef85cfe89c25e08459, 0x264f4d5ea53f11ef85cfe89c25e08459),
(0x4b4845f1a53f11ef85cfe89c25e08459, 13.1, 0xafb96f81a53e11ef85cfe89c25e08459, 0x2643be3ba53f11ef85cfe89c25e08459),
(0x4b48622da53f11ef85cfe89c25e08459, 15.5, 0xafb332f5a53e11ef85cfe89c25e08459, 0x263ddf58a53f11ef85cfe89c25e08459),
(0x4b487dd8a53f11ef85cfe89c25e08459, 19.7, 0xafb90b38a53e11ef85cfe89c25e08459, 0x263a0452a53f11ef85cfe89c25e08459),
(0x4b489c21a53f11ef85cfe89c25e08459, 10.5, 0xafb16326a53e11ef85cfe89c25e08459, 0x26398690a53f11ef85cfe89c25e08459),
(0x4b48bad2a53f11ef85cfe89c25e08459, 18, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x2641f5c9a53f11ef85cfe89c25e08459),
(0x4b48d84ba53f11ef85cfe89c25e08459, 7.5, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x26467397a53f11ef85cfe89c25e08459),
(0x4b490525a53f11ef85cfe89c25e08459, 7.5, 0xafb857f7a53e11ef85cfe89c25e08459, 0x2641da99a53f11ef85cfe89c25e08459),
(0x4b49227aa53f11ef85cfe89c25e08459, 19.7, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x264cc44da53f11ef85cfe89c25e08459),
(0x4b493ef1a53f11ef85cfe89c25e08459, 7.9, 0xafb77980a53e11ef85cfe89c25e08459, 0x263d71c4a53f11ef85cfe89c25e08459),
(0x4b495b4ba53f11ef85cfe89c25e08459, 12.1, 0xafb332f5a53e11ef85cfe89c25e08459, 0x264259a2a53f11ef85cfe89c25e08459),
(0x4b497704a53f11ef85cfe89c25e08459, 0.8, 0xafb2a1c2a53e11ef85cfe89c25e08459, 0x2641788fa53f11ef85cfe89c25e08459),
(0x4b49a29aa53f11ef85cfe89c25e08459, 7.6, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x2642d13da53f11ef85cfe89c25e08459),
(0x4b49d394a53f11ef85cfe89c25e08459, 6.4, 0xafb253a9a53e11ef85cfe89c25e08459, 0x26493de1a53f11ef85cfe89c25e08459),
(0x4b49f0e0a53f11ef85cfe89c25e08459, 9.6, 0xafb98cdba53e11ef85cfe89c25e08459, 0x264cc44da53f11ef85cfe89c25e08459),
(0x4b4a0e10a53f11ef85cfe89c25e08459, 12, 0xafb90b38a53e11ef85cfe89c25e08459, 0x26493de1a53f11ef85cfe89c25e08459),
(0x4b4a2a90a53f11ef85cfe89c25e08459, 15.7, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x264497aea53f11ef85cfe89c25e08459),
(0x4b4a4639a53f11ef85cfe89c25e08459, 18, 0xafb65ed7a53e11ef85cfe89c25e08459, 0x264f9444a53f11ef85cfe89c25e08459),
(0x4b4a62c9a53f11ef85cfe89c25e08459, 3.5, 0xafb69351a53e11ef85cfe89c25e08459, 0x26499c5fa53f11ef85cfe89c25e08459),
(0x4b4a7e71a53f11ef85cfe89c25e08459, 5.7, 0xafb54337a53e11ef85cfe89c25e08459, 0x2643f386a53f11ef85cfe89c25e08459),
(0x4b4aa9e6a53f11ef85cfe89c25e08459, 6, 0xafb14901a53e11ef85cfe89c25e08459, 0x26516a95a53f11ef85cfe89c25e08459),
(0x4b4ac5c4a53f11ef85cfe89c25e08459, 12, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x26487369a53f11ef85cfe89c25e08459),
(0x4b4ae272a53f11ef85cfe89c25e08459, 9, 0xafb14901a53e11ef85cfe89c25e08459, 0x264859a0a53f11ef85cfe89c25e08459),
(0x4b4b445ea53f11ef85cfe89c25e08459, 8.9, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x263a99e4a53f11ef85cfe89c25e08459),
(0x4b4b6db8a53f11ef85cfe89c25e08459, 0.1, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x264b0301a53f11ef85cfe89c25e08459),
(0x4b4bbb68a53f11ef85cfe89c25e08459, 16.8, 0xafb927dba53e11ef85cfe89c25e08459, 0x26508ee3a53f11ef85cfe89c25e08459),
(0x4b4be59da53f11ef85cfe89c25e08459, 4.3, 0xafb7950ca53e11ef85cfe89c25e08459, 0x2639be36a53f11ef85cfe89c25e08459),
(0x4b4c0d84a53f11ef85cfe89c25e08459, 1.2, 0xafb17dafa53e11ef85cfe89c25e08459, 0x263cb303a53f11ef85cfe89c25e08459),
(0x4b4c4bc9a53f11ef85cfe89c25e08459, 3.9, 0xafb16326a53e11ef85cfe89c25e08459, 0x2645f059a53f11ef85cfe89c25e08459),
(0x4b4c76a0a53f11ef85cfe89c25e08459, 18.8, 0xafb8e114a53e11ef85cfe89c25e08459, 0x2640505ea53f11ef85cfe89c25e08459),
(0x4b4c9fe8a53f11ef85cfe89c25e08459, 13.3, 0xafb6d8d9a53e11ef85cfe89c25e08459, 0x264cdf23a53f11ef85cfe89c25e08459),
(0x4b4cc632a53f11ef85cfe89c25e08459, 6.7, 0xafb65ed7a53e11ef85cfe89c25e08459, 0x2640e115a53f11ef85cfe89c25e08459),
(0x4b4cf062a53f11ef85cfe89c25e08459, 2, 0xafb54337a53e11ef85cfe89c25e08459, 0x264f4d5ea53f11ef85cfe89c25e08459),
(0x4b4d1968a53f11ef85cfe89c25e08459, 1.2, 0xafb7b189a53e11ef85cfe89c25e08459, 0x2643882aa53f11ef85cfe89c25e08459),
(0x4b4d42b2a53f11ef85cfe89c25e08459, 0, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x263d8c92a53f11ef85cfe89c25e08459),
(0x4b4d6d2ea53f11ef85cfe89c25e08459, 14.6, 0xafb21feba53e11ef85cfe89c25e08459, 0x2649c700a53f11ef85cfe89c25e08459),
(0x4b4daa5ba53f11ef85cfe89c25e08459, 7, 0xafb21feba53e11ef85cfe89c25e08459, 0x264eedfca53f11ef85cfe89c25e08459),
(0x4b4dd342a53f11ef85cfe89c25e08459, 0.7, 0xafb10d95a53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b4dfb7aa53f11ef85cfe89c25e08459, 5.4, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x2647b2aaa53f11ef85cfe89c25e08459),
(0x4b4e20dca53f11ef85cfe89c25e08459, 14.6, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x26456f5ea53f11ef85cfe89c25e08459),
(0x4b4e4b98a53f11ef85cfe89c25e08459, 19.8, 0xafb90b38a53e11ef85cfe89c25e08459, 0x2640fb9fa53f11ef85cfe89c25e08459),
(0x4b4e7420a53f11ef85cfe89c25e08459, 2.4, 0xafb7b189a53e11ef85cfe89c25e08459, 0x263f2346a53f11ef85cfe89c25e08459),
(0x4b4e9cb9a53f11ef85cfe89c25e08459, 15.4, 0xafb4b3d1a53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b4eb811a53f11ef85cfe89c25e08459, 6.5, 0xafb19784a53e11ef85cfe89c25e08459, 0x26400872a53f11ef85cfe89c25e08459),
(0x4b4ed3f6a53f11ef85cfe89c25e08459, 7.3, 0xafb740f8a53e11ef85cfe89c25e08459, 0x264859a0a53f11ef85cfe89c25e08459),
(0x4b4ef042a53f11ef85cfe89c25e08459, 1.6, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x263edac0a53f11ef85cfe89c25e08459),
(0x4b4f0bb7a53f11ef85cfe89c25e08459, 18.8, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x2641f5c9a53f11ef85cfe89c25e08459),
(0x4b4f276ea53f11ef85cfe89c25e08459, 10, 0xafb8c5f9a53e11ef85cfe89c25e08459, 0x26501d9fa53f11ef85cfe89c25e08459),
(0x4b4f42c2a53f11ef85cfe89c25e08459, 16.4, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b4f6d5fa53f11ef85cfe89c25e08459, 15.4, 0xafb635e8a53e11ef85cfe89c25e08459, 0x263ade76a53f11ef85cfe89c25e08459),
(0x4b4f9a61a53f11ef85cfe89c25e08459, 7.2, 0xafb56d92a53e11ef85cfe89c25e08459, 0x2649c700a53f11ef85cfe89c25e08459),
(0x4b4fb5fda53f11ef85cfe89c25e08459, 3.3, 0xafb54337a53e11ef85cfe89c25e08459, 0x2651198ca53f11ef85cfe89c25e08459),
(0x4b4fd250a53f11ef85cfe89c25e08459, 12.6, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x264259a2a53f11ef85cfe89c25e08459),
(0x4b4ffe15a53f11ef85cfe89c25e08459, 13.8, 0xafb6f261a53e11ef85cfe89c25e08459, 0x263bbb3ba53f11ef85cfe89c25e08459),
(0x4b50193ca53f11ef85cfe89c25e08459, 2.8, 0xafb72711a53e11ef85cfe89c25e08459, 0x26516a95a53f11ef85cfe89c25e08459),
(0x4b5034b8a53f11ef85cfe89c25e08459, 12.3, 0xafb1b448a53e11ef85cfe89c25e08459, 0x2639d91ca53f11ef85cfe89c25e08459),
(0x4b505030a53f11ef85cfe89c25e08459, 10.4, 0xafb2f0daa53e11ef85cfe89c25e08459, 0x2647e958a53f11ef85cfe89c25e08459),
(0x4b507b5fa53f11ef85cfe89c25e08459, 2.6, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x263ea351a53f11ef85cfe89c25e08459),
(0x4b5096dba53f11ef85cfe89c25e08459, 0.8, 0xafb56d92a53e11ef85cfe89c25e08459, 0x2643a31ca53f11ef85cfe89c25e08459),
(0x4b50b20aa53f11ef85cfe89c25e08459, 5, 0xafb88fe2a53e11ef85cfe89c25e08459, 0x2645f059a53f11ef85cfe89c25e08459),
(0x4b50cd6ba53f11ef85cfe89c25e08459, 19.4, 0xafb94437a53e11ef85cfe89c25e08459, 0x264c2294a53f11ef85cfe89c25e08459),
(0x4b50e889a53f11ef85cfe89c25e08459, 13.5, 0xafb6f261a53e11ef85cfe89c25e08459, 0x264a9551a53f11ef85cfe89c25e08459),
(0x4b51053fa53f11ef85cfe89c25e08459, 6, 0xafb39148a53e11ef85cfe89c25e08459, 0x263d56d1a53f11ef85cfe89c25e08459),
(0x4b5121cba53f11ef85cfe89c25e08459, 19.7, 0xafb17dafa53e11ef85cfe89c25e08459, 0x26403244a53f11ef85cfe89c25e08459),
(0x4b5140daa53f11ef85cfe89c25e08459, 1.9, 0xafb7950ca53e11ef85cfe89c25e08459, 0x265062d5a53f11ef85cfe89c25e08459),
(0x4b515caca53f11ef85cfe89c25e08459, 16.8, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x26472d7fa53f11ef85cfe89c25e08459),
(0x4b517909a53f11ef85cfe89c25e08459, 14.3, 0xafb635e8a53e11ef85cfe89c25e08459, 0x264cdf23a53f11ef85cfe89c25e08459),
(0x4b519471a53f11ef85cfe89c25e08459, 4.7, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x263bd743a53f11ef85cfe89c25e08459),
(0x4b51b049a53f11ef85cfe89c25e08459, 8.1, 0xafb20453a53e11ef85cfe89c25e08459, 0x264dd6d8a53f11ef85cfe89c25e08459),
(0x4b51cb98a53f11ef85cfe89c25e08459, 11.1, 0xafb44932a53e11ef85cfe89c25e08459, 0x2639a243a53f11ef85cfe89c25e08459),
(0x4b51e7baa53f11ef85cfe89c25e08459, 2.5, 0xafb47e6da53e11ef85cfe89c25e08459, 0x263ddf58a53f11ef85cfe89c25e08459),
(0x4b52049ba53f11ef85cfe89c25e08459, 2.4, 0xafb414c4a53e11ef85cfe89c25e08459, 0x26430535a53f11ef85cfe89c25e08459),
(0x4b52208aa53f11ef85cfe89c25e08459, 14.9, 0xafb332f5a53e11ef85cfe89c25e08459, 0x2640e115a53f11ef85cfe89c25e08459),
(0x4b523ca5a53f11ef85cfe89c25e08459, 2.7, 0xafb332f5a53e11ef85cfe89c25e08459, 0x264116b9a53f11ef85cfe89c25e08459),
(0x4b5258b2a53f11ef85cfe89c25e08459, 3.8, 0xafb7e83ba53e11ef85cfe89c25e08459, 0x2640fb9fa53f11ef85cfe89c25e08459),
(0x4b52777ba53f11ef85cfe89c25e08459, 10.3, 0xafb19784a53e11ef85cfe89c25e08459, 0x2646b98ca53f11ef85cfe89c25e08459),
(0x4b529427a53f11ef85cfe89c25e08459, 1.7, 0xafb2a1c2a53e11ef85cfe89c25e08459, 0x2639a243a53f11ef85cfe89c25e08459),
(0x4b52bb80a53f11ef85cfe89c25e08459, 16.4, 0xafb47e6da53e11ef85cfe89c25e08459, 0x26430535a53f11ef85cfe89c25e08459),
(0x4b52e770a53f11ef85cfe89c25e08459, 8.1, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x2644ddbfa53f11ef85cfe89c25e08459),
(0x4b531791a53f11ef85cfe89c25e08459, 16, 0xafb10d95a53e11ef85cfe89c25e08459, 0x2649e21da53f11ef85cfe89c25e08459),
(0x4b53359ea53f11ef85cfe89c25e08459, 19.7, 0xafb8e114a53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b536098a53f11ef85cfe89c25e08459, 11.9, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b538bcca53f11ef85cfe89c25e08459, 20, 0xafb874e0a53e11ef85cfe89c25e08459, 0x264e2826a53f11ef85cfe89c25e08459),
(0x4b53a661a53f11ef85cfe89c25e08459, 0.4, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b53d0f7a53f11ef85cfe89c25e08459, 2.1, 0xafb20453a53e11ef85cfe89c25e08459, 0x26422de7a53f11ef85cfe89c25e08459),
(0x4b53eae9a53f11ef85cfe89c25e08459, 6.8, 0xafb527dda53e11ef85cfe89c25e08459, 0x2641937ea53f11ef85cfe89c25e08459),
(0x4b541545a53f11ef85cfe89c25e08459, 14.5, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x264116b9a53f11ef85cfe89c25e08459),
(0x4b54415ea53f11ef85cfe89c25e08459, 6.3, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x2644b200a53f11ef85cfe89c25e08459),
(0x4b545c52a53f11ef85cfe89c25e08459, 17.3, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x263a39eda53f11ef85cfe89c25e08459),
(0x4b547813a53f11ef85cfe89c25e08459, 16.3, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x2650036ba53f11ef85cfe89c25e08459),
(0x4b549443a53f11ef85cfe89c25e08459, 16.9, 0xafb3e0f0a53e11ef85cfe89c25e08459, 0x2648d010a53f11ef85cfe89c25e08459),
(0x4b54b03aa53f11ef85cfe89c25e08459, 12.8, 0xafb10d95a53e11ef85cfe89c25e08459, 0x2648141da53f11ef85cfe89c25e08459),
(0x4b54cbc6a53f11ef85cfe89c25e08459, 17.5, 0xafb319f2a53e11ef85cfe89c25e08459, 0x264beb08a53f11ef85cfe89c25e08459),
(0x4b54e6d4a53f11ef85cfe89c25e08459, 1, 0xafb44932a53e11ef85cfe89c25e08459, 0x264212ada53f11ef85cfe89c25e08459),
(0x4b550288a53f11ef85cfe89c25e08459, 5.4, 0xafb69351a53e11ef85cfe89c25e08459, 0x264259a2a53f11ef85cfe89c25e08459),
(0x4b551ed1a53f11ef85cfe89c25e08459, 3.2, 0xafb6f261a53e11ef85cfe89c25e08459, 0x2644287ca53f11ef85cfe89c25e08459),
(0x4b553abca53f11ef85cfe89c25e08459, 19.3, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x264859a0a53f11ef85cfe89c25e08459),
(0x4b555625a53f11ef85cfe89c25e08459, 7.1, 0xafb8c5f9a53e11ef85cfe89c25e08459, 0x263c0e81a53f11ef85cfe89c25e08459),
(0x4b557225a53f11ef85cfe89c25e08459, 7.1, 0xafb20453a53e11ef85cfe89c25e08459, 0x2639be36a53f11ef85cfe89c25e08459),
(0x4b558ebda53f11ef85cfe89c25e08459, 10.7, 0xafb414c4a53e11ef85cfe89c25e08459, 0x265062d5a53f11ef85cfe89c25e08459),
(0x4b55aa51a53f11ef85cfe89c25e08459, 16.9, 0xafb527dda53e11ef85cfe89c25e08459, 0x2639d91ca53f11ef85cfe89c25e08459),
(0x4b55c5faa53f11ef85cfe89c25e08459, 11, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x2643f386a53f11ef85cfe89c25e08459),
(0x4b55f114a53f11ef85cfe89c25e08459, 16.9, 0xafb586e9a53e11ef85cfe89c25e08459, 0x2646d52ca53f11ef85cfe89c25e08459),
(0x4b5623aca53f11ef85cfe89c25e08459, 10.6, 0xafb21feba53e11ef85cfe89c25e08459, 0x263dfad9a53f11ef85cfe89c25e08459),
(0x4b563e80a53f11ef85cfe89c25e08459, 6.4, 0xafb44932a53e11ef85cfe89c25e08459, 0x2643d8b6a53f11ef85cfe89c25e08459),
(0x4b565a72a53f11ef85cfe89c25e08459, 1.1, 0xafb67926a53e11ef85cfe89c25e08459, 0x26514fa9a53f11ef85cfe89c25e08459),
(0x4b56768ea53f11ef85cfe89c25e08459, 2.3, 0xafb498c9a53e11ef85cfe89c25e08459, 0x264859a0a53f11ef85cfe89c25e08459),
(0x4b56941ca53f11ef85cfe89c25e08459, 11.1, 0xafb19784a53e11ef85cfe89c25e08459, 0x263c0e81a53f11ef85cfe89c25e08459),
(0x4b56b34aa53f11ef85cfe89c25e08459, 15.6, 0xafb927dba53e11ef85cfe89c25e08459, 0x263ab430a53f11ef85cfe89c25e08459),
(0x4b56d047a53f11ef85cfe89c25e08459, 16.2, 0xafb16326a53e11ef85cfe89c25e08459, 0x26385e9ba53f11ef85cfe89c25e08459),
(0x4b56fdf4a53f11ef85cfe89c25e08459, 10.9, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x2650e378a53f11ef85cfe89c25e08459),
(0x4b572a5aa53f11ef85cfe89c25e08459, 7.7, 0xafb6f261a53e11ef85cfe89c25e08459, 0x263d71c4a53f11ef85cfe89c25e08459),
(0x4b574513a53f11ef85cfe89c25e08459, 17, 0xafb527dda53e11ef85cfe89c25e08459, 0x264da14ea53f11ef85cfe89c25e08459),
(0x4b577639a53f11ef85cfe89c25e08459, 13.5, 0xafb3e0f0a53e11ef85cfe89c25e08459, 0x263d1f12a53f11ef85cfe89c25e08459),
(0x4b57a2e5a53f11ef85cfe89c25e08459, 8.8, 0xafb56d92a53e11ef85cfe89c25e08459, 0x264ad8c9a53f11ef85cfe89c25e08459),
(0x4b57bd3ea53f11ef85cfe89c25e08459, 3.5, 0xafb740f8a53e11ef85cfe89c25e08459, 0x263b9f2ea53f11ef85cfe89c25e08459),
(0x4b57e9bda53f11ef85cfe89c25e08459, 14.4, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x26482e99a53f11ef85cfe89c25e08459),
(0x4b5806e2a53f11ef85cfe89c25e08459, 3.2, 0xafb54337a53e11ef85cfe89c25e08459, 0x2644287ca53f11ef85cfe89c25e08459),
(0x4b583256a53f11ef85cfe89c25e08459, 2.8, 0xafb72711a53e11ef85cfe89c25e08459, 0x26482e99a53f11ef85cfe89c25e08459),
(0x4b584e99a53f11ef85cfe89c25e08459, 7.3, 0xafb8124ba53e11ef85cfe89c25e08459, 0x2640c587a53f11ef85cfe89c25e08459),
(0x4b586a13a53f11ef85cfe89c25e08459, 16.8, 0xafb414c4a53e11ef85cfe89c25e08459, 0x263d1f12a53f11ef85cfe89c25e08459),
(0x4b5885bba53f11ef85cfe89c25e08459, 14.5, 0xafb16326a53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b58a19ea53f11ef85cfe89c25e08459, 8.8, 0xafb98cdba53e11ef85cfe89c25e08459, 0x263da76ea53f11ef85cfe89c25e08459),
(0x4b58bd2aa53f11ef85cfe89c25e08459, 7.6, 0xafb72711a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b58d890a53f11ef85cfe89c25e08459, 18.6, 0xafb6d8d9a53e11ef85cfe89c25e08459, 0x2649e21da53f11ef85cfe89c25e08459),
(0x4b58f47ca53f11ef85cfe89c25e08459, 4.1, 0xafb498c9a53e11ef85cfe89c25e08459, 0x26516a95a53f11ef85cfe89c25e08459),
(0x4b59205aa53f11ef85cfe89c25e08459, 16.4, 0xafb5f404a53e11ef85cfe89c25e08459, 0x263da76ea53f11ef85cfe89c25e08459),
(0x4b594eaaa53f11ef85cfe89c25e08459, 2.9, 0xafb67926a53e11ef85cfe89c25e08459, 0x264f4d5ea53f11ef85cfe89c25e08459),
(0x4b596afea53f11ef85cfe89c25e08459, 6.4, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b598708a53f11ef85cfe89c25e08459, 14.6, 0xafb90b38a53e11ef85cfe89c25e08459, 0x263a5508a53f11ef85cfe89c25e08459),
(0x4b59a420a53f11ef85cfe89c25e08459, 8.5, 0xafb4fa7fa53e11ef85cfe89c25e08459, 0x26488cb5a53f11ef85cfe89c25e08459),
(0x4b59bf68a53f11ef85cfe89c25e08459, 17.6, 0xafb56d92a53e11ef85cfe89c25e08459, 0x264abf54a53f11ef85cfe89c25e08459),
(0x4b59dad8a53f11ef85cfe89c25e08459, 19.1, 0xafb77980a53e11ef85cfe89c25e08459, 0x2643516ba53f11ef85cfe89c25e08459),
(0x4b59f629a53f11ef85cfe89c25e08459, 6.3, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x264968b8a53f11ef85cfe89c25e08459),
(0x4b5a2111a53f11ef85cfe89c25e08459, 18.4, 0xafb98cdba53e11ef85cfe89c25e08459, 0x263ef84ea53f11ef85cfe89c25e08459),
(0x4b5a4bbfa53f11ef85cfe89c25e08459, 18.6, 0xafb4fa7fa53e11ef85cfe89c25e08459, 0x263909f9a53f11ef85cfe89c25e08459),
(0x4b5a6608a53f11ef85cfe89c25e08459, 5.8, 0xafb3fabaa53e11ef85cfe89c25e08459, 0x263a1e85a53f11ef85cfe89c25e08459),
(0x4b5a81b4a53f11ef85cfe89c25e08459, 14.6, 0xafb65ed7a53e11ef85cfe89c25e08459, 0x2649c700a53f11ef85cfe89c25e08459),
(0x4b5a9dada53f11ef85cfe89c25e08459, 10.9, 0xafb498c9a53e11ef85cfe89c25e08459, 0x263ab430a53f11ef85cfe89c25e08459),
(0x4b5ab900a53f11ef85cfe89c25e08459, 17.7, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x264f6977a53f11ef85cfe89c25e08459),
(0x4b5ae4dda53f11ef85cfe89c25e08459, 10.2, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b5affb8a53f11ef85cfe89c25e08459, 7.7, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x2650acdda53f11ef85cfe89c25e08459),
(0x4b5b2ad3a53f11ef85cfe89c25e08459, 18.5, 0xafb14901a53e11ef85cfe89c25e08459, 0x263f66cea53f11ef85cfe89c25e08459),
(0x4b5b4685a53f11ef85cfe89c25e08459, 7.3, 0xafb39148a53e11ef85cfe89c25e08459, 0x263c29fea53f11ef85cfe89c25e08459),
(0x4b5b61f1a53f11ef85cfe89c25e08459, 17.8, 0xafb874e0a53e11ef85cfe89c25e08459, 0x2643d8b6a53f11ef85cfe89c25e08459),
(0x4b5b7de8a53f11ef85cfe89c25e08459, 6.7, 0xafb56d92a53e11ef85cfe89c25e08459, 0x264f6977a53f11ef85cfe89c25e08459),
(0x4b5b9a85a53f11ef85cfe89c25e08459, 3.5, 0xafb72711a53e11ef85cfe89c25e08459, 0x2644fa0fa53f11ef85cfe89c25e08459),
(0x4b5bb66da53f11ef85cfe89c25e08459, 10.1, 0xafb44932a53e11ef85cfe89c25e08459, 0x2647cec0a53f11ef85cfe89c25e08459),
(0x4b5bd27ba53f11ef85cfe89c25e08459, 13.5, 0xafb67926a53e11ef85cfe89c25e08459, 0x264d85dea53f11ef85cfe89c25e08459),
(0x4b5beb43a53f11ef85cfe89c25e08459, 18, 0xafb740f8a53e11ef85cfe89c25e08459, 0x263c4515a53f11ef85cfe89c25e08459),
(0x4b5c06e5a53f11ef85cfe89c25e08459, 15.1, 0xafb20453a53e11ef85cfe89c25e08459, 0x2645d3a3a53f11ef85cfe89c25e08459),
(0x4b5c354aa53f11ef85cfe89c25e08459, 12.7, 0xafb19784a53e11ef85cfe89c25e08459, 0x26456f5ea53f11ef85cfe89c25e08459),
(0x4b5c4fbba53f11ef85cfe89c25e08459, 19.9, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x263dc28da53f11ef85cfe89c25e08459),
(0x4b5c6b34a53f11ef85cfe89c25e08459, 10.1, 0xafb740f8a53e11ef85cfe89c25e08459, 0x2640aa16a53f11ef85cfe89c25e08459),
(0x4b5c86aba53f11ef85cfe89c25e08459, 12.1, 0xafb94437a53e11ef85cfe89c25e08459, 0x263ebf13a53f11ef85cfe89c25e08459),
(0x4b5ca270a53f11ef85cfe89c25e08459, 11.4, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x264e4639a53f11ef85cfe89c25e08459),
(0x4b5cbee3a53f11ef85cfe89c25e08459, 14.4, 0xafb463d8a53e11ef85cfe89c25e08459, 0x263c5ff4a53f11ef85cfe89c25e08459),
(0x4b5cda2ba53f11ef85cfe89c25e08459, 15.7, 0xafb5b144a53e11ef85cfe89c25e08459, 0x2647cec0a53f11ef85cfe89c25e08459),
(0x4b5cf5aaa53f11ef85cfe89c25e08459, 17.6, 0xafb4b3d1a53e11ef85cfe89c25e08459, 0x264eb79ea53f11ef85cfe89c25e08459),
(0x4b5d1154a53f11ef85cfe89c25e08459, 8.5, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x26465799a53f11ef85cfe89c25e08459),
(0x4b5d2d81a53f11ef85cfe89c25e08459, 11.7, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x263e16a0a53f11ef85cfe89c25e08459),
(0x4b5d497ca53f11ef85cfe89c25e08459, 12.1, 0xafb253a9a53e11ef85cfe89c25e08459, 0x263e16a0a53f11ef85cfe89c25e08459),
(0x4b5d65b6a53f11ef85cfe89c25e08459, 0.6, 0xafb28780a53e11ef85cfe89c25e08459, 0x264e2826a53f11ef85cfe89c25e08459),
(0x4b5d82c7a53f11ef85cfe89c25e08459, 2.2, 0xafb70d23a53e11ef85cfe89c25e08459, 0x263d3b79a53f11ef85cfe89c25e08459),
(0x4b5d9ec9a53f11ef85cfe89c25e08459, 7.7, 0xafb98cdba53e11ef85cfe89c25e08459, 0x2640c587a53f11ef85cfe89c25e08459),
(0x4b5db9ffa53f11ef85cfe89c25e08459, 16.7, 0xafb8e114a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b5dd58ea53f11ef85cfe89c25e08459, 14.6, 0xafb69351a53e11ef85cfe89c25e08459, 0x2645f059a53f11ef85cfe89c25e08459),
(0x4b5df266a53f11ef85cfe89c25e08459, 15.7, 0xafb239efa53e11ef85cfe89c25e08459, 0x264cdf23a53f11ef85cfe89c25e08459),
(0x4b5e0dc4a53f11ef85cfe89c25e08459, 8.8, 0xafb857f7a53e11ef85cfe89c25e08459, 0x263b22f8a53f11ef85cfe89c25e08459),
(0x4b5e39cca53f11ef85cfe89c25e08459, 3.3, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x263dfad9a53f11ef85cfe89c25e08459),
(0x4b5e5414a53f11ef85cfe89c25e08459, 2.1, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x264116b9a53f11ef85cfe89c25e08459),
(0x4b5e7e69a53f11ef85cfe89c25e08459, 13.4, 0xafb96f81a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b5ea8b6a53f11ef85cfe89c25e08459, 15.3, 0xafb44932a53e11ef85cfe89c25e08459, 0x263b9f2ea53f11ef85cfe89c25e08459),
(0x4b5ec41ca53f11ef85cfe89c25e08459, 15.6, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b5ef326a53f11ef85cfe89c25e08459, 7.2, 0xafb98cdba53e11ef85cfe89c25e08459, 0x2645351fa53f11ef85cfe89c25e08459),
(0x4b5f1036a53f11ef85cfe89c25e08459, 3.3, 0xafb16326a53e11ef85cfe89c25e08459, 0x2644287ca53f11ef85cfe89c25e08459),
(0x4b5f2b86a53f11ef85cfe89c25e08459, 0.5, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x26414cfba53f11ef85cfe89c25e08459),
(0x4b5f4a26a53f11ef85cfe89c25e08459, 13, 0xafb14901a53e11ef85cfe89c25e08459, 0x26398690a53f11ef85cfe89c25e08459),
(0x4b5f6588a53f11ef85cfe89c25e08459, 4.6, 0xafb8e114a53e11ef85cfe89c25e08459, 0x2648fa5aa53f11ef85cfe89c25e08459),
(0x4b5f90c4a53f11ef85cfe89c25e08459, 18.7, 0xafb96f81a53e11ef85cfe89c25e08459, 0x264e2826a53f11ef85cfe89c25e08459),
(0x4b5fbbffa53f11ef85cfe89c25e08459, 4.6, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x263c0e81a53f11ef85cfe89c25e08459),
(0x4b5fd6bba53f11ef85cfe89c25e08459, 6.8, 0xafb874e0a53e11ef85cfe89c25e08459, 0x26403244a53f11ef85cfe89c25e08459),
(0x4b5ff248a53f11ef85cfe89c25e08459, 10, 0xafb5f404a53e11ef85cfe89c25e08459, 0x265062d5a53f11ef85cfe89c25e08459),
(0x4b600f87a53f11ef85cfe89c25e08459, 8.2, 0xafb463d8a53e11ef85cfe89c25e08459, 0x26436d2da53f11ef85cfe89c25e08459),
(0x4b602bc4a53f11ef85cfe89c25e08459, 0, 0xafb7b189a53e11ef85cfe89c25e08459, 0x263b4e8ea53f11ef85cfe89c25e08459),
(0x4b6047a7a53f11ef85cfe89c25e08459, 1.3, 0xafb14901a53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b6063bda53f11ef85cfe89c25e08459, 4.2, 0xafb21feba53e11ef85cfe89c25e08459, 0x2640c587a53f11ef85cfe89c25e08459),
(0x4b6082b9a53f11ef85cfe89c25e08459, 15.5, 0xafb35c05a53e11ef85cfe89c25e08459, 0x264779c9a53f11ef85cfe89c25e08459),
(0x4b609ff0a53f11ef85cfe89c25e08459, 13.1, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b60eb07a53f11ef85cfe89c25e08459, 19.3, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x26487369a53f11ef85cfe89c25e08459),
(0x4b610841a53f11ef85cfe89c25e08459, 4.5, 0xafb39148a53e11ef85cfe89c25e08459, 0x26470136a53f11ef85cfe89c25e08459),
(0x4b6135dba53f11ef85cfe89c25e08459, 4.3, 0xafb28780a53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b615328a53f11ef85cfe89c25e08459, 9.1, 0xafb56d92a53e11ef85cfe89c25e08459, 0x264b1cfba53f11ef85cfe89c25e08459),
(0x4b616f2ca53f11ef85cfe89c25e08459, 3.2, 0xafb5b144a53e11ef85cfe89c25e08459, 0x2649243ca53f11ef85cfe89c25e08459),
(0x4b619a7ea53f11ef85cfe89c25e08459, 10, 0xafb927dba53e11ef85cfe89c25e08459, 0x26455260a53f11ef85cfe89c25e08459),
(0x4b61c512a53f11ef85cfe89c25e08459, 7.1, 0xafb70d23a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b61dfa4a53f11ef85cfe89c25e08459, 7.2, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x2641937ea53f11ef85cfe89c25e08459),
(0x4b61fb41a53f11ef85cfe89c25e08459, 19.6, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x2644b200a53f11ef85cfe89c25e08459),
(0x4b621810a53f11ef85cfe89c25e08459, 1.8, 0xafb253a9a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b6234a5a53f11ef85cfe89c25e08459, 7.5, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b6250f9a53f11ef85cfe89c25e08459, 19.7, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x2650fe38a53f11ef85cfe89c25e08459),
(0x4b626ccda53f11ef85cfe89c25e08459, 16.4, 0xafb96f81a53e11ef85cfe89c25e08459, 0x263e5d9ba53f11ef85cfe89c25e08459),
(0x4b62899da53f11ef85cfe89c25e08459, 1.4, 0xafb47e6da53e11ef85cfe89c25e08459, 0x263bd743a53f11ef85cfe89c25e08459),
(0x4b62a778a53f11ef85cfe89c25e08459, 10.3, 0xafb21feba53e11ef85cfe89c25e08459, 0x263b4e8ea53f11ef85cfe89c25e08459),
(0x4b62c3f4a53f11ef85cfe89c25e08459, 11.1, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x26499c5fa53f11ef85cfe89c25e08459),
(0x4b62dfd6a53f11ef85cfe89c25e08459, 7.6, 0xafb3769ea53e11ef85cfe89c25e08459, 0x263d8c92a53f11ef85cfe89c25e08459),
(0x4b62fbb5a53f11ef85cfe89c25e08459, 6.5, 0xafb4fa7fa53e11ef85cfe89c25e08459, 0x2647b2aaa53f11ef85cfe89c25e08459),
(0x4b6317f4a53f11ef85cfe89c25e08459, 18.7, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x263d56d1a53f11ef85cfe89c25e08459),
(0x4b633458a53f11ef85cfe89c25e08459, 12.1, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x26430535a53f11ef85cfe89c25e08459),
(0x4b634faaa53f11ef85cfe89c25e08459, 0, 0xafb2a1c2a53e11ef85cfe89c25e08459, 0x263d56d1a53f11ef85cfe89c25e08459),
(0x4b636b82a53f11ef85cfe89c25e08459, 16.7, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x2649e21da53f11ef85cfe89c25e08459),
(0x4b6387cea53f11ef85cfe89c25e08459, 17.3, 0xafb239efa53e11ef85cfe89c25e08459, 0x263cce52a53f11ef85cfe89c25e08459),
(0x4b63a37fa53f11ef85cfe89c25e08459, 15.5, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x26408edaa53f11ef85cfe89c25e08459),
(0x4b63bf34a53f11ef85cfe89c25e08459, 1.1, 0xafb67926a53e11ef85cfe89c25e08459, 0x263da76ea53f11ef85cfe89c25e08459),
(0x4b63da7ba53f11ef85cfe89c25e08459, 10.9, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x263bbb3ba53f11ef85cfe89c25e08459),
(0x4b63f5d6a53f11ef85cfe89c25e08459, 3.3, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x264b892ea53f11ef85cfe89c25e08459),
(0x4b6411d0a53f11ef85cfe89c25e08459, 0.7, 0xafb635e8a53e11ef85cfe89c25e08459, 0x2651198ca53f11ef85cfe89c25e08459),
(0x4b642cfaa53f11ef85cfe89c25e08459, 8.1, 0xafb17dafa53e11ef85cfe89c25e08459, 0x2647b2aaa53f11ef85cfe89c25e08459),
(0x4b644934a53f11ef85cfe89c25e08459, 3.1, 0xafb7e83ba53e11ef85cfe89c25e08459, 0x2646228aa53f11ef85cfe89c25e08459),
(0x4b646542a53f11ef85cfe89c25e08459, 13.6, 0xafb253a9a53e11ef85cfe89c25e08459, 0x2644fa0fa53f11ef85cfe89c25e08459),
(0x4b648085a53f11ef85cfe89c25e08459, 17.3, 0xafb10d95a53e11ef85cfe89c25e08459, 0x26436d2da53f11ef85cfe89c25e08459),
(0x4b649d15a53f11ef85cfe89c25e08459, 11.4, 0xafb3ac4da53e11ef85cfe89c25e08459, 0x26408edaa53f11ef85cfe89c25e08459),
(0x4b64b84ba53f11ef85cfe89c25e08459, 16.9, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x26400872a53f11ef85cfe89c25e08459),
(0x4b64d4eca53f11ef85cfe89c25e08459, 9.9, 0xafb69351a53e11ef85cfe89c25e08459, 0x264b1cfba53f11ef85cfe89c25e08459),
(0x4b64f223a53f11ef85cfe89c25e08459, 5.3, 0xafb857f7a53e11ef85cfe89c25e08459, 0x26414cfba53f11ef85cfe89c25e08459),
(0x4b650eeea53f11ef85cfe89c25e08459, 18.7, 0xafb44932a53e11ef85cfe89c25e08459, 0x264fae73a53f11ef85cfe89c25e08459),
(0x4b653a34a53f11ef85cfe89c25e08459, 18.2, 0xafb8c5f9a53e11ef85cfe89c25e08459, 0x265048daa53f11ef85cfe89c25e08459),
(0x4b65556ea53f11ef85cfe89c25e08459, 1.3, 0xafb7b189a53e11ef85cfe89c25e08459, 0x264859a0a53f11ef85cfe89c25e08459),
(0x4b6570e4a53f11ef85cfe89c25e08459, 3.9, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x263da76ea53f11ef85cfe89c25e08459),
(0x4b658c97a53f11ef85cfe89c25e08459, 11.4, 0xafb39148a53e11ef85cfe89c25e08459, 0x263b4e8ea53f11ef85cfe89c25e08459),
(0x4b65a8aea53f11ef85cfe89c25e08459, 2.3, 0xafb7e83ba53e11ef85cfe89c25e08459, 0x263d56d1a53f11ef85cfe89c25e08459),
(0x4b65c429a53f11ef85cfe89c25e08459, 9, 0xafb319f2a53e11ef85cfe89c25e08459, 0x263edac0a53f11ef85cfe89c25e08459),
(0x4b65e0bea53f11ef85cfe89c25e08459, 16.9, 0xafb65ed7a53e11ef85cfe89c25e08459, 0x263f66cea53f11ef85cfe89c25e08459),
(0x4b65fbe0a53f11ef85cfe89c25e08459, 8, 0xafb96f81a53e11ef85cfe89c25e08459, 0x2645d3a3a53f11ef85cfe89c25e08459),
(0x4b661743a53f11ef85cfe89c25e08459, 18.4, 0xafb3fabaa53e11ef85cfe89c25e08459, 0x2646b98ca53f11ef85cfe89c25e08459),
(0x4b66330ea53f11ef85cfe89c25e08459, 18.1, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x2641788fa53f11ef85cfe89c25e08459),
(0x4b664f63a53f11ef85cfe89c25e08459, 5.6, 0xafb498c9a53e11ef85cfe89c25e08459, 0x263da76ea53f11ef85cfe89c25e08459),
(0x4b666afda53f11ef85cfe89c25e08459, 16.6, 0xafb3769ea53e11ef85cfe89c25e08459, 0x2641937ea53f11ef85cfe89c25e08459),
(0x4b668663a53f11ef85cfe89c25e08459, 7.4, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x263e16a0a53f11ef85cfe89c25e08459),
(0x4b66a1caa53f11ef85cfe89c25e08459, 4.8, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b66ce42a53f11ef85cfe89c25e08459, 14.8, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x2644ddbfa53f11ef85cfe89c25e08459),
(0x4b66ea95a53f11ef85cfe89c25e08459, 0.5, 0xafb3fabaa53e11ef85cfe89c25e08459, 0x264c3e42a53f11ef85cfe89c25e08459),
(0x4b670644a53f11ef85cfe89c25e08459, 15.4, 0xafb3e0f0a53e11ef85cfe89c25e08459, 0x2642a132a53f11ef85cfe89c25e08459),
(0x4b6723f6a53f11ef85cfe89c25e08459, 11.3, 0xafb19784a53e11ef85cfe89c25e08459, 0x264b6e42a53f11ef85cfe89c25e08459),
(0x4b674476a53f11ef85cfe89c25e08459, 14.7, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x264f9444a53f11ef85cfe89c25e08459),
(0x4b67712ca53f11ef85cfe89c25e08459, 5.9, 0xafb75cfea53e11ef85cfe89c25e08459, 0x2641bfa0a53f11ef85cfe89c25e08459),
(0x4b678ce1a53f11ef85cfe89c25e08459, 7.9, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x264e2826a53f11ef85cfe89c25e08459),
(0x4b67aa5aa53f11ef85cfe89c25e08459, 7.7, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x264ad8c9a53f11ef85cfe89c25e08459),
(0x4b67c64ea53f11ef85cfe89c25e08459, 12.4, 0xafb319f2a53e11ef85cfe89c25e08459, 0x2648fa5aa53f11ef85cfe89c25e08459),
(0x4b67e31aa53f11ef85cfe89c25e08459, 14.9, 0xafb6f261a53e11ef85cfe89c25e08459, 0x264ad8c9a53f11ef85cfe89c25e08459),
(0x4b67ffc0a53f11ef85cfe89c25e08459, 2.7, 0xafb10d95a53e11ef85cfe89c25e08459, 0x2641f5c9a53f11ef85cfe89c25e08459),
(0x4b681ca4a53f11ef85cfe89c25e08459, 3.9, 0xafb75cfea53e11ef85cfe89c25e08459, 0x26436d2da53f11ef85cfe89c25e08459),
(0x4b68383ba53f11ef85cfe89c25e08459, 16, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x264b0301a53f11ef85cfe89c25e08459),
(0x4b68548aa53f11ef85cfe89c25e08459, 6.3, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x2648b678a53f11ef85cfe89c25e08459),
(0x4b68717ba53f11ef85cfe89c25e08459, 11.2, 0xafb253a9a53e11ef85cfe89c25e08459, 0x263d8c92a53f11ef85cfe89c25e08459),
(0x4b688eb1a53f11ef85cfe89c25e08459, 10.2, 0xafb7950ca53e11ef85cfe89c25e08459, 0x263c96b9a53f11ef85cfe89c25e08459),
(0x4b68ab86a53f11ef85cfe89c25e08459, 1.4, 0xafb3769ea53e11ef85cfe89c25e08459, 0x26430535a53f11ef85cfe89c25e08459),
(0x4b68c874a53f11ef85cfe89c25e08459, 12, 0xafb7e83ba53e11ef85cfe89c25e08459, 0x2641bfa0a53f11ef85cfe89c25e08459),
(0x4b68e4c6a53f11ef85cfe89c25e08459, 0.6, 0xafb586e9a53e11ef85cfe89c25e08459, 0x264e2826a53f11ef85cfe89c25e08459),
(0x4b690122a53f11ef85cfe89c25e08459, 6.5, 0xafb7950ca53e11ef85cfe89c25e08459, 0x263edac0a53f11ef85cfe89c25e08459),
(0x4b691c6aa53f11ef85cfe89c25e08459, 16.3, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x263c29fea53f11ef85cfe89c25e08459),
(0x4b6938e4a53f11ef85cfe89c25e08459, 15.8, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x26470136a53f11ef85cfe89c25e08459),
(0x4b695483a53f11ef85cfe89c25e08459, 5.4, 0xafb56d92a53e11ef85cfe89c25e08459, 0x26430535a53f11ef85cfe89c25e08459),
(0x4b69824aa53f11ef85cfe89c25e08459, 17.2, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x264dbc5ba53f11ef85cfe89c25e08459),
(0x4b69b254a53f11ef85cfe89c25e08459, 3.4, 0xafb90b38a53e11ef85cfe89c25e08459, 0x263c5ff4a53f11ef85cfe89c25e08459),
(0x4b69cf64a53f11ef85cfe89c25e08459, 7.4, 0xafb3769ea53e11ef85cfe89c25e08459, 0x263b68e2a53f11ef85cfe89c25e08459),
(0x4b69eac5a53f11ef85cfe89c25e08459, 14.4, 0xafb17dafa53e11ef85cfe89c25e08459, 0x263ab430a53f11ef85cfe89c25e08459),
(0x4b6a051aa53f11ef85cfe89c25e08459, 7.1, 0xafb414c4a53e11ef85cfe89c25e08459, 0x26516a95a53f11ef85cfe89c25e08459),
(0x4b6a2fd2a53f11ef85cfe89c25e08459, 0.5, 0xafb28780a53e11ef85cfe89c25e08459, 0x264e0cffa53f11ef85cfe89c25e08459),
(0x4b6a6001a53f11ef85cfe89c25e08459, 7.2, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x263e16a0a53f11ef85cfe89c25e08459),
(0x4b6a7ab8a53f11ef85cfe89c25e08459, 2.2, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x264335efa53f11ef85cfe89c25e08459),
(0x4b6a9693a53f11ef85cfe89c25e08459, 12.5, 0xafb2a1c2a53e11ef85cfe89c25e08459, 0x264fd83ca53f11ef85cfe89c25e08459),
(0x4b6ab2aba53f11ef85cfe89c25e08459, 11.7, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x264ad8c9a53f11ef85cfe89c25e08459),
(0x4b6ace33a53f11ef85cfe89c25e08459, 1.3, 0xafb498c9a53e11ef85cfe89c25e08459, 0x26482e99a53f11ef85cfe89c25e08459),
(0x4b6afb9ba53f11ef85cfe89c25e08459, 15.2, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x26414cfba53f11ef85cfe89c25e08459),
(0x4b6b15e2a53f11ef85cfe89c25e08459, 10.5, 0xafb498c9a53e11ef85cfe89c25e08459, 0x264f9444a53f11ef85cfe89c25e08459),
(0x4b6b4125a53f11ef85cfe89c25e08459, 19.7, 0xafb88fe2a53e11ef85cfe89c25e08459, 0x264dbc5ba53f11ef85cfe89c25e08459),
(0x4b6b5d48a53f11ef85cfe89c25e08459, 6, 0xafb8124ba53e11ef85cfe89c25e08459, 0x263a5508a53f11ef85cfe89c25e08459),
(0x4b6b7950a53f11ef85cfe89c25e08459, 7.5, 0xafb7e83ba53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b6b950fa53f11ef85cfe89c25e08459, 11.8, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x264c59bca53f11ef85cfe89c25e08459),
(0x4b6bc379a53f11ef85cfe89c25e08459, 19.4, 0xafb740f8a53e11ef85cfe89c25e08459, 0x264d0a11a53f11ef85cfe89c25e08459),
(0x4b6bdfb3a53f11ef85cfe89c25e08459, 18.7, 0xafb635e8a53e11ef85cfe89c25e08459, 0x2650036ba53f11ef85cfe89c25e08459),
(0x4b6c0acea53f11ef85cfe89c25e08459, 18.2, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x264cc44da53f11ef85cfe89c25e08459),
(0x4b6c2590a53f11ef85cfe89c25e08459, 9.1, 0xafb3e0f0a53e11ef85cfe89c25e08459, 0x264eb79ea53f11ef85cfe89c25e08459),
(0x4b6c5054a53f11ef85cfe89c25e08459, 4.5, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x26493de1a53f11ef85cfe89c25e08459),
(0x4b6c6c82a53f11ef85cfe89c25e08459, 13.4, 0xafb75cfea53e11ef85cfe89c25e08459, 0x263bd743a53f11ef85cfe89c25e08459),
(0x4b6c888fa53f11ef85cfe89c25e08459, 13.3, 0xafb3fabaa53e11ef85cfe89c25e08459, 0x263e5d9ba53f11ef85cfe89c25e08459),
(0x4b6cb3a1a53f11ef85cfe89c25e08459, 7.4, 0xafb70d23a53e11ef85cfe89c25e08459, 0x264c2294a53f11ef85cfe89c25e08459),
(0x4b6ccf9aa53f11ef85cfe89c25e08459, 18.9, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b6d052fa53f11ef85cfe89c25e08459, 3.9, 0xafb20453a53e11ef85cfe89c25e08459, 0x264eedfca53f11ef85cfe89c25e08459),
(0x4b6d303fa53f11ef85cfe89c25e08459, 6.9, 0xafb69351a53e11ef85cfe89c25e08459, 0x263da76ea53f11ef85cfe89c25e08459),
(0x4b6d4d50a53f11ef85cfe89c25e08459, 11.8, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x264452c3a53f11ef85cfe89c25e08459),
(0x4b6d697fa53f11ef85cfe89c25e08459, 11.1, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x2643d8b6a53f11ef85cfe89c25e08459),
(0x4b6d84dca53f11ef85cfe89c25e08459, 12.7, 0xafb16326a53e11ef85cfe89c25e08459, 0x263edac0a53f11ef85cfe89c25e08459),
(0x4b6da10aa53f11ef85cfe89c25e08459, 10.5, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x263e16a0a53f11ef85cfe89c25e08459),
(0x4b6dbceea53f11ef85cfe89c25e08459, 16.4, 0xafb17dafa53e11ef85cfe89c25e08459, 0x2646228aa53f11ef85cfe89c25e08459),
(0x4b6de7a2a53f11ef85cfe89c25e08459, 8.2, 0xafb740f8a53e11ef85cfe89c25e08459, 0x26470136a53f11ef85cfe89c25e08459),
(0x4b6e0196a53f11ef85cfe89c25e08459, 4.2, 0xafb6f261a53e11ef85cfe89c25e08459, 0x26472d7fa53f11ef85cfe89c25e08459),
(0x4b6e313aa53f11ef85cfe89c25e08459, 5.6, 0xafb88fe2a53e11ef85cfe89c25e08459, 0x2640fb9fa53f11ef85cfe89c25e08459),
(0x4b6e5e79a53f11ef85cfe89c25e08459, 2.9, 0xafb874e0a53e11ef85cfe89c25e08459, 0x263d56d1a53f11ef85cfe89c25e08459),
(0x4b6e794ca53f11ef85cfe89c25e08459, 3.8, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x264dd6d8a53f11ef85cfe89c25e08459),
(0x4b6ea9e2a53f11ef85cfe89c25e08459, 1.4, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x263bbb3ba53f11ef85cfe89c25e08459),
(0x4b6ed4e6a53f11ef85cfe89c25e08459, 10.6, 0xafb14901a53e11ef85cfe89c25e08459, 0x26456f5ea53f11ef85cfe89c25e08459),
(0x4b6ef0c5a53f11ef85cfe89c25e08459, 12.3, 0xafb16326a53e11ef85cfe89c25e08459, 0x2643a31ca53f11ef85cfe89c25e08459),
(0x4b6f1c02a53f11ef85cfe89c25e08459, 1, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x263a1e85a53f11ef85cfe89c25e08459),
(0x4b6f35fba53f11ef85cfe89c25e08459, 12.7, 0xafb527dda53e11ef85cfe89c25e08459, 0x264df25ca53f11ef85cfe89c25e08459),
(0x4b6f6112a53f11ef85cfe89c25e08459, 17.7, 0xafb72711a53e11ef85cfe89c25e08459, 0x263d71c4a53f11ef85cfe89c25e08459),
(0x4b6f7c6fa53f11ef85cfe89c25e08459, 8.4, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x264ed2e7a53f11ef85cfe89c25e08459),
(0x4b6f97eda53f11ef85cfe89c25e08459, 4.9, 0xafb28780a53e11ef85cfe89c25e08459, 0x263a39eda53f11ef85cfe89c25e08459),
(0x4b6fc3c0a53f11ef85cfe89c25e08459, 11.3, 0xafb8124ba53e11ef85cfe89c25e08459, 0x263c0e81a53f11ef85cfe89c25e08459),
(0x4b6fef5ea53f11ef85cfe89c25e08459, 9.1, 0xafb54337a53e11ef85cfe89c25e08459, 0x2649e21da53f11ef85cfe89c25e08459),
(0x4b700a39a53f11ef85cfe89c25e08459, 14.6, 0xafb7cd32a53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b703a88a53f11ef85cfe89c25e08459, 15.2, 0xafb69351a53e11ef85cfe89c25e08459, 0x263a99e4a53f11ef85cfe89c25e08459),
(0x4b7054c0a53f11ef85cfe89c25e08459, 18.9, 0xafb70d23a53e11ef85cfe89c25e08459, 0x26394943a53f11ef85cfe89c25e08459),
(0x4b7080f3a53f11ef85cfe89c25e08459, 16.5, 0xafb586e9a53e11ef85cfe89c25e08459, 0x263ef84ea53f11ef85cfe89c25e08459),
(0x4b709eeea53f11ef85cfe89c25e08459, 1.7, 0xafb414c4a53e11ef85cfe89c25e08459, 0x26468e2ca53f11ef85cfe89c25e08459),
(0x4b70bc20a53f11ef85cfe89c25e08459, 14.3, 0xafb5f404a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b70ec35a53f11ef85cfe89c25e08459, 0.6, 0xafb5b144a53e11ef85cfe89c25e08459, 0x2641320ca53f11ef85cfe89c25e08459),
(0x4b711e5fa53f11ef85cfe89c25e08459, 15.5, 0xafb39148a53e11ef85cfe89c25e08459, 0x264982b8a53f11ef85cfe89c25e08459),
(0x4b7139d4a53f11ef85cfe89c25e08459, 13.4, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x263e8844a53f11ef85cfe89c25e08459),
(0x4b7155ada53f11ef85cfe89c25e08459, 7.5, 0xafb28780a53e11ef85cfe89c25e08459, 0x263f4d7ca53f11ef85cfe89c25e08459),
(0x4b7171b1a53f11ef85cfe89c25e08459, 8.8, 0xafb14901a53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b719d31a53f11ef85cfe89c25e08459, 17.5, 0xafb28780a53e11ef85cfe89c25e08459, 0x26440de6a53f11ef85cfe89c25e08459),
(0x4b71cab8a53f11ef85cfe89c25e08459, 18.3, 0xafb4b3d1a53e11ef85cfe89c25e08459, 0x263faa19a53f11ef85cfe89c25e08459),
(0x4b71e664a53f11ef85cfe89c25e08459, 16, 0xafb4fa7fa53e11ef85cfe89c25e08459, 0x263d1f12a53f11ef85cfe89c25e08459),
(0x4b721273a53f11ef85cfe89c25e08459, 1.3, 0xafb8e114a53e11ef85cfe89c25e08459, 0x2645f059a53f11ef85cfe89c25e08459),
(0x4b723d6ca53f11ef85cfe89c25e08459, 8.5, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x2641788fa53f11ef85cfe89c25e08459),
(0x4b7257dda53f11ef85cfe89c25e08459, 16.1, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x264e6407a53f11ef85cfe89c25e08459),
(0x4b7273b2a53f11ef85cfe89c25e08459, 17.1, 0xafb75cfea53e11ef85cfe89c25e08459, 0x26488cb5a53f11ef85cfe89c25e08459),
(0x4b729093a53f11ef85cfe89c25e08459, 9.9, 0xafb8c5f9a53e11ef85cfe89c25e08459, 0x2649c700a53f11ef85cfe89c25e08459),
(0x4b72ad4fa53f11ef85cfe89c25e08459, 0.5, 0xafb332f5a53e11ef85cfe89c25e08459, 0x2650e378a53f11ef85cfe89c25e08459),
(0x4b72ca57a53f11ef85cfe89c25e08459, 16.4, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x264f4d5ea53f11ef85cfe89c25e08459),
(0x4b72e724a53f11ef85cfe89c25e08459, 7, 0xafb10d95a53e11ef85cfe89c25e08459, 0x264f4d5ea53f11ef85cfe89c25e08459),
(0x4b731267a53f11ef85cfe89c25e08459, 8.2, 0xafb47e6da53e11ef85cfe89c25e08459, 0x263f2346a53f11ef85cfe89c25e08459),
(0x4b732daea53f11ef85cfe89c25e08459, 2.1, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b734aa4a53f11ef85cfe89c25e08459, 7.7, 0xafb44932a53e11ef85cfe89c25e08459, 0x26514fa9a53f11ef85cfe89c25e08459),
(0x4b73666da53f11ef85cfe89c25e08459, 16.3, 0xafb8124ba53e11ef85cfe89c25e08459, 0x263c5ff4a53f11ef85cfe89c25e08459),
(0x4b738365a53f11ef85cfe89c25e08459, 16.2, 0xafb239efa53e11ef85cfe89c25e08459, 0x264e8016a53f11ef85cfe89c25e08459),
(0x4b73a0ada53f11ef85cfe89c25e08459, 14.9, 0xafb77980a53e11ef85cfe89c25e08459, 0x2641da99a53f11ef85cfe89c25e08459),
(0x4b73bce2a53f11ef85cfe89c25e08459, 11.1, 0xafb20453a53e11ef85cfe89c25e08459, 0x2642a132a53f11ef85cfe89c25e08459),
(0x4b73d80ea53f11ef85cfe89c25e08459, 12.8, 0xafb44932a53e11ef85cfe89c25e08459, 0x264f4d5ea53f11ef85cfe89c25e08459),
(0x4b74030ea53f11ef85cfe89c25e08459, 14.5, 0xafb586e9a53e11ef85cfe89c25e08459, 0x264516e8a53f11ef85cfe89c25e08459),
(0x4b741d0ba53f11ef85cfe89c25e08459, 8, 0xafb498c9a53e11ef85cfe89c25e08459, 0x264fd83ca53f11ef85cfe89c25e08459),
(0x4b744f91a53f11ef85cfe89c25e08459, 3.3, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x263909f9a53f11ef85cfe89c25e08459),
(0x4b746ddca53f11ef85cfe89c25e08459, 3.9, 0xafb8e114a53e11ef85cfe89c25e08459, 0x263bbb3ba53f11ef85cfe89c25e08459),
(0x4b748a03a53f11ef85cfe89c25e08459, 4.8, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x264beb08a53f11ef85cfe89c25e08459),
(0x4b74a5efa53f11ef85cfe89c25e08459, 8.4, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x26499c5fa53f11ef85cfe89c25e08459),
(0x4b74c1c6a53f11ef85cfe89c25e08459, 0.7, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x263f4d7ca53f11ef85cfe89c25e08459),
(0x4b74dd8fa53f11ef85cfe89c25e08459, 13.6, 0xafb3e0f0a53e11ef85cfe89c25e08459, 0x2649e21da53f11ef85cfe89c25e08459),
(0x4b7508aba53f11ef85cfe89c25e08459, 8.9, 0xafb19784a53e11ef85cfe89c25e08459, 0x264e2826a53f11ef85cfe89c25e08459),
(0x4b7526b1a53f11ef85cfe89c25e08459, 10.7, 0xafb3ac4da53e11ef85cfe89c25e08459, 0x264a2666a53f11ef85cfe89c25e08459),
(0x4b75447ea53f11ef85cfe89c25e08459, 9, 0xafb4b3d1a53e11ef85cfe89c25e08459, 0x264116b9a53f11ef85cfe89c25e08459),
(0x4b756211a53f11ef85cfe89c25e08459, 3.4, 0xafb3ac4da53e11ef85cfe89c25e08459, 0x264b892ea53f11ef85cfe89c25e08459),
(0x4b757fd8a53f11ef85cfe89c25e08459, 7.6, 0xafb6f261a53e11ef85cfe89c25e08459, 0x263ebf13a53f11ef85cfe89c25e08459),
(0x4b759c09a53f11ef85cfe89c25e08459, 0.5, 0xafb39148a53e11ef85cfe89c25e08459, 0x264d85dea53f11ef85cfe89c25e08459),
(0x4b75b952a53f11ef85cfe89c25e08459, 19.5, 0xafb94437a53e11ef85cfe89c25e08459, 0x263c5ff4a53f11ef85cfe89c25e08459),
(0x4b75d67ca53f11ef85cfe89c25e08459, 11.9, 0xafb2f0daa53e11ef85cfe89c25e08459, 0x263f4d7ca53f11ef85cfe89c25e08459),
(0x4b75f37aa53f11ef85cfe89c25e08459, 19.2, 0xafb253a9a53e11ef85cfe89c25e08459, 0x264c59bca53f11ef85cfe89c25e08459),
(0x4b760f80a53f11ef85cfe89c25e08459, 0.1, 0xafb16326a53e11ef85cfe89c25e08459, 0x2649c700a53f11ef85cfe89c25e08459),
(0x4b763bc8a53f11ef85cfe89c25e08459, 16.5, 0xafb498c9a53e11ef85cfe89c25e08459, 0x264a9551a53f11ef85cfe89c25e08459),
(0x4b7659f7a53f11ef85cfe89c25e08459, 10.1, 0xafb17dafa53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b76770da53f11ef85cfe89c25e08459, 16.6, 0xafb1b448a53e11ef85cfe89c25e08459, 0x264e8016a53f11ef85cfe89c25e08459),
(0x4b76a242a53f11ef85cfe89c25e08459, 9.7, 0xafb635e8a53e11ef85cfe89c25e08459, 0x264d6a17a53f11ef85cfe89c25e08459),
(0x4b76be43a53f11ef85cfe89c25e08459, 11.4, 0xafb70d23a53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b76da6ea53f11ef85cfe89c25e08459, 13.2, 0xafb8ab57a53e11ef85cfe89c25e08459, 0x263feef4a53f11ef85cfe89c25e08459),
(0x4b770725a53f11ef85cfe89c25e08459, 2, 0xafb463d8a53e11ef85cfe89c25e08459, 0x26501d9fa53f11ef85cfe89c25e08459),
(0x4b772209a53f11ef85cfe89c25e08459, 3.5, 0xafb239efa53e11ef85cfe89c25e08459, 0x26470136a53f11ef85cfe89c25e08459),
(0x4b774e3ea53f11ef85cfe89c25e08459, 12.7, 0xafb5ca84a53e11ef85cfe89c25e08459, 0x264452c3a53f11ef85cfe89c25e08459),
(0x4b777c3da53f11ef85cfe89c25e08459, 19.3, 0xafb47e6da53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b77a818a53f11ef85cfe89c25e08459, 0.3, 0xafb88fe2a53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b77d496a53f11ef85cfe89c25e08459, 6.1, 0xafb98cdba53e11ef85cfe89c25e08459, 0x263f4d7ca53f11ef85cfe89c25e08459),
(0x4b77fff7a53f11ef85cfe89c25e08459, 8, 0xafb10d95a53e11ef85cfe89c25e08459, 0x2640fb9fa53f11ef85cfe89c25e08459),
(0x4b781baea53f11ef85cfe89c25e08459, 16.4, 0xafb463d8a53e11ef85cfe89c25e08459, 0x264dbc5ba53f11ef85cfe89c25e08459),
(0x4b7837e3a53f11ef85cfe89c25e08459, 17, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x264b0301a53f11ef85cfe89c25e08459),
(0x4b78533aa53f11ef85cfe89c25e08459, 12.7, 0xafb28780a53e11ef85cfe89c25e08459, 0x2645351fa53f11ef85cfe89c25e08459),
(0x4b786eb6a53f11ef85cfe89c25e08459, 6.6, 0xafb332f5a53e11ef85cfe89c25e08459, 0x264a9551a53f11ef85cfe89c25e08459),
(0x4b789a33a53f11ef85cfe89c25e08459, 11.1, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x264d4efca53f11ef85cfe89c25e08459),
(0x4b78b4eea53f11ef85cfe89c25e08459, 3.4, 0xafb332f5a53e11ef85cfe89c25e08459, 0x2643be3ba53f11ef85cfe89c25e08459),
(0x4b78e580a53f11ef85cfe89c25e08459, 14.8, 0xafb54337a53e11ef85cfe89c25e08459, 0x264497aea53f11ef85cfe89c25e08459),
(0x4b791189a53f11ef85cfe89c25e08459, 11.8, 0xafb72711a53e11ef85cfe89c25e08459, 0x2638e70ba53f11ef85cfe89c25e08459),
(0x4b792c91a53f11ef85cfe89c25e08459, 9.5, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x263faa19a53f11ef85cfe89c25e08459),
(0x4b79480ba53f11ef85cfe89c25e08459, 13.4, 0xafb7b189a53e11ef85cfe89c25e08459, 0x264452c3a53f11ef85cfe89c25e08459),
(0x4b796364a53f11ef85cfe89c25e08459, 5.8, 0xafb7e83ba53e11ef85cfe89c25e08459, 0x26493de1a53f11ef85cfe89c25e08459),
(0x4b797e47a53f11ef85cfe89c25e08459, 1.6, 0xafb90b38a53e11ef85cfe89c25e08459, 0x264335efa53f11ef85cfe89c25e08459),
(0x4b799a1da53f11ef85cfe89c25e08459, 15.1, 0xafb10d95a53e11ef85cfe89c25e08459, 0x264e0cffa53f11ef85cfe89c25e08459),
(0x4b79b619a53f11ef85cfe89c25e08459, 12.6, 0xafb56d92a53e11ef85cfe89c25e08459, 0x264c8fb0a53f11ef85cfe89c25e08459),
(0x4b79d1d7a53f11ef85cfe89c25e08459, 9.1, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x263c5ff4a53f11ef85cfe89c25e08459),
(0x4b79edd0a53f11ef85cfe89c25e08459, 13.4, 0xafb527dda53e11ef85cfe89c25e08459, 0x264e4639a53f11ef85cfe89c25e08459),
(0x4b7a2012a53f11ef85cfe89c25e08459, 2.2, 0xafb21feba53e11ef85cfe89c25e08459, 0x26408edaa53f11ef85cfe89c25e08459),
(0x4b7a3bfca53f11ef85cfe89c25e08459, 6.1, 0xafb77980a53e11ef85cfe89c25e08459, 0x2643a31ca53f11ef85cfe89c25e08459),
(0x4b7a56c9a53f11ef85cfe89c25e08459, 13.9, 0xafb3769ea53e11ef85cfe89c25e08459, 0x264f083ca53f11ef85cfe89c25e08459),
(0x4b7a87d6a53f11ef85cfe89c25e08459, 15, 0xafb94437a53e11ef85cfe89c25e08459, 0x263ddf58a53f11ef85cfe89c25e08459),
(0x4b7ab355a53f11ef85cfe89c25e08459, 2.7, 0xafb28780a53e11ef85cfe89c25e08459, 0x264cdf23a53f11ef85cfe89c25e08459),
(0x4b7acd88a53f11ef85cfe89c25e08459, 0.9, 0xafb2a1c2a53e11ef85cfe89c25e08459, 0x2644ddbfa53f11ef85cfe89c25e08459),
(0x4b7afb54a53f11ef85cfe89c25e08459, 8.1, 0xafb414c4a53e11ef85cfe89c25e08459, 0x264d85dea53f11ef85cfe89c25e08459),
(0x4b7b18d1a53f11ef85cfe89c25e08459, 8.5, 0xafb47e6da53e11ef85cfe89c25e08459, 0x2641788fa53f11ef85cfe89c25e08459),
(0x4b7b343fa53f11ef85cfe89c25e08459, 2.5, 0xafb6d8d9a53e11ef85cfe89c25e08459, 0x264f6977a53f11ef85cfe89c25e08459),
(0x4b7b64faa53f11ef85cfe89c25e08459, 17.4, 0xafb47e6da53e11ef85cfe89c25e08459, 0x2642eb09a53f11ef85cfe89c25e08459),
(0x4b7b7f5ca53f11ef85cfe89c25e08459, 4, 0xafb414c4a53e11ef85cfe89c25e08459, 0x263ddf58a53f11ef85cfe89c25e08459),
(0x4b7baa4ea53f11ef85cfe89c25e08459, 9.9, 0xafb8c5f9a53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b7bd51fa53f11ef85cfe89c25e08459, 4.5, 0xafb47e6da53e11ef85cfe89c25e08459, 0x264116b9a53f11ef85cfe89c25e08459),
(0x4b7bef88a53f11ef85cfe89c25e08459, 7.3, 0xafb319f2a53e11ef85cfe89c25e08459, 0x263e5d9ba53f11ef85cfe89c25e08459),
(0x4b7c20a1a53f11ef85cfe89c25e08459, 13, 0xafb586e9a53e11ef85cfe89c25e08459, 0x264cdf23a53f11ef85cfe89c25e08459),
(0x4b7c4bc0a53f11ef85cfe89c25e08459, 14.1, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x264b892ea53f11ef85cfe89c25e08459),
(0x4b7c65c0a53f11ef85cfe89c25e08459, 10.8, 0xafb635e8a53e11ef85cfe89c25e08459, 0x264fae73a53f11ef85cfe89c25e08459),
(0x4b7c8584a53f11ef85cfe89c25e08459, 3.9, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x263ade76a53f11ef85cfe89c25e08459),
(0x4b7ca1daa53f11ef85cfe89c25e08459, 4.9, 0xafb3769ea53e11ef85cfe89c25e08459, 0x264eb79ea53f11ef85cfe89c25e08459),
(0x4b7cd27aa53f11ef85cfe89c25e08459, 16, 0xafb39148a53e11ef85cfe89c25e08459, 0x264d6a17a53f11ef85cfe89c25e08459),
(0x4b7d028ca53f11ef85cfe89c25e08459, 1.3, 0xafb2f0daa53e11ef85cfe89c25e08459, 0x26479624a53f11ef85cfe89c25e08459),
(0x4b7d1d0aa53f11ef85cfe89c25e08459, 18.5, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x26472d7fa53f11ef85cfe89c25e08459),
(0x4b7db166a53f11ef85cfe89c25e08459, 18.7, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x2646d52ca53f11ef85cfe89c25e08459),
(0x4b7dceb7a53f11ef85cfe89c25e08459, 1.7, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x26516a95a53f11ef85cfe89c25e08459),
(0x4b7deb11a53f11ef85cfe89c25e08459, 15.9, 0xafb47e6da53e11ef85cfe89c25e08459, 0x263b08b3a53f11ef85cfe89c25e08459),
(0x4b7e0717a53f11ef85cfe89c25e08459, 6.7, 0xafb527dda53e11ef85cfe89c25e08459, 0x264d4efca53f11ef85cfe89c25e08459),
(0x4b7e2382a53f11ef85cfe89c25e08459, 4.5, 0xafb28780a53e11ef85cfe89c25e08459, 0x2641320ca53f11ef85cfe89c25e08459),
(0x4b7e3e53a53f11ef85cfe89c25e08459, 12.8, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x26488cb5a53f11ef85cfe89c25e08459),
(0x4b7e6eb7a53f11ef85cfe89c25e08459, 1.2, 0xafb20453a53e11ef85cfe89c25e08459, 0x263ce91ca53f11ef85cfe89c25e08459),
(0x4b7e8ae0a53f11ef85cfe89c25e08459, 3.2, 0xafb9b99aa53e11ef85cfe89c25e08459, 0x2641f5c9a53f11ef85cfe89c25e08459),
(0x4b7eb5eaa53f11ef85cfe89c25e08459, 3.2, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x263ddf58a53f11ef85cfe89c25e08459),
(0x4b7ee7bfa53f11ef85cfe89c25e08459, 7.8, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x263c5ff4a53f11ef85cfe89c25e08459),
(0x4b7f0488a53f11ef85cfe89c25e08459, 6, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x2647471ca53f11ef85cfe89c25e08459),
(0x4b7f34eca53f11ef85cfe89c25e08459, 19.4, 0xafb54337a53e11ef85cfe89c25e08459, 0x263b4e8ea53f11ef85cfe89c25e08459),
(0x4b7f6015a53f11ef85cfe89c25e08459, 5.1, 0xafb90b38a53e11ef85cfe89c25e08459, 0x264c74c5a53f11ef85cfe89c25e08459),
(0x4b7f7a72a53f11ef85cfe89c25e08459, 11.7, 0xafb332f5a53e11ef85cfe89c25e08459, 0x264e6407a53f11ef85cfe89c25e08459),
(0x4b7fa5cfa53f11ef85cfe89c25e08459, 5.3, 0xafb42ec1a53e11ef85cfe89c25e08459, 0x26458b11a53f11ef85cfe89c25e08459),
(0x4b7fc0a8a53f11ef85cfe89c25e08459, 10.8, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x263e4361a53f11ef85cfe89c25e08459),
(0x4b7feba9a53f11ef85cfe89c25e08459, 6.1, 0xafb12ca2a53e11ef85cfe89c25e08459, 0x2650e378a53f11ef85cfe89c25e08459);
INSERT INTO `grades` (`id`, `score`, `assignment_id`, `enrollment_id`) VALUES
(0x4b801c1fa53f11ef85cfe89c25e08459, 14.3, 0xafb35c05a53e11ef85cfe89c25e08459, 0x2640aa16a53f11ef85cfe89c25e08459),
(0x4b80363fa53f11ef85cfe89c25e08459, 1.5, 0xafb67926a53e11ef85cfe89c25e08459, 0x2645a6e9a53f11ef85cfe89c25e08459),
(0x4b80614ea53f11ef85cfe89c25e08459, 6.3, 0xafb14901a53e11ef85cfe89c25e08459, 0x263a0452a53f11ef85cfe89c25e08459),
(0x4b808c8ea53f11ef85cfe89c25e08459, 6.6, 0xafb874e0a53e11ef85cfe89c25e08459, 0x264dd6d8a53f11ef85cfe89c25e08459),
(0x4b80a7a7a53f11ef85cfe89c25e08459, 11.7, 0xafb28780a53e11ef85cfe89c25e08459, 0x263c29fea53f11ef85cfe89c25e08459),
(0x4b80d7f9a53f11ef85cfe89c25e08459, 2.8, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x2643d8b6a53f11ef85cfe89c25e08459),
(0x4b80f243a53f11ef85cfe89c25e08459, 9.1, 0xafb874e0a53e11ef85cfe89c25e08459, 0x263a0452a53f11ef85cfe89c25e08459),
(0x4b812535a53f11ef85cfe89c25e08459, 5, 0xafb1e8d7a53e11ef85cfe89c25e08459, 0x263c7b7ea53f11ef85cfe89c25e08459),
(0x4b8155e0a53f11ef85cfe89c25e08459, 9.6, 0xafb874e0a53e11ef85cfe89c25e08459, 0x2650c851a53f11ef85cfe89c25e08459),
(0x4b81702ba53f11ef85cfe89c25e08459, 2.7, 0xafb2d6fca53e11ef85cfe89c25e08459, 0x264f329fa53f11ef85cfe89c25e08459),
(0x4b81a017a53f11ef85cfe89c25e08459, 11.1, 0xafb874e0a53e11ef85cfe89c25e08459, 0x2648d010a53f11ef85cfe89c25e08459),
(0x4b81cb06a53f11ef85cfe89c25e08459, 10.2, 0xafb4b3d1a53e11ef85cfe89c25e08459, 0x264eedfca53f11ef85cfe89c25e08459),
(0x4b81e602a53f11ef85cfe89c25e08459, 5, 0xafb77980a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b821b60a53f11ef85cfe89c25e08459, 3.1, 0xafb7b189a53e11ef85cfe89c25e08459, 0x2648b678a53f11ef85cfe89c25e08459),
(0x4b823608a53f11ef85cfe89c25e08459, 12.1, 0xafb39148a53e11ef85cfe89c25e08459, 0x2641788fa53f11ef85cfe89c25e08459),
(0x4b826156a53f11ef85cfe89c25e08459, 12.6, 0xafb44932a53e11ef85cfe89c25e08459, 0x264eedfca53f11ef85cfe89c25e08459),
(0x4b829178a53f11ef85cfe89c25e08459, 12.6, 0xafb253a9a53e11ef85cfe89c25e08459, 0x263d8c92a53f11ef85cfe89c25e08459),
(0x4b82abf3a53f11ef85cfe89c25e08459, 18.7, 0xafb8e114a53e11ef85cfe89c25e08459, 0x26385e9ba53f11ef85cfe89c25e08459),
(0x4b82dc68a53f11ef85cfe89c25e08459, 4.8, 0xafb414c4a53e11ef85cfe89c25e08459, 0x26428491a53f11ef85cfe89c25e08459),
(0x4b830c7ca53f11ef85cfe89c25e08459, 2, 0xafb19784a53e11ef85cfe89c25e08459, 0x26516a95a53f11ef85cfe89c25e08459),
(0x4b8326dea53f11ef85cfe89c25e08459, 3.4, 0xafb2bcc0a53e11ef85cfe89c25e08459, 0x264e0cffa53f11ef85cfe89c25e08459),
(0x4b8351ffa53f11ef85cfe89c25e08459, 0.6, 0xafb56d92a53e11ef85cfe89c25e08459, 0x264df25ca53f11ef85cfe89c25e08459),
(0x4b836c1fa53f11ef85cfe89c25e08459, 9.9, 0xafb527dda53e11ef85cfe89c25e08459, 0x263b4e8ea53f11ef85cfe89c25e08459),
(0x4b839d14a53f11ef85cfe89c25e08459, 3.3, 0xafb7950ca53e11ef85cfe89c25e08459, 0x263b4e8ea53f11ef85cfe89c25e08459),
(0x4b83cdafa53f11ef85cfe89c25e08459, 1.8, 0xafb16326a53e11ef85cfe89c25e08459, 0x264e8016a53f11ef85cfe89c25e08459),
(0x4b83e853a53f11ef85cfe89c25e08459, 7.5, 0xafb4b3d1a53e11ef85cfe89c25e08459, 0x263e5d9ba53f11ef85cfe89c25e08459),
(0x4b8413fda53f11ef85cfe89c25e08459, 18, 0xafb857f7a53e11ef85cfe89c25e08459, 0x264b530ba53f11ef85cfe89c25e08459),
(0x4b84404aa53f11ef85cfe89c25e08459, 18.4, 0xafb857f7a53e11ef85cfe89c25e08459, 0x263c4515a53f11ef85cfe89c25e08459),
(0x4b845ac0a53f11ef85cfe89c25e08459, 13.5, 0xafb1b448a53e11ef85cfe89c25e08459, 0x264d85dea53f11ef85cfe89c25e08459),
(0x4b848b04a53f11ef85cfe89c25e08459, 0.1, 0xafb28780a53e11ef85cfe89c25e08459, 0x263ab430a53f11ef85cfe89c25e08459),
(0x4b84a53ea53f11ef85cfe89c25e08459, 16.1, 0xafb54337a53e11ef85cfe89c25e08459, 0x26488cb5a53f11ef85cfe89c25e08459),
(0x4b84d010a53f11ef85cfe89c25e08459, 17.6, 0xafb586e9a53e11ef85cfe89c25e08459, 0x264a500ba53f11ef85cfe89c25e08459),
(0x4b84faf6a53f11ef85cfe89c25e08459, 3.1, 0xafb7b189a53e11ef85cfe89c25e08459, 0x264b1cfba53f11ef85cfe89c25e08459),
(0x4b85157fa53f11ef85cfe89c25e08459, 4.6, 0xafb47e6da53e11ef85cfe89c25e08459, 0x264c2294a53f11ef85cfe89c25e08459),
(0x4b854661a53f11ef85cfe89c25e08459, 4, 0xafb414c4a53e11ef85cfe89c25e08459, 0x26463d6ea53f11ef85cfe89c25e08459),
(0x4b857114a53f11ef85cfe89c25e08459, 3.9, 0xafb5b144a53e11ef85cfe89c25e08459, 0x264c8fb0a53f11ef85cfe89c25e08459),
(0x4b858b5ca53f11ef85cfe89c25e08459, 2.5, 0xafb6af3ba53e11ef85cfe89c25e08459, 0x263d3b79a53f11ef85cfe89c25e08459),
(0x4b85b720a53f11ef85cfe89c25e08459, 11.1, 0xafb2a1c2a53e11ef85cfe89c25e08459, 0x264fd83ca53f11ef85cfe89c25e08459),
(0x4b85d29ba53f11ef85cfe89c25e08459, 19.4, 0xafb4dd64a53e11ef85cfe89c25e08459, 0x264335efa53f11ef85cfe89c25e08459),
(0x4b8609eba53f11ef85cfe89c25e08459, 4.6, 0xafb1ce73a53e11ef85cfe89c25e08459, 0x26398690a53f11ef85cfe89c25e08459),
(0x4b863dc3a53f11ef85cfe89c25e08459, 17.6, 0xafb239efa53e11ef85cfe89c25e08459, 0x264bbee3a53f11ef85cfe89c25e08459),
(0x4b8659f8a53f11ef85cfe89c25e08459, 13.1, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x2638e70ba53f11ef85cfe89c25e08459),
(0x4b868af3a53f11ef85cfe89c25e08459, 12.7, 0xafb82cc2a53e11ef85cfe89c25e08459, 0x263ade76a53f11ef85cfe89c25e08459),
(0x4b86b6c1a53f11ef85cfe89c25e08459, 4.4, 0xafb8c5f9a53e11ef85cfe89c25e08459, 0x264beb08a53f11ef85cfe89c25e08459),
(0x4b86d2afa53f11ef85cfe89c25e08459, 7.5, 0xafb3c6cca53e11ef85cfe89c25e08459, 0x265134cea53f11ef85cfe89c25e08459),
(0x4b870333a53f11ef85cfe89c25e08459, 8.1, 0xafb65ed7a53e11ef85cfe89c25e08459, 0x263a6f5ea53f11ef85cfe89c25e08459),
(0x4b871d93a53f11ef85cfe89c25e08459, 16.9, 0xafb26d9fa53e11ef85cfe89c25e08459, 0x263faa19a53f11ef85cfe89c25e08459),
(0x4b875136a53f11ef85cfe89c25e08459, 9.9, 0xafb740f8a53e11ef85cfe89c25e08459, 0x2640e115a53f11ef85cfe89c25e08459),
(0x4b878182a53f11ef85cfe89c25e08459, 13.9, 0xafb72711a53e11ef85cfe89c25e08459, 0x263faa19a53f11ef85cfe89c25e08459),
(0x4b879befa53f11ef85cfe89c25e08459, 19.1, 0xafb8124ba53e11ef85cfe89c25e08459, 0x263dc28da53f11ef85cfe89c25e08459),
(0x4b87c73ba53f11ef85cfe89c25e08459, 8.8, 0xafb60d0ea53e11ef85cfe89c25e08459, 0x2647cec0a53f11ef85cfe89c25e08459),
(0x4b87e5a4a53f11ef85cfe89c25e08459, 18.8, 0xafb19784a53e11ef85cfe89c25e08459, 0x2643d8b6a53f11ef85cfe89c25e08459),
(0x4b881483a53f11ef85cfe89c25e08459, 19.4, 0xafb69351a53e11ef85cfe89c25e08459, 0x263f66cea53f11ef85cfe89c25e08459),
(0x4b8847dca53f11ef85cfe89c25e08459, 12.5, 0xafb586e9a53e11ef85cfe89c25e08459, 0x263b83e6a53f11ef85cfe89c25e08459),
(0x4b8888c1a53f11ef85cfe89c25e08459, 15.2, 0xafb7950ca53e11ef85cfe89c25e08459, 0x264beb08a53f11ef85cfe89c25e08459),
(0x4b88b3e8a53f11ef85cfe89c25e08459, 17.1, 0xafb7950ca53e11ef85cfe89c25e08459, 0x264c59bca53f11ef85cfe89c25e08459),
(0x4b88e4cba53f11ef85cfe89c25e08459, 3.2, 0xafb47e6da53e11ef85cfe89c25e08459, 0x26446dbaa53f11ef85cfe89c25e08459),
(0x4b89016ca53f11ef85cfe89c25e08459, 14.9, 0xafb75cfea53e11ef85cfe89c25e08459, 0x26482e99a53f11ef85cfe89c25e08459),
(0x4b891d28a53f11ef85cfe89c25e08459, 8.6, 0xafb39148a53e11ef85cfe89c25e08459, 0x2648fa5aa53f11ef85cfe89c25e08459),
(0x4b8937e9a53f11ef85cfe89c25e08459, 1.7, 0xafb54337a53e11ef85cfe89c25e08459, 0x264e9c38a53f11ef85cfe89c25e08459);

-- --------------------------------------------------------

--
-- Structure de la table `requests`
--

DROP TABLE IF EXISTS `requests`;
CREATE TABLE IF NOT EXISTS `requests` (
  `id` binary(16) NOT NULL,
  `status` bit(1) NOT NULL,
  `student_id` binary(16) DEFAULT NULL,
  `teacher_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqi6ae3wvfk4aq3w1ao6x4lvk1` (`student_id`),
  UNIQUE KEY `UK4fap4wsul0ga8qdojpjywtcyx` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` enum('ADMIN','STUDENT','TEACHER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `is_verified` bit(1) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `date_of_birth` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe2rndfrsx22acpq2ty1caeuyw` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `students`
--

INSERT INTO `students` (`id`, `email`, `firstname`, `is_verified`, `lastname`, `password`, `salt`, `date_of_birth`) VALUES
(0xafa64618a53e11ef85cfe89c25e08459, 'student1@example.com', 'Student1', b'1', 'Lastname1', 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg==', '2000-01-02'),
(0xafa679efa53e11ef85cfe89c25e08459, 'student2@example.com', 'Student2', b'1', 'Lastname2', 'hashed_password2', 'salt2', '2000-01-03'),
(0xafa6954fa53e11ef85cfe89c25e08459, 'student3@example.com', 'Student3', b'1', 'Lastname3', 'hashed_password3', 'salt3', '2000-01-04'),
(0xafa6eeb9a53e11ef85cfe89c25e08459, 'student4@example.com', 'Student4', b'1', 'Lastname4', 'hashed_password4', 'salt4', '2000-01-05'),
(0xafa728e8a53e11ef85cfe89c25e08459, 'student5@example.com', 'Student5', b'1', 'Lastname5', 'hashed_password5', 'salt5', '2000-01-06'),
(0xafa75273a53e11ef85cfe89c25e08459, 'student6@example.com', 'Student6', b'1', 'Lastname6', 'hashed_password6', 'salt6', '2000-01-07'),
(0xafa76c70a53e11ef85cfe89c25e08459, 'student7@example.com', 'Student7', b'1', 'Lastname7', 'hashed_password7', 'salt7', '2000-01-08'),
(0xafa79734a53e11ef85cfe89c25e08459, 'student8@example.com', 'Student8', b'1', 'Lastname8', 'hashed_password8', 'salt8', '2000-01-09'),
(0xafa7b13da53e11ef85cfe89c25e08459, 'student9@example.com', 'Student9', b'1', 'Lastname9', 'hashed_password9', 'salt9', '2000-01-10'),
(0xafa7ca3fa53e11ef85cfe89c25e08459, 'student10@example.com', 'Student10', b'1', 'Lastname10', 'hashed_password10', 'salt10', '2000-01-11'),
(0xafa7e371a53e11ef85cfe89c25e08459, 'student11@example.com', 'Student11', b'1', 'Lastname11', 'hashed_password11', 'salt11', '2000-01-12'),
(0xafa7fc9ea53e11ef85cfe89c25e08459, 'student12@example.com', 'Student12', b'1', 'Lastname12', 'hashed_password12', 'salt12', '2000-01-13'),
(0xafa81593a53e11ef85cfe89c25e08459, 'student13@example.com', 'Student13', b'1', 'Lastname13', 'hashed_password13', 'salt13', '2000-01-14'),
(0xafa82e92a53e11ef85cfe89c25e08459, 'student14@example.com', 'Student14', b'1', 'Lastname14', 'hashed_password14', 'salt14', '2000-01-15'),
(0xafa8470ba53e11ef85cfe89c25e08459, 'student15@example.com', 'Student15', b'1', 'Lastname15', 'hashed_password15', 'salt15', '2000-01-16'),
(0xafa85fc2a53e11ef85cfe89c25e08459, 'student16@example.com', 'Student16', b'1', 'Lastname16', 'hashed_password16', 'salt16', '2000-01-17'),
(0xafa8797da53e11ef85cfe89c25e08459, 'student17@example.com', 'Student17', b'1', 'Lastname17', 'hashed_password17', 'salt17', '2000-01-18'),
(0xafa893e1a53e11ef85cfe89c25e08459, 'student18@example.com', 'Student18', b'1', 'Lastname18', 'hashed_password18', 'salt18', '2000-01-19'),
(0xafa8ae24a53e11ef85cfe89c25e08459, 'student19@example.com', 'Student19', b'1', 'Lastname19', 'hashed_password19', 'salt19', '2000-01-20'),
(0xafa8c77ca53e11ef85cfe89c25e08459, 'student20@example.com', 'Student20', b'1', 'Lastname20', 'hashed_password20', 'salt20', '2000-01-21'),
(0xafa8e0cea53e11ef85cfe89c25e08459, 'student21@example.com', 'Student21', b'1', 'Lastname21', 'hashed_password21', 'salt21', '2000-01-22'),
(0xafa908f6a53e11ef85cfe89c25e08459, 'student22@example.com', 'Student22', b'1', 'Lastname22', 'hashed_password22', 'salt22', '2000-01-23'),
(0xafa92159a53e11ef85cfe89c25e08459, 'student23@example.com', 'Student23', b'1', 'Lastname23', 'hashed_password23', 'salt23', '2000-01-24'),
(0xafa94c76a53e11ef85cfe89c25e08459, 'student24@example.com', 'Student24', b'1', 'Lastname24', 'hashed_password24', 'salt24', '2000-01-25'),
(0xafa96635a53e11ef85cfe89c25e08459, 'student25@example.com', 'Student25', b'1', 'Lastname25', 'hashed_password25', 'salt25', '2000-01-26'),
(0xafa97f9fa53e11ef85cfe89c25e08459, 'student26@example.com', 'Student26', b'1', 'Lastname26', 'hashed_password26', 'salt26', '2000-01-27'),
(0xafa9999ca53e11ef85cfe89c25e08459, 'student27@example.com', 'Student27', b'1', 'Lastname27', 'hashed_password27', 'salt27', '2000-01-28'),
(0xafa9b2b7a53e11ef85cfe89c25e08459, 'student28@example.com', 'Student28', b'1', 'Lastname28', 'hashed_password28', 'salt28', '2000-01-29'),
(0xafa9cb42a53e11ef85cfe89c25e08459, 'student29@example.com', 'Student29', b'1', 'Lastname29', 'hashed_password29', 'salt29', '2000-01-30'),
(0xafa9e400a53e11ef85cfe89c25e08459, 'student30@example.com', 'Student30', b'1', 'Lastname30', 'hashed_password30', 'salt30', '2000-01-31'),
(0xafa9fc3fa53e11ef85cfe89c25e08459, 'student31@example.com', 'Student31', b'1', 'Lastname31', 'hashed_password31', 'salt31', '2000-02-01'),
(0xafaa149ea53e11ef85cfe89c25e08459, 'student32@example.com', 'Student32', b'1', 'Lastname32', 'hashed_password32', 'salt32', '2000-02-02'),
(0xafaa2cfda53e11ef85cfe89c25e08459, 'student33@example.com', 'Student33', b'1', 'Lastname33', 'hashed_password33', 'salt33', '2000-02-03'),
(0xafaa5540a53e11ef85cfe89c25e08459, 'student34@example.com', 'Student34', b'1', 'Lastname34', 'hashed_password34', 'salt34', '2000-02-04'),
(0xafaa6d6ba53e11ef85cfe89c25e08459, 'student35@example.com', 'Student35', b'1', 'Lastname35', 'hashed_password35', 'salt35', '2000-02-05'),
(0xafaa9528a53e11ef85cfe89c25e08459, 'student36@example.com', 'Student36', b'1', 'Lastname36', 'hashed_password36', 'salt36', '2000-02-06'),
(0xafaaad96a53e11ef85cfe89c25e08459, 'student37@example.com', 'Student37', b'1', 'Lastname37', 'hashed_password37', 'salt37', '2000-02-07'),
(0xafaac69da53e11ef85cfe89c25e08459, 'student38@example.com', 'Student38', b'1', 'Lastname38', 'hashed_password38', 'salt38', '2000-02-08'),
(0xafaadf0ca53e11ef85cfe89c25e08459, 'student39@example.com', 'Student39', b'1', 'Lastname39', 'hashed_password39', 'salt39', '2000-02-09'),
(0xafaaf7aca53e11ef85cfe89c25e08459, 'student40@example.com', 'Student40', b'1', 'Lastname40', 'hashed_password40', 'salt40', '2000-02-10'),
(0xafab0fc1a53e11ef85cfe89c25e08459, 'student41@example.com', 'Student41', b'1', 'Lastname41', 'hashed_password41', 'salt41', '2000-02-11'),
(0xafab2863a53e11ef85cfe89c25e08459, 'student42@example.com', 'Student42', b'1', 'Lastname42', 'hashed_password42', 'salt42', '2000-02-12'),
(0xafab4037a53e11ef85cfe89c25e08459, 'student43@example.com', 'Student43', b'1', 'Lastname43', 'hashed_password43', 'salt43', '2000-02-13'),
(0xafab58e9a53e11ef85cfe89c25e08459, 'student44@example.com', 'Student44', b'1', 'Lastname44', 'hashed_password44', 'salt44', '2000-02-14'),
(0xafab7110a53e11ef85cfe89c25e08459, 'student45@example.com', 'Student45', b'1', 'Lastname45', 'hashed_password45', 'salt45', '2000-02-15'),
(0xafab9a16a53e11ef85cfe89c25e08459, 'student46@example.com', 'Student46', b'1', 'Lastname46', 'hashed_password46', 'salt46', '2000-02-16'),
(0xafabc8b8a53e11ef85cfe89c25e08459, 'student47@example.com', 'Student47', b'1', 'Lastname47', 'hashed_password47', 'salt47', '2000-02-17'),
(0xafabe2d3a53e11ef85cfe89c25e08459, 'student48@example.com', 'Student48', b'1', 'Lastname48', 'hashed_password48', 'salt48', '2000-02-18'),
(0xafabfc6ea53e11ef85cfe89c25e08459, 'student49@example.com', 'Student49', b'1', 'Lastname49', 'hashed_password49', 'salt49', '2000-02-19'),
(0xafac1503a53e11ef85cfe89c25e08459, 'student50@example.com', 'Student50', b'1', 'Lastname50', 'hashed_password50', 'salt50', '2000-02-20');

-- --------------------------------------------------------

--
-- Structure de la table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
CREATE TABLE IF NOT EXISTS `teachers` (
  `id` binary(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `is_verified` bit(1) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4l9jjfvsct1dd5aufnurxcvbs` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `teachers`
--

INSERT INTO `teachers` (`id`, `email`, `firstname`, `is_verified`, `lastname`, `password`, `salt`, `department`) VALUES
(0xafacf640a53e11ef85cfe89c25e08459, 'teacher1@example.com', 'Teacher1', b'1', 'Lastname1', 'hashed_password1', 'salt1', 'PHYSIQUE'),
(0xafad10b0a53e11ef85cfe89c25e08459, 'teacher2@example.com', 'Teacher2', b'1', 'Lastname2', 'hashed_password2', 'salt2', 'MATHEMATIQUES'),
(0xafad2b20a53e11ef85cfe89c25e08459, 'teacher3@example.com', 'Teacher3', b'1', 'Lastname3', 'hashed_password3', 'salt3', 'PHYSIQUE'),
(0xafad441aa53e11ef85cfe89c25e08459, 'teacher4@example.com', 'Teacher4', b'1', 'Lastname4', 'hashed_password4', 'salt4', 'MATHEMATIQUES'),
(0xafad5dbba53e11ef85cfe89c25e08459, 'teacher5@example.com', 'Teacher5', b'1', 'Lastname5', 'hashed_password5', 'salt5', 'PHYSIQUE'),
(0xafad7670a53e11ef85cfe89c25e08459, 'teacher6@example.com', 'Teacher6', b'1', 'Lastname6', 'hashed_password6', 'salt6', 'MATHEMATIQUES'),
(0xafad8eafa53e11ef85cfe89c25e08459, 'teacher7@example.com', 'Teacher7', b'1', 'Lastname7', 'hashed_password7', 'salt7', 'PHYSIQUE'),
(0xafadb743a53e11ef85cfe89c25e08459, 'teacher8@example.com', 'Teacher8', b'1', 'Lastname8', 'hashed_password8', 'salt8', 'MATHEMATIQUES'),
(0xafadcf2fa53e11ef85cfe89c25e08459, 'teacher9@example.com', 'Teacher9', b'1', 'Lastname9', 'hashed_password9', 'salt9', 'PHYSIQUE'),
(0xafadf940a53e11ef85cfe89c25e08459, 'teacher10@example.com', 'Teacher10', b'1', 'Lastname10', 'hashed_password10', 'salt10', 'MATHEMATIQUES');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `assignments`
--
ALTER TABLE `assignments`
  ADD CONSTRAINT `FK6p1m72jobsvmrrn4bpj4168mg` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Contraintes pour la table `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `FK468oyt88pgk2a0cxrvxygadqg` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`);

--
-- Contraintes pour la table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `FK8kf1u1857xgo56xbfmnif2c51` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Contraintes pour la table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `FK287de6bfabv9mj8r4flun01jd` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`id`),
  ADD CONSTRAINT `FK354vwjlgqmgi7tveernv2ylp6` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`);

--
-- Contraintes pour la table `requests`
--
ALTER TABLE `requests`
  ADD CONSTRAINT `FKhdl1thi9l90rxkj82qekjhl` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FKo19jaswa5so3jh41g9wa5cmw2` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`);

--
-- Contraintes pour la table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
