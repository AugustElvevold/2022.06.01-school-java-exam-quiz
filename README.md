# Java Project Quiz

This Java application, originally developed as a part of an object-oriented programming exam, offers a terminal-based quiz experience. It connects to a local MySQL server to fetch quiz data. Upon launching, the application prompts users to input their usernames, which is later used for leaderboards. Users can choose between two quizzes: "Rocket League" (binary choice) and "Minecraft" (multiple choice). As each quiz commences, a timer begins. At the end, users can view a leaderboard, sorted by score and then time. The application offers other intriguing features, all accessible via terminal menus.

## Getting Started

This guide will help you set up your development environment to run the Java Quiz Project.

### Prerequisites

Ensure you have the following tools installed:

- Git
- MySQL
- Java (JDK)
- IntelliJ Community Edition

Verify installations using the following commands in terminal:

```bash
git --version
mysql --version
java -version
```
For IntelliJ, simply attempt to launch it.

### Installing Dependencies

If you lack any of the aforementioned tools, Chocolatey can streamline the installation process:
1. **Install Chocolatey** :
   If Chocolatey isn't already installed, [follow the official guide](https://chocolatey.org/install).
   Alternatively, you can execute this command in Windows PowerShell:
   ```bash
   Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
   ```

2. **Install Prerequisites**:
   ```bash
   choco install git
   choco install mysql
   choco install jdk
   choco install intellijidea-community
   ```

### Project Setup

#### Open IntelliJ and import the Project:

1. Launch IntelliJ.
2. Choose `Get from Version Control`.
3. Enter the repository URL: `https://github.com/AugustElvevold/2022.06.01-school-java-exam-quiz.git`

#### Configure MySQL:
Refer to the "Create user and database.sql" file included in the project. You have two methods:

a.  **Using MySQL Workbench** :
   * If you have MySQL Workbench, you can run the script there.

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

For the exam submission, the project was delivered using vanilla Java without the assistance of any dependency management tools. However, to simplify the setup process for users setting up the project on their personal machines, Maven has been integrated. This ensures an easier and more seamless experience when managing and setting up dependencies.

---

You're all set to dive into the Java Quiz Project! Enjoy quizzing!
