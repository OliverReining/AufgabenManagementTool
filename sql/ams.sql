-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 27. Sep 2024 um 21:28
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `ams`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `benutzer`
--

CREATE TABLE `benutzer` (
  `userid` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `vorname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pass` varchar(50) NOT NULL DEFAULT 'pass123',
  `role` varchar(20) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `benutzer`
--

INSERT INTO `benutzer` (`userid`, `name`, `vorname`, `email`, `pass`, `role`) VALUES
(1, 'Reining', 'Oliver', 'oreining@email.com', 'sicheresPasswort', 'admin'),
(2, 'Bach', 'Klara', 'kbach@email.com', 'pass123', 'user'),
(3, 'Johnson', 'Alex', 'ajohnson@email.com', 'pass123', 'user'),
(4, 'James', 'Ben', 'bjames@email.com', 'pass123', 'user'),
(5, 'Miller', 'Chris', 'cmiller@email.com', 'pass123', 'user'),
(6, 'Green', 'David', 'dgreen@email.com', 'pass123', 'user'),
(7, 'Smith', 'Emma', 'esmith@email.com', 'pass123', 'user'),
(8, 'Jones', 'Fred', 'fjones@email.com', 'pass123', 'user'),
(9, 'Martin', 'Grace', 'gmartin@email.com', 'pass123', 'user'),
(10, 'Lee', 'Hannah', 'hlee@email.com', 'pass123', 'user'),
(11, 'Martin', 'Isaac', 'imartin@email.com', 'pass123', 'user'),
(12, 'Smith', 'Jack', 'jsmith@email.com', 'pass123', 'user'),
(13, 'Wilson', 'Kate', 'kwilson@email.com', 'pass123', 'user'),
(14, 'Lopez', 'Liam', 'llopez@email.com', 'pass123', 'user'),
(15, 'Turner', 'Mia', 'mturner@email.com', 'pass123', 'user'),
(16, 'White', 'Noah', 'nwhite@email.com', 'pass123', 'user'),
(17, 'Sanchez', 'Olivia', 'osanchez@email.com', 'pass123', 'user'),
(18, 'Johnson', 'Paul', 'pjohnson@email.com', 'pass123', 'user'),
(19, 'King', 'Quinn', 'qking@email.com', 'pass123', 'user'),
(20, 'Scott', 'Rachel', 'rscott@email.com', 'pass123', 'user'),
(21, 'Clark', 'Sam', 'sclark@email.com', 'pass123', 'user'),
(22, 'Roberts', 'Tom', 'troberts@email.com', 'pass123', 'user');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `project`
--

CREATE TABLE `project` (
  `projectid` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `projectlead` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `project`
--

INSERT INTO `project` (`projectid`, `title`, `description`, `projectlead`) VALUES
(1, 'Team Management', 'Projektmanagement-System für Teams', 1),
(2, 'CRM System', 'CRM-System für Kundenpflege', 1),
(3, 'Process Automation', 'Automatisierung von Prozessen', 3),
(4, 'E-Commerce Development', 'E-Commerce Website', 7),
(5, 'Fitness App Development', 'Mobile App Entwicklung für Fitness-Tracking', 9),
(6, 'AI Recommendation System', 'AI basiertes Empfehlungssystem für E-Commerce', 11),
(7, 'CMS Development', 'Content-Management-System für Blogs', 13),
(8, 'Social Media Development', 'Social Media Plattform für Networking', 15),
(9, 'Project Management Tool', 'Projektmanagement-Tool für IT-Projekte', 17);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `task`
--

CREATE TABLE `task` (
  `taskid` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `projectid` int(11) NOT NULL,
  `isFinished` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `task`
--

INSERT INTO `task` (`taskid`, `title`, `description`, `projectid`, `isFinished`) VALUES
(1, 'Database Design', 'Datenbankdesign', 1, 0),
(2, 'UI Development', 'Benutzer-Interface Entwicklung', 1, 0),
(3, 'CRM Integration', 'Integration mit CRM', 2, 0),
(4, 'Invoice Automation', 'Automatisierung von Rechnungsprozessen', 3, 0),
(5, 'Frontend Development', 'Frontend Entwicklung', 4, 0),
(6, 'Backend Development', 'Back-End Entwicklung', 1, 0),
(7, 'Data Migration', 'Datenmigration', 2, 0),
(8, 'Test Automation', 'Testautomatisierung', 3, 0),
(9, 'Payment Integration', 'Zahlungssystem-Integration', 4, 0),
(10, 'SEO Optimization', 'SEO Optimierung', 4, 0),
(11, 'Report Creation', 'Erstellen von Berichten', 2, 0),
(12, 'API Development', 'API-Entwicklung', 1, 0),
(13, 'Security Review', 'Sicherheitsüberprüfung', 3, 0),
(14, 'Role Management Design', 'Design von Benutzerrollen', 1, 0),
(15, 'Data Analysis and Reporting', 'Datenanalyse und Reporting', 3, 0),
(16, 'Setup Notifications', 'Einrichten von Benachrichtigungen', 4, 0),
(17, 'Code Review', 'Code-Review für bestehende Module', 1, 0),
(18, 'Query Optimization', 'Optimierung der Datenbank-Abfragen', 2, 0),
(19, 'Security Design', 'Design von Sicherheitsrichtlinien', 3, 0),
(20, 'Frontend Optimization', 'Frontend-Performance-Optimierung', 4, 0),
(21, 'Unit Testing', 'Erstellen von Unit-Tests', 1, 0),
(22, 'Mobile Optimization', 'Mobile Responsiveness verbessern', 5, 0),
(23, 'Cloud Migration', 'Cloud-Migration', 6, 0),
(24, 'DevOps Pipelines', 'Erstellen von DevOps-Pipelines', 7, 0),
(25, 'Project Documentation', 'Dokumentation des Projekts', 3, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `task_user`
--

CREATE TABLE `task_user` (
  `taskid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `starttime` timestamp NULL DEFAULT NULL,
  `endtime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `task_user`
--

INSERT INTO `task_user` (`taskid`, `userid`, `starttime`, `endtime`) VALUES
(1, 1, '2024-09-01 04:00:00', '2024-09-05 13:00:00'),
(1, 2, '2024-09-01 05:00:00', '2024-09-05 12:00:00'),
(1, 3, '2024-09-30 05:00:00', '2024-10-03 13:00:00'),
(1, 5, NULL, NULL),
(1, 6, '2024-09-28 05:00:00', '2024-09-30 13:00:00'),
(2, 7, '2024-09-29 05:00:00', '2024-09-30 13:00:00'),
(2, 9, '2024-09-20 04:00:00', '2024-09-24 13:00:00'),
(2, 10, '2024-09-28 05:00:00', '2024-09-30 13:00:00'),
(2, 11, '2024-10-12 05:00:00', '2024-10-15 13:00:00'),
(3, 1, '2024-10-01 05:00:00', '2024-10-05 13:00:00'),
(3, 3, '2024-09-02 04:00:00', '2024-09-06 13:00:00'),
(3, 4, '2024-10-04 05:00:00', '2024-10-08 13:00:00'),
(3, 6, '2024-09-30 05:00:00', '2024-10-03 13:00:00'),
(3, 18, NULL, NULL),
(4, 2, '2024-10-02 05:00:00', '2024-10-06 13:00:00'),
(4, 3, NULL, NULL),
(4, 4, '2024-09-03 04:00:00', '2024-09-07 13:00:00'),
(4, 8, '2024-09-28 05:00:00', '2024-09-30 13:00:00'),
(4, 10, '2024-09-21 04:00:00', '2024-09-25 13:00:00'),
(5, 5, '2024-10-05 05:00:00', '2024-10-09 13:00:00'),
(5, 6, '2024-10-03 05:00:00', '2024-10-05 13:00:00'),
(5, 8, '2024-10-01 05:00:00', '2024-10-04 13:00:00'),
(5, 10, '2024-10-04 05:00:00', '2024-10-06 13:00:00'),
(6, 11, '2024-09-22 04:00:00', '2024-09-26 13:00:00'),
(7, 3, '2024-09-29 05:00:00', '2024-10-03 13:00:00'),
(7, 5, '2024-09-04 04:00:00', '2024-09-08 13:00:00'),
(8, 6, '2024-10-06 05:00:00', '2024-10-09 13:00:00'),
(8, 9, '2024-09-29 05:00:00', '2024-10-01 13:00:00'),
(9, 12, '2024-09-28 05:00:00', '2024-09-30 13:00:00'),
(10, 1, '2024-09-13 04:00:00', '2024-09-17 13:00:00'),
(10, 3, '2024-09-14 04:00:00', '2024-09-17 13:00:00'),
(10, 7, '2024-09-18 04:00:00', '2024-09-22 13:00:00'),
(10, 13, NULL, NULL),
(11, 4, '2024-09-30 05:00:00', '2024-10-04 13:00:00'),
(11, 7, NULL, NULL),
(11, 8, '2024-09-19 04:00:00', '2024-09-23 13:00:00'),
(11, 11, NULL, NULL),
(11, 17, NULL, NULL),
(12, 1, NULL, NULL),
(12, 6, '2024-09-05 04:00:00', '2024-09-09 13:00:00'),
(12, 8, '2024-10-05 05:00:00', '2024-10-08 13:00:00'),
(12, 15, NULL, NULL),
(13, 9, '2024-10-02 05:00:00', '2024-10-05 13:00:00'),
(13, 15, NULL, NULL),
(14, 11, '2024-09-30 05:00:00', '2024-10-02 13:00:00'),
(15, 4, '2024-09-14 04:00:00', '2024-09-18 13:00:00'),
(15, 12, '2024-10-01 05:00:00', '2024-10-03 13:00:00'),
(15, 22, NULL, NULL),
(16, 5, '2024-09-28 05:00:00', '2024-10-01 13:00:00'),
(16, 7, '2024-09-30 05:00:00', '2024-10-02 13:00:00'),
(16, 13, NULL, NULL),
(17, 8, '2024-09-15 04:00:00', '2024-09-19 13:00:00'),
(17, 9, '2024-10-01 05:00:00', '2024-10-05 13:00:00'),
(18, 6, '2024-10-03 05:00:00', '2024-10-05 13:00:00'),
(18, 14, NULL, NULL),
(19, 7, '2024-10-04 05:00:00', '2024-10-06 13:00:00'),
(19, 10, '2024-10-03 05:00:00', '2024-10-05 13:00:00'),
(19, 11, '2024-10-09 05:00:00', '2024-10-11 13:00:00'),
(19, 12, '2024-10-10 05:00:00', '2024-10-12 13:00:00'),
(20, 9, '2024-10-13 05:00:00', '2024-10-15 13:00:00'),
(20, 11, '2024-10-06 05:00:00', '2024-10-08 13:00:00'),
(21, 8, '2024-10-12 05:00:00', '2024-10-15 13:00:00'),
(21, 13, '2024-10-07 05:00:00', '2024-10-09 13:00:00'),
(22, 12, '2024-10-07 05:00:00', '2024-10-09 13:00:00'),
(22, 20, '2024-10-09 05:00:00', '2024-10-12 13:00:00'),
(23, 14, '2024-10-05 05:00:00', '2024-10-09 13:00:00'),
(23, 21, '2024-09-17 04:00:00', '2024-09-21 13:00:00'),
(24, 15, '2024-09-16 04:00:00', '2024-09-20 13:00:00'),
(24, 19, '2024-10-06 05:00:00', '2024-10-08 13:00:00'),
(24, 22, '2024-10-02 05:00:00', '2024-10-06 13:00:00'),
(25, 16, '2024-10-09 05:00:00', '2024-10-12 13:00:00'),
(25, 17, '2024-10-06 05:00:00', '2024-10-08 13:00:00'),
(25, 18, '2024-10-10 05:00:00', '2024-10-13 13:00:00');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `benutzer`
--
ALTER TABLE `benutzer`
  ADD PRIMARY KEY (`userid`);

--
-- Indizes für die Tabelle `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`projectid`),
  ADD KEY `projectlead` (`projectlead`);

--
-- Indizes für die Tabelle `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`taskid`),
  ADD KEY `projectid` (`projectid`);

--
-- Indizes für die Tabelle `task_user`
--
ALTER TABLE `task_user`
  ADD PRIMARY KEY (`taskid`,`userid`),
  ADD KEY `userid` (`userid`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `benutzer`
--
ALTER TABLE `benutzer`
  MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT für Tabelle `project`
--
ALTER TABLE `project`
  MODIFY `projectid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT für Tabelle `task`
--
ALTER TABLE `task`
  MODIFY `taskid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `project_ibfk_1` FOREIGN KEY (`projectlead`) REFERENCES `benutzer` (`userid`);

--
-- Constraints der Tabelle `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_1` FOREIGN KEY (`projectid`) REFERENCES `project` (`projectid`);

--
-- Constraints der Tabelle `task_user`
--
ALTER TABLE `task_user`
  ADD CONSTRAINT `task_user_ibfk_1` FOREIGN KEY (`taskid`) REFERENCES `task` (`taskid`),
  ADD CONSTRAINT `task_user_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `benutzer` (`userid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
