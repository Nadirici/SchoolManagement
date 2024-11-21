-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 19 nov. 2024 à 12:03
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
                                                                                                     (0x84a5b0b76df7418b8ce6ddd2d1e03be3, 'huynhquanb@cy-tech.fr', 'Melvin', b'1', 'Huynh-Quan-Binh', 'WUN93MWuHZyF/OW2REIMyw==', 'GC5egRGf5B3ptGNKR65wjA=='),
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
                                                                                         (0x3a62f67d1a2e4af1bcf47f9f4b5366e4, 1, 'Basic algebra problems covering addition and multiplication', 'Algebra Basics', 0x1e9b7b9b3c764f2eb8b08b47b8a5e7a8),
                                                                                         (0x442ddaf538454ee6b48e071e0a9c2d9f, 1, 'Advanced problem set on linear equations and inequalities', 'Linear Equations', 0x1e9b7b9b3c764f2eb8b08b47b8a5e7a8),
                                                                                         (0x543f03b82df04404984080ce3eed9d31, 1, 'Introduction to Newton\'s laws of motion', 'Newton\'s Laws', 0xcc5e0d3a1cb34e1fbc02b5b646f6be68),
                                                                                         (0x96a786c736964938a147969e86df4efc, 1, 'Fundamental chemical reactions and balancing equations', 'Chemical Reactions', 0xe6cfd17c30714a8d87ec5b5c314f44f8),
                                                                                         (0xacb113f28b364b8983918e9a78707744, 1, 'Problem set on kinetic and potential energy concepts', 'Energy Concepts', 0xcc5e0d3a1cb34e1fbc02b5b646f6be68),
                                                                                         (0xdb8deae5ea2d4f6ebb6583b856ca90c1, 1, 'Study on atomic structure and periodic table elements', 'Atomic Structure', 0xe6cfd17c30714a8d87ec5b5c314f44f8);

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
                                                                      (0x1e9b7b9b3c764f2eb8b08b47b8a5e7a8, 'An introductory course to Mathematics', 'Mathematics', 0x4a1c11853f8f4e0da6f33c24155a7a7f),
                                                                      (0x6352b59d73ed44059ce2d7701e10626c, 'cours d\'info', 'Informatique test', 0x4639da0b4b524f168e83ff863310ddfb),
                                                                      (0xcc5e0d3a1cb34e1fbc02b5b646f6be68, 'Fundamentals of Physics', 'Physics', 0x5bce19f690784e348d5bb2e6537b2330),
                                                                      (0xe6cfd17c30714a8d87ec5b5c314f44f8, 'Introduction to Chemistry concepts', 'Chemistry', 0xbe3b84f31a9f4c7e94479ae484d8e0cc);

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
                                                                (0x0233b4828d184fb592eb2ed6737931bf, 0x1e9b7b9b3c764f2eb8b08b47b8a5e7a8, 0x39b3c9e6e72f4c75a83fc7c097a36eec),
                                                                (0x0f0010fe7d864da4acb0b5638b704305, 0xcc5e0d3a1cb34e1fbc02b5b646f6be68, 0x39b3c9e6e72f4c75a83fc7c097a36eec),
                                                                (0x1bcf76e108b34c12b654c7f5a0f6aa01, 0xcc5e0d3a1cb34e1fbc02b5b646f6be68, 0x3f17a4046b044e49bf475c97a9fef2a6),
                                                                (0x3f22c5eb64f44c08914f99d737ddf586, 0xe6cfd17c30714a8d87ec5b5c314f44f8, 0x39b3c9e6e72f4c75a83fc7c097a36eec),
                                                                (0x64ad75776d6d43f0b0c247da16009d1a, 0x1e9b7b9b3c764f2eb8b08b47b8a5e7a8, 0x3f17a4046b044e49bf475c97a9fef2a6),
                                                                (0xa5b8f80b0c8e4c84bb433e5b7b07f1c7, 0x1e9b7b9b3c764f2eb8b08b47b8a5e7a8, 0x82107de4968c46de84eac75a443d42ba),
                                                                (0xe9784570d2dd4888a7ba4fd50e046d94, 0x6352b59d73ed44059ce2d7701e10626c, 0x39b3c9e6e72f4c75a83fc7c097a36eec);

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
                                                                           (0x4b4f1eae12d344a2b6bc6f91a6f0b87e, 18, 0x442ddaf538454ee6b48e071e0a9c2d9f, 0xa5b8f80b0c8e4c84bb433e5b7b07f1c7),
                                                                           (0x7d8b1e7c12c44d41b6f850d25f1ab1c4, 17, 0x3a62f67d1a2e4af1bcf47f9f4b5366e4, 0xa5b8f80b0c8e4c84bb433e5b7b07f1c7),
                                                                           (0xa1b3e1d43f894b2f9576b4e8d0e9f8b2, 15.5, 0x543f03b82df04404984080ce3eed9d31, 0x1bcf76e108b34c12b654c7f5a0f6aa01),
                                                                           (0xa9d7c1e54b8a4e5b94766a8e1d2b9c3f, 18.25, 0x442ddaf538454ee6b48e071e0a9c2d9f, 0x0233b4828d184fb592eb2ed6737931bf),
                                                                           (0xb4c7d1a24f9e4a3bb9c67e8f0d4b3a5f, 16.5, 0x442ddaf538454ee6b48e071e0a9c2d9f, 0x64ad75776d6d43f0b0c247da16009d1a),
                                                                           (0xc3d8a3f78f4c4f7298c45b8e0d2e9f1b, 18.5, 0x96a786c736964938a147969e86df4efc, 0x3f22c5eb64f44c08914f99d737ddf586),
                                                                           (0xd4e9b1f89f6d4b8eb5c76a9f1e0d8a3e, 17.75, 0xdb8deae5ea2d4f6ebb6583b856ca90c1, 0x3f22c5eb64f44c08914f99d737ddf586),
                                                                           (0xe7b3f1a98c7f4e2d94b66f5d1c3b2a4d, 15.25, 0x3a62f67d1a2e4af1bcf47f9f4b5366e4, 0x64ad75776d6d43f0b0c247da16009d1a),
                                                                           (0xf1e3b5d87a9e4f8b95746b5d1f9e2c3a, 17.5, 0x3a62f67d1a2e4af1bcf47f9f4b5366e4, 0x0233b4828d184fb592eb2ed6737931bf),
                                                                           (0xf3d2e0a63c4a4f8e95b3d1b4e0f8c7a5, 17.5, 0xacb113f28b364b8983918e9a78707744, 0x1bcf76e108b34c12b654c7f5a0f6aa01);

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

--
-- Déchargement des données de la table `requests`
--

INSERT INTO `requests` (`id`, `status`, `student_id`, `teacher_id`) VALUES
                                                                        (0x043973d8ceeb421085ee07b94fedd8dc, b'1', NULL, 0x4639da0b4b524f168e83ff863310ddfb),
                                                                        (0x30f117a2f774423f8dd192ffc458bd0b, b'1', NULL, 0x5bce19f690784e348d5bb2e6537b2330),
                                                                        (0xa5a42779839443e5b313c2de24b4a8c2, b'1', NULL, 0xbe3b84f31a9f4c7e94479ae484d8e0cc),
                                                                        (0xd23c99ffd08843a687760df764a4cab4, b'1', NULL, 0x4a1c11853f8f4e0da6f33c24155a7a7f),
                                                                        (0xd42a225c71e442589bf7d92c7c5c72ee, b'1', 0x39b3c9e6e72f4c75a83fc7c097a36eec, NULL),
                                                                        (0xd536248eab7242a3936dd1622f129660, b'1', 0x3f17a4046b044e49bf475c97a9fef2a6, NULL),
                                                                        (0xe0cbf4f4a8334246ad519e77ef783313, b'1', 0x82107de4968c46de84eac75a443d42ba, NULL),
                                                                        (0xe50d89d721b545f3a3f25c2a15fc61d4, b'0', 0xc9f6e1a82d8b4f3c9e0d5b9f3d8a0c7f, NULL);

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
                                                                                                                        (0x39b3c9e6e72f4c75a83fc7c097a36eec, 'bwhite@example.com', 'Bob', b'1', 'White', 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg==', '2001-03-10'),
                                                                                                                        (0x3f17a4046b044e49bf475c97a9fef2a6, 'asmith@example.com', 'Alice', b'1', 'Smith', NULL, NULL, '1999-07-23'),
                                                                                                                        (0x82107de4968c46de84eac75a443d42ba, 'jdoe@example.com', 'John', b'1', 'Doe', NULL, NULL, '2000-01-15'),
                                                                                                                        (0xc9f6e1a82d8b4f3c9e0d5b9f3d8a0c7f, 'unis_verified@example.com', 'Charlie', b'0', 'Brown', NULL, NULL, '2002-05-15');

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
                                                                                                                     (0x4639da0b4b524f168e83ff863310ddfb, 'test@test.com', 'test', b'1', 'test', 'U5B9CVOWHwumfW1QXcLg6Q==', 'wrfig67+hMdminYaQsX5Mg==', 'INFORMATIQUE'),
                                                                                                                     (0x4a1c11853f8f4e0da6f33c24155a7a7f, 'prof.jones@example.com', 'Emma', b'1', 'Jones', NULL, NULL, NULL),
                                                                                                                     (0x5bce19f690784e348d5bb2e6537b2330, 'prof.brown@example.com', 'Liam', b'1', 'Brown', NULL, NULL, NULL),
                                                                                                                     (0xbe3b84f31a9f4c7e94479ae484d8e0cc, 'prof.smith@example.com', 'Olivia', b'1', 'Smith', NULL, NULL, NULL);

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
