-- =============================================================
-- Insurance Management System — Database Schema & Initial Data
-- Compatible with MySQL 8.0, 8.4 LTS, and 9.x
-- =============================================================

CREATE DATABASE IF NOT EXISTS `project`;
USE `project`;

-- 1. Drop existing tables if present (respecting foreign keys)
DROP TABLE IF EXISTS `emps_address`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `emps`;
DROP TABLE IF EXISTS `policy`;
DROP TABLE IF EXISTS `user`;

-- 2. Address Entity Table
CREATE TABLE IF NOT EXISTS `address` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255) DEFAULT NULL,
  `last_name` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Employee Entity Table
CREATE TABLE IF NOT EXISTS `emps` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `city` VARCHAR(255) DEFAULT NULL,
  `email_id` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Employee Address Mapping Table
CREATE TABLE IF NOT EXISTS `emps_address` (
  `employee_id` INT NOT NULL,
  `address_id` INT NOT NULL,
  UNIQUE KEY `UK_address_id` (`address_id`),
  KEY `FK_employee_id` (`employee_id`),
  CONSTRAINT `FK_emp_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FK_emp_id` FOREIGN KEY (`employee_id`) REFERENCES `emps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Policy Entity Table
CREATE TABLE IF NOT EXISTS `policy` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `policynumber` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(255) DEFAULT NULL,
  `premiumamount` VARCHAR(255) DEFAULT NULL,
  `sumassured` VARCHAR(255) DEFAULT NULL,
  `coveragetype` VARCHAR(255) DEFAULT NULL,
  `status` VARCHAR(255) DEFAULT 'Active',
  `claimnumber` VARCHAR(255) DEFAULT NULL,
  `claimamount` VARCHAR(255) DEFAULT NULL,
  `claimstatus` VARCHAR(255) DEFAULT NULL,
  `claimremarks` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- Seed Data: Active In-Force Insurance Policies & Adjudicated Claims
INSERT INTO `policy` (`id`, `policynumber`, `name`, `email`, `premiumamount`, `sumassured`, `coveragetype`, `status`, `claimnumber`, `claimamount`, `claimstatus`, `claimremarks`) VALUES
(2, 'POL-1002', 'Alex Mercer', 'alex@example.com', '25000', '1500000', 'Whole Life Comprehensive Protection', 'Active', 'CLM-502', NULL, 'SETTLED', 'Adjudicated and approved for disbursement'),
(3, 'POL-9921', 'Vikas Indla', 'indlavikas57@gmail.com', '18500', '2500000', 'Pure Term Life Assurance', 'Active', 'CLM-104', NULL, 'SETTLED', 'Approved under standard settlement guidelines'),
(4, 'POL-3341', 'Priya Sharma', 'priya.sharma@outlook.com', '14200', '1000000', 'Endowment with Guaranteed Savings', 'Active', 'CLM-209', NULL, 'SETTLED', 'Settled via electronic transfer'),
(5, 'POL-5582', 'Rajesh Verma', 'rajesh.verma@techmail.in', '32000', '5000000', 'Term Life with Critical Illness Rider', 'Active', 'CLM-311', NULL, 'UNDER_REVIEW', 'Underwriting documentation under review'),
(6, 'POL-7719', 'Ananya Iyer', 'ananya.iyer@fintechcorp.com', '21500', '2000000', 'Pure Term Life Assurance', 'Active', 'CLM-902', NULL, 'REJECTED', 'Claim rejected: non-disclosure of pre-existing condition');

-- 6. User Entity Table
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) DEFAULT NULL,
  `userpwd` VARCHAR(255) DEFAULT NULL,
  `useremail` VARCHAR(255) DEFAULT NULL,
  `firstname` VARCHAR(255) DEFAULT NULL,
  `lastname` VARCHAR(255) DEFAULT NULL,
  `gender` CHAR(1) DEFAULT NULL,
  `userage` INT DEFAULT NULL,
  `city` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Seed Data: User Login Credentials
INSERT INTO `user` (`id`, `username`, `userpwd`, `useremail`, `firstname`, `lastname`, `gender`, `userage`, `city`) VALUES
(1, 'jeevankulkarni10@gmail.com', '12345678', 'jeevankulkarni10@gmail.com', 'Jeevan', 'Kulkarni', 'M', 30, 'Pune'),
(2, 'indlavikas57@gmail.com', '12345678', 'indlavikas57@gmail.com', 'Omvikas', 'Indla', 'M', 22, 'Pune');
