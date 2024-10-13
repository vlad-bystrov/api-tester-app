CREATE DATABASE IF NOT EXISTS `students_db`;
USE `students_db`;

DROP TABLE IF EXISTS `students`;

CREATE TABLE `students`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
