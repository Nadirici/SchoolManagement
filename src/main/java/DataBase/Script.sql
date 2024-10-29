-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 29 oct. 2024 à 19:17
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
-- Base de données : `schoolmanagement`
--

-- --------------------------------------------------------

--
-- Structure de la table `course`
--

DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course` (
                                        `course_id` int NOT NULL,
                                        `course_title` varchar(100) DEFAULT NULL,
                                        `credit` tinyint DEFAULT NULL,
                                        `teacher_id` int DEFAULT NULL,
                                        `department_id` int DEFAULT NULL,
                                        `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                        `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`course_id`),
                                        KEY `teacher_id` (`teacher_id`),
                                        KEY `department_id` (`department_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `department`
--

DROP TABLE IF EXISTS `department`;
CREATE TABLE IF NOT EXISTS `department` (
                                            `department_id` int NOT NULL,
                                            `name` varchar(50) DEFAULT NULL,
                                            PRIMARY KEY (`department_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
CREATE TABLE IF NOT EXISTS `enrollment` (
                                            `student_id` int NOT NULL,
                                            `course_id` int NOT NULL,
                                            `date` date DEFAULT NULL,
                                            PRIMARY KEY (`student_id`,`course_id`),
                                            KEY `course_id` (`course_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `exam`
--

DROP TABLE IF EXISTS `exam`;
CREATE TABLE IF NOT EXISTS `exam` (
                                      `exam_id` int NOT NULL,
                                      `type` varchar(50) DEFAULT NULL,
                                      PRIMARY KEY (`exam_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `grade`
--

DROP TABLE IF EXISTS `grade`;
CREATE TABLE IF NOT EXISTS `grade` (
                                       `grade_id` int NOT NULL,
                                       `student_id` int DEFAULT NULL,
                                       `course_id` int DEFAULT NULL,
                                       `exam_id` int DEFAULT NULL,
                                       `date` date DEFAULT NULL,
                                       `grade` decimal(5,2) DEFAULT NULL,
                                       PRIMARY KEY (`grade_id`),
                                       KEY `student_id` (`student_id`),
                                       KEY `course_id` (`course_id`),
                                       KEY `exam_id` (`exam_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
                                          `user_id` varchar(255) NOT NULL,
                                          `created_at` date DEFAULT NULL,
                                          `user_email` varchar(255) DEFAULT NULL,
                                          `first_name` varchar(255) DEFAULT NULL,
                                          `last_name` varchar(255) DEFAULT NULL,
                                          `user_password` varchar(255) DEFAULT NULL,
                                          `updated_at` date DEFAULT NULL,
                                          `is_verified` bit(1) DEFAULT NULL,
                                          `date_of_birth` date DEFAULT NULL,
                                          PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `students`
--

INSERT INTO `students` (`user_id`, `created_at`, `user_email`, `first_name`, `last_name`, `user_password`, `updated_at`, `is_verified`, `date_of_birth`) VALUES
                                                                                                                                                             ('25d1af', '2024-10-29', 'Nadir1401@gmail.com', 'a', 'a', 'a', '2024-10-29', b'0', '2004-01-14'),
                                                                                                                                                             ('267e5b', '2024-10-29', 'w@w.com', 'w', 'w', 'w', '2024-10-29', b'0', '2003-05-04'),
                                                                                                                                                             ('2b6f51', '2024-10-29', 'test@test.com', 'test', 'test', 'test', '2024-10-29', b'0', '2004-01-14');

-- --------------------------------------------------------

--
-- Structure de la table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
                                         `user_id` varchar(100) NOT NULL,
                                         `first_name` varchar(50) DEFAULT NULL,
                                         `last_name` varchar(50) DEFAULT NULL,
                                         `user_email` varchar(100) DEFAULT NULL,
                                         `is_verified` tinyint(1) DEFAULT NULL,
                                         `password` varchar(255) DEFAULT NULL,
                                         `department_id` int DEFAULT NULL,
                                         `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                         `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`user_id`),
                                         UNIQUE KEY `user_email` (`user_email`),
                                         KEY `department_id` (`department_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
