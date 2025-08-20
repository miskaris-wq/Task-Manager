### Hexlet tests and linter status:
[![Actions Status](https://github.com/miskaris-wq/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/miskaris-wq/java-project-99/actions)

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=miskaris-wq_java-project-99)

# java-project-99: Hexlet Project - Tasks Manager

## Description

**Task Manager** is a Spring Boot-based task management system, similar to http://www.redmine.org/. This is the final, capstone project of the Hexlet Java development course, designed to demonstrate all key aspects of creating a modern web application using the Spring ecosystem.

The application allows users to create tasks, assign executors, track statuses, and organize work. Access to the system requires registration and authentication.

## Watch the working project

There is the link where you can see how the project works: https://java-task-manager-ru.hexlet.app/

## Watch the working project

A live demo of the application is available at: [https://java-project-99.onrender.com](https://java-project-72.onrender.com) (Loading may take up to a minute as it uses a free hosting tier).

## Project Goal

The main goal of the project is to consolidate all the acquired knowledge and demonstrate the ability to create full-fledged websites from scratch using Spring Boot and best practices:

*   **Spring Data JPA**: Using ORM to describe models and their relationships (One-to-Many, Many-to-Many)
*   **Spring MVC**: Implementing resource routing and CRUD operations
*   **Spring Security**: Configuring authentication and authorization mechanisms
*   **Data Filtering**: Implementing a clean filtering mechanism using Spring Data Specifications
*   **Spring Boot Build**: Configuring the project with Gradle and deployment setup
*   **Error Monitoring**: Integration with Rollbar for production monitoring

## Features

*   **User Management**: Registration and authentication with Spring Security
*   **Task Management**: Full CRUD operations for tasks with Spring Data JPA
*   **Role-based Access Control**: Authorization with Spring Security annotations
*   **Status Management**: Task status workflow management
*   **Label System**: Flexible tagging system with Many-to-Many relationships
*   **Advanced Filtering**: Filter tasks by multiple criteria using Specifications

## Technologies Used

*   **Spring Boot 3.x** - Main application framework
*   **Spring Data JPA** - Database access and ORM
*   **Spring Security** - Authentication and authorization
*   **Spring MVC** - Web layer implementation
*   **PostgreSQL** - Production database
*   **H2 Database** - Development and testing
*   **Gradle** - Build tool and dependency management
*   **Thymeleaf** - Template engine for server-side rendering
*   **Rollbar** - Error monitoring and tracking
*   **JUnit 5 & Spring Boot Test** - Testing framework

## How to Run

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/miskaris-wq/Task-Manager.git
    cd Task-Manager
    ```

2. **Build the project**:
    ```bash
    ./gradlew build
    ```

3. **Run the application**:
    ```bash
    ./gradlew bootRun
    ```
    Or using the generated JAR:
    ```bash
    java -jar build/libs/Task-Manager.jar
    ```

4. **Access the application**:
    Open your browser and navigate to: [http://localhost:8080](http://localhost:8080)

5. **Then you can see the login page**:

   ![](/photos/photo_1.png)

6. **After login, you can use the site to manage task, it's statuses and labels accessing the required section via the site menu** :
    Some examples of sections:

   ![](/photos/main_page.png)
   ![](/photos/tasks.png)
   ![](/photos/users.png)
   ![](/photos/labels.png)
   ![](/photos/statuses.png)
    
## Authors

*   miskaris-wq

## Acknowledgments

Thanks to Hexlet for the educational program and the provided platform for developing this project.