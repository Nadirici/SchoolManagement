# School Management Application

A comprehensive system for managing students, teachers, courses, enrollments, and grades, developed in Java using the Spring Boot framework with Hibernate for ORM and MySQL as the database.

## Features

- CRUD operations for **Students**, **Teachers**, **Courses**, **Enrollments**, and **Grades**.
- Unique **UUID** identifiers for each entity.
- Enroll and disenroll students in courses.
- Manage grades for students in each enrolled course.
- RESTful API design with standardized JSON responses.
- Error handling and validation. **(TODO)**

## Architecture

- **Backend**: Spring Boot with Spring Data JPA for managing entities and repositories.
- **Database**: MySQL database with tables for each entity.
- **DTOs**: Data Transfer Objects are used for encapsulating request/response data.
- **Mapping**: ModelMapper for mapping between entities and DTOs.

## Technologies

- **Java** (JDK 23.0.1 or higher)
- **Spring Boot** (3.3.5+)
- **Spring Data JPA** and **Hibernate**
- **MySQL** Database
- **ModelMapper** for entity-DTO mapping
- **Lombok** (optional for getters, setters, and constructors)
- **Maven** for dependency management

### Database Schema

| Entity       | Description                 |
|--------------|-----------------------------|
| `Student`    | Manages student data        |
| `Teacher`    | Manages teacher data        |
| `Course`     | Manages course details      |
| `Enrollment` | Tracks student-course links |
| `Grade`      | Records grades per enrollment |

<img src="https://github.com/user-attachments/assets/553d5b8a-6103-4e91-a004-f25ab55bfc43" alt="MCDSchoolManager" width="600"/>

## 🗃️ Sample Data for Testing

For convenience, a `sample-data.sql` file is included in the project. This file contains pre-configured test data that can be used to populate the database quickly. By loading this data, you can experiment with and test the various API endpoints without needing to manually insert records.

- **Usage:** Import the sample-data.sql file into your database. This will create test records for `Student`, `Course` and `Enrollment` entities.
- **Objective:** Quickly set up a testing environment to validate API requests and application behavior.
This file is especially helpful for trying out the enrollment and student-course relationship queries, as well as testing new API functionalities as they are added.

## API Endpoints

[View all the endpoints on Postman](https://web.postman.co/workspace/1dcef16f-204d-4823-ba5f-c8e32e5ffaed/overview)

### Students

- `GET /students` - List all students
- `POST /students` - Add a new student
  - Request Body : `StudentDTO`
- `GET /students/{id}` - Get a specific student by ID
- `DELETE /students/{id}` - Delete a student
- `GET /students/{id}/courses` - Retrieves all courses a student is enrolled in
- `PUT /students/{id}` - Updates information for a specific student **(TODO)**
    - Request Body : `StudentDTO`

### Teachers

- `GET /teachers` - List all teachers
- `POST /teachers` - Add a new teacher
  - Request Body : `TeacherDTO`
- `GET /teachers/{id}` - Get a specific teacher by ID
- `DELETE /teachers/{id}` - Delete a teacher
- `GET /teachers/{id}/courses` - Retrieves a list of courses taught by a specific teacher **(TODO)**
- `PUT /teachers/{id}` - Updates information for a specific teacher **(TODO)**
    - Request Body : `TeacherDTO`

### Courses

- `GET /courses` - List all courses
- `POST /courses` - Add a new course
    - Request Body : `CourseDTO`
- `GET /courses/{id}` - Get a specific course by ID
- `DELETE /courses/{id}` - Remove a course
- `GET /courses/{id}/students` - Retrieves a list of students enrolled in a specific course.
- `PUT /courses/{id}` - Updates information for a specific course **(TODO)**
  - Request Body : `CourseDTO`

### Enrollments

- `POST /enrollments` - Enrolls a student in a course
  - Request Body : `{ "studentId": UUID, "courseId": UUID }`
- `DELETE /enrollments/{id}` - Unenrolls a student from a course
- `GET /enrollments` - Retrieves a list of all enrollments
- `GET /enrollments/{id}` - Retrieves a specific enrollment by ID **(TODO)**
- `GET /enrollments/{id}` - Retrieves all grades for a specific enrollment **(TODO)**

### Grades

- `POST /grades` - Adds a grade to a student’s enrollment
  - Request Body : `{ "enrollmentId": UUID, "value": double }`
- `DELETE /grades/{id}` - Deletes a specific grade by ID
- `PUT /grades/{id}` - Updates a grade **(TODO)**
    - Request Body : `{ "value": double }`

## 📈 Development Roadmap

This roadmap outlines the next steps for expanding and enhancing the School Management System. The following features and improvements will make the system more functional, secure, and user-friendly.

### 1. Complete Missing Endpoints
- **Task**: Implement the remaining endpoints required for full CRUD (Create, Read, Update, Delete) functionality for each entity, including `Student`, `Course`, `Enrollment`, and `Grade`.
- **Objective**: Ensure all necessary operations are supported and fully tested.

### 2. Develop Controllers and Views
- **Task**: Create MVC controllers using Spring to handle the frontend interactions.
- **Views**: Implement JSP views to display data for `Student`, `Course`, `Enrollment`, and `Grade`.
- **Objective**: Provide a simple user interface that interacts with the API, displaying and managing data.

### 3. API Improvements
- **Validation**: Add validation to DTOs and API endpoints to enforce data integrity and consistency.
- **Error Handling**: Implement custom error messages and HTTP status codes for better API response clarity.
- **Objective**: Ensure the API is robust, providing meaningful feedback on user input and system status.

### 4. Add Security and Roles
- **Task**: Introduce role-based access control (RBAC) using Spring Security, with roles `ADMIN`, `STUDENT`, and `TEACHER`.
- **Features**:
    - Protect sensitive endpoints, allowing access based on user roles.
    - Implement login/logout functionality.
- **Objective**: Secure the API and frontend, ensuring only authorized users can access or modify data.
  
### 5. Additional Enhancements (Optional)
- **Pagination and Filtering**: Add pagination to list endpoints for better scalability.
- **Implement Testing**: Guarantee the quality and reliability of the application’s core functionalities. (Unit Tests + Integration Tests)
- **Logging and Monitoring**: Implement logging for activity tracking and error monitoring.
- **Documentation**: Provide detailed documentation of the API endpoints and data models.
