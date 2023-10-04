CREATE USER 'User1'@'localhost' IDENTIFIED by '123456';

CREATE DATABASE IF NOT EXISTS `quizdb`;

USE `quizdb`;

GRANT ALL ON `quizdb` TO `User1`;