# Java Project Setup Guide

This guide will walk you through setting up your environment to run the Java project found at [this GitHub repository](https://github.com/AugustElvevold/2022.06.01-school-java-exam-quiz.git).

## Prerequisites

Before you begin, ensure you have the following tools installed:

- Git
- MySQL
- Java (JDK)
- IntelliJ Community Edition

If you're not sure whether you have these tools installed, you can check using the following commands in your terminal:

```bash
git --version
mysql --version
java -version
```


To check IntelliJ, simply try launching it. If you don't have these tools, follow the instructions below.

## Installation using Chocolatey (Choco)

If you don't have the above tools, I recommend using Chocolatey to make the installation process seamless. First, ensure you have Chocolatey installed. If not, [follow these installation instructions](https://chocolatey.org/install), or if you're not as concerned about security just run this command in windows powershell: 
```bash
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

Once Chocolatey is installed, run the following commands in your terminal:

```bash
choco install git
choco install mysql
choco install jdk
choco install intellijidea-community
```

## Setting up the Project

1. **Open IntelliJ and Import the Project** :

* Launch IntelliJ.
* Choose `Get from Version Control`.
* Enter the repository URL: `https://github.com/AugustElvevold/2022.06.01-school-java-exam-quiz.git`

1. **Setup MySQL User and Database** :
   Open the "Create user and database.sql" file from the project. There are two methods to set up the database:

   a.  **Using MySQL Workbench** :
    * If you have MySQL Workbench, you can open it and run the script there.

   b.  **Using powershell** :
    * If you dont have Workbench, you can run the commands from the file directly in the terminal.

```sql
   -- If new installation of MySQL and not using Workbench, 
   -- you might have to run these commands first:
   -- mysql -u root
   -- mysql -u root -p (if you've set a password)

   CREATE USER 'User1'@'localhost' IDENTIFIED by '123456';

   CREATE DATABASE IF NOT EXISTS `quizdb`;

   USE `quizdb`;

   GRANT ALL ON `quizdb` TO `User1`;

   -- exit;
```

## Note

This project uses Maven for dependency management. You don't need to manually set up any additional libraries; Maven will handle that for you.

---

Now, you should be ready to run and explore the Java project!
