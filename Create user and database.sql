-- If new installation of MySQL and not workbench, run these commands first in order to test java project:
-- mysql -u root
-- mysql -u root -p (if already user)

CREATE USER 'User1'@'localhost' IDENTIFIED by '123456';

CREATE DATABASE IF NOT EXISTS `quizdb`;

USE `quizdb`;

GRANT ALL ON `quizdb` TO `User1`;

-- exit;
