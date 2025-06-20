# Warehouse Management System

## Overview

Warehouse Management System is a Spring Boot-based application designed to manage employees in a warehouse environment. It supports functionalities such as employee management, shift tracking, and role-specific operations for delivery drivers and warehouse employees.

## Features

- **Employee Management**: Add, update, and retrieve employee details.
- **Role Assignment**: Assign employees as delivery drivers or warehouse workers.
- **Shift Tracking**: Start and end shifts for employees.
- **Break Management**: Manage breaks for delivery drivers with automated status updates.

## Technologies Used

- **Java**: Core programming language.
- **Spring Boot**: Framework for building the application.
- **Maven**: Dependency management and build tool.
- **Lombok**: Simplifies boilerplate code.

## Project Structure

- `src/main/java/edu/pja/mas/warehouse/`: Contains the main application code.
  - `service/`: Business logic and service layer.
  - `repository/`: Data access layer for interacting with the database.
  - `dto/`: Data Transfer Objects for API communication.
  - `entity/`: Entity classes representing database tables.
  - `enums/`: Enumerations for predefined values like employee types and driver statuses.
