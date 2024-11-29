DROP DATABASE school_db;
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

CREATE TABLE IF NOT EXISTS students (
    id BINARY(16) NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    is_verified BIT(1) NOT NULL,
    password VARCHAR(255),
    salt VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS teachers (
    id BINARY(16) NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    department VARCHAR(255),
    is_verified BIT(1) NOT NULL,
    password VARCHAR(255),
    salt VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS courses (
    id BINARY(16) NOT NULL PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    teacher_id BINARY(16),
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS enrollments (
    id BINARY(16) NOT NULL PRIMARY KEY,
    course_id BINARY(16) NOT NULL,
    student_id BINARY(16) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assignments (
    id BINARY(16) NOT NULL PRIMARY KEY,
    coefficient DOUBLE NOT NULL,
    description VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    course_id BINARY(16) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS grades (
    id BINARY(16) NOT NULL PRIMARY KEY,
    score DOUBLE,
    assignment_id BINARY(16) NOT NULL,
    enrollment_id BINARY(16) NOT NULL,
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
    id BINARY(16) NOT NULL PRIMARY KEY,
    status BIT(1) NOT NULL,
    student_id BINARY(16) DEFAULT NULL,
    teacher_id BINARY(16) DEFAULT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE,
    UNIQUE (student_id),
    UNIQUE (teacher_id)
);

CREATE TABLE IF NOT EXISTS admins (
    id BINARY(16) NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    is_verified BIT(1) NOT NULL,
    password VARCHAR(255),
    salt VARCHAR(255)
);

INSERT INTO students (id, email, firstname, lastname, date_of_birth, is_verified, password, salt) VALUES
    (UNHEX(REPLACE('82107de4-968c-46de-84ea-c75a443d42ba', '-', '')), 'jdoe@example.com', 'John', 'Doe', '2000-01-15', 1, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg=='),
    (UNHEX(REPLACE('3f17a404-6b04-4e49-bf47-5c97a9fef2a6', '-', '')), 'asmith@example.com', 'Alice', 'Smith', '1999-07-23', 1, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg=='),
    (UNHEX(REPLACE('39b3c9e6-e72f-4c75-a83f-c7c097a36eec', '-', '')), 'bwhite@example.com', 'Bob', 'White', '2001-03-10', 1, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg=='),
    (UNHEX(REPLACE('c9f6e1a8-2d8b-4f3c-9e0d-5b9f3d8a0c7f', '-', '')), 'unis_verified@example.com', 'Charlie', 'Brown', '2002-05-15', 0, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg==');

INSERT INTO teachers (id, email, firstname, lastname, department, is_verified, password, salt) VALUES
    (UNHEX(REPLACE('4a1c1185-3f8f-4e0d-a6f3-3c24155a7a7f', '-', '')), 'prof.jones@example.com', 'Emma', 'Jones', 'MATHEMATIQUES', 1, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg=='),
    (UNHEX(REPLACE('5bce19f6-9078-4e34-8d5b-b2e6537b2330', '-', '')), 'prof.brown@example.com', 'Liam', 'Brown', 'PHYSIQUE', 1, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg=='),
    (UNHEX(REPLACE('be3b84f3-1a9f-4c7e-9447-9ae484d8e0cc', '-', '')), 'prof.smith@example.com', 'Olivia', 'Smith', 'CHIMIE', 1, 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg==');

INSERT INTO courses (id, name, description, teacher_id) VALUES
    (UNHEX(REPLACE('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '-', '')), 'Mathematics', 'An introductory course to Mathematics', UNHEX(REPLACE('4a1c1185-3f8f-4e0d-a6f3-3c24155a7a7f', '-', ''))),
    (UNHEX(REPLACE('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '-', '')), 'Physics', 'Fundamentals of Physics', UNHEX(REPLACE('5bce19f6-9078-4e34-8d5b-b2e6537b2330', '-', ''))),
    (UNHEX(REPLACE('e6cfd17c-3071-4a8d-87ec-5b5c314f44f8', '-', '')), 'Chemistry', 'Introduction to Chemistry concepts', UNHEX(REPLACE('be3b84f3-1a9f-4c7e-9447-9ae484d8e0cc', '-', '')));

INSERT INTO enrollments (id, course_id, student_id) VALUES
    (UNHEX(REPLACE('a5b8f80b-0c8e-4c84-bb43-3e5b7b07f1c7', '-', '')), UNHEX(REPLACE('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '-', '')), UNHEX(REPLACE('82107de4-968c-46de-84ea-c75a443d42ba', '-', ''))), -- John enroll to Mathematics
    (UNHEX(REPLACE('1bcf76e1-08b3-4c12-b654-c7f5a0f6aa01', '-', '')), UNHEX(REPLACE('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '-', '')), UNHEX(REPLACE('3f17a404-6b04-4e49-bf47-5c97a9fef2a6', '-', ''))), -- Alice enroll to Physics
    (UNHEX(REPLACE('32d5a8c2-3b4e-42fb-9e56-2abdfb41b4d8', '-', '')), UNHEX(REPLACE('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '-', '')), UNHEX(REPLACE('82107de4-968c-46de-84ea-c75a443d42ba', '-', ''))), -- John enroll to Physics
    (UNHEX(REPLACE('b3cc2070-27e1-46dc-8a51-586942dfcab3', '-', '')), UNHEX(REPLACE('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '-', '')), UNHEX(REPLACE('39b3c9e6-e72f-4c75-a83f-c7c097a36eec', '-', ''))), -- Bob enroll to Physics
    (UNHEX(REPLACE('3f22c5eb-64f4-4c08-914f-99d737ddf586', '-', '')), UNHEX(REPLACE('e6cfd17c-3071-4a8d-87ec-5b5c314f44f8', '-', '')), UNHEX(REPLACE('39b3c9e6-e72f-4c75-a83f-c7c097a36eec', '-', ''))), -- Bob enroll to Chemistry
    (UNHEX(REPLACE('64ad7577-6d6d-43f0-b0c2-47da16009d1a', '-', '')), UNHEX(REPLACE('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '-', '')), UNHEX(REPLACE('3f17a404-6b04-4e49-bf47-5c97a9fef2a6', '-', ''))), -- Alice enroll to Mathematics
    (UNHEX(REPLACE('0233b482-8d18-4fb5-92eb-2ed6737931bf', '-', '')), UNHEX(REPLACE('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '-', '')), UNHEX(REPLACE('39b3c9e6-e72f-4c75-a83f-c7c097a36eec', '-', ''))); -- Bob enroll to Mathematics

INSERT INTO assignments (id, coefficient, description, title, course_id) VALUES
    (UNHEX(REPLACE('3a62f67d-1a2e-4af1-bcf4-7f9f4b5366e4', '-', '')), 1, 'Basic algebra problems covering addition and multiplication', 'Algebra Basics', UNHEX(REPLACE('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '-', ''))),
    (UNHEX(REPLACE('442ddaf5-3845-4ee6-b48e-071e0a9c2d9f', '-', '')), 1, 'Advanced problem set on linear equations and inequalities', 'Linear Equations', UNHEX(REPLACE('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '-', ''))),
    (UNHEX(REPLACE('543f03b8-2df0-4404-9840-80ce3eed9d31', '-', '')), 1, 'Introduction to Newton\'s laws of motion', 'Newton\'s Laws', UNHEX(REPLACE('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '-', ''))),
    (UNHEX(REPLACE('acb113f2-8b36-4b89-8391-8e9a78707744', '-', '')), 1, 'Problem set on kinetic and potential energy concepts', 'Energy Concepts', UNHEX(REPLACE('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '-', ''))),
    (UNHEX(REPLACE('96a786c7-3696-4938-a147-969e86df4efc', '-', '')), 1, 'Fundamental chemical reactions and balancing equations', 'Chemical Reactions', UNHEX(REPLACE('e6cfd17c-3071-4a8d-87ec-5b5c314f44f8', '-', ''))),
    (UNHEX(REPLACE('db8deae5-ea2d-4f6e-bb65-83b856ca90c1', '-', '')), 1, 'Study on atomic structure and periodic table elements', 'Atomic Structure', UNHEX(REPLACE('e6cfd17c-3071-4a8d-87ec-5b5c314f44f8', '-', '')));

INSERT INTO grades (id, score, assignment_id, enrollment_id) VALUES
    -- Grades for John in Mathematics
    (UNHEX(REPLACE('7d8b1e7c-12c4-4d41-b6f8-50d25f1ab1c4', '-', '')), 17, UNHEX(REPLACE('3a62f67d-1a2e-4af1-bcf4-7f9f4b5366e4', '-', '')), UNHEX(REPLACE('a5b8f80b-0c8e-4c84-bb43-3e5b7b07f1c7', '-', ''))), -- Algebra Basics
    (UNHEX(REPLACE('4b4f1eae-12d3-44a2-b6bc-6f91a6f0b87e', '-', '')), 18, UNHEX(REPLACE('442ddaf5-3845-4ee6-b48e-071e0a9c2d9f', '-', '')), UNHEX(REPLACE('a5b8f80b-0c8e-4c84-bb43-3e5b7b07f1c7', '-', ''))), -- Linear Equations

    -- Grades for Alice in Physics
    (UNHEX(REPLACE('a1b3e1d4-3f89-4b2f-9576-b4e8d0e9f8b2', '-', '')), 16, UNHEX(REPLACE('543f03b8-2df0-4404-9840-80ce3eed9d31', '-', '')), UNHEX(REPLACE('1bcf76e1-08b3-4c12-b654-c7f5a0f6aa01', '-', ''))), -- Newton's Laws
    (UNHEX(REPLACE('f3d2e0a6-3c4a-4f8e-95b3-d1b4e0f8c7a5', '-', '')), 5.5, UNHEX(REPLACE('acb113f2-8b36-4b89-8391-8e9a78707744', '-', '')), UNHEX(REPLACE('1bcf76e1-08b3-4c12-b654-c7f5a0f6aa01', '-', ''))), -- Energy Concepts

    -- Grades for John in Physics
    (UNHEX(REPLACE('929ed0f1-f7cc-44ce-8068-b24ff27d3620', '-', '')), 20, UNHEX(REPLACE('543f03b8-2df0-4404-9840-80ce3eed9d31', '-', '')), UNHEX(REPLACE('32d5a8c2-3b4e-42fb-9e56-2abdfb41b4d8', '-', ''))), -- Newton's Laws
    (UNHEX(REPLACE('97d1e336-bb8b-4527-a95e-1d0d55d4558f', '-', '')), 15, UNHEX(REPLACE('acb113f2-8b36-4b89-8391-8e9a78707744', '-', '')), UNHEX(REPLACE('32d5a8c2-3b4e-42fb-9e56-2abdfb41b4d8', '-', ''))), -- Energy Concepts

    -- Grades for Bob in Physics
    (UNHEX(REPLACE('18ac95f3-6f14-4eba-a47a-f6369135f609', '-', '')), 0, UNHEX(REPLACE('543f03b8-2df0-4404-9840-80ce3eed9d31', '-', '')), UNHEX(REPLACE('b3cc2070-27e1-46dc-8a51-586942dfcab3', '-', ''))), -- Newton's Laws
    (UNHEX(REPLACE('2a67b40c-7516-47fc-892a-07b8fffdb3ff', '-', '')), 4, UNHEX(REPLACE('acb113f2-8b36-4b89-8391-8e9a78707744', '-', '')), UNHEX(REPLACE('b3cc2070-27e1-46dc-8a51-586942dfcab3', '-', ''))), -- Energy Concepts

    -- Grades for Bob in Chemistry
    (UNHEX(REPLACE('c3d8a3f7-8f4c-4f72-98c4-5b8e0d2e9f1b', '-', '')), 10, UNHEX(REPLACE('96a786c7-3696-4938-a147-969e86df4efc', '-', '')), UNHEX(REPLACE('3f22c5eb-64f4-4c08-914f-99d737ddf586', '-', ''))), -- Chemical Reactions
    (UNHEX(REPLACE('d4e9b1f8-9f6d-4b8e-b5c7-6a9f1e0d8a3e', '-', '')), 10, UNHEX(REPLACE('db8deae5-ea2d-4f6e-bb65-83b856ca90c1', '-', '')), UNHEX(REPLACE('3f22c5eb-64f4-4c08-914f-99d737ddf586', '-', ''))), -- Atomic Structure

    -- Grades for Alice in Mathematics
    (UNHEX(REPLACE('e7b3f1a9-8c7f-4e2d-94b6-6f5d1c3b2a4d', '-', '')), 15.25, UNHEX(REPLACE('3a62f67d-1a2e-4af1-bcf4-7f9f4b5366e4', '-', '')), UNHEX(REPLACE('64ad7577-6d6d-43f0-b0c2-47da16009d1a', '-', ''))), -- Algebra Basics
    (UNHEX(REPLACE('b4c7d1a2-4f9e-4a3b-b9c6-7e8f0d4b3a5f', '-', '')), 16.5, UNHEX(REPLACE('442ddaf5-3845-4ee6-b48e-071e0a9c2d9f', '-', '')), UNHEX(REPLACE('64ad7577-6d6d-43f0-b0c2-47da16009d1a', '-', ''))), -- Linear Equations

    -- Grades for Bob in Mathematics
    (UNHEX(REPLACE('f1e3b5d8-7a9e-4f8b-9574-6b5d1f9e2c3a', '-', '')), 10, UNHEX(REPLACE('3a62f67d-1a2e-4af1-bcf4-7f9f4b5366e4', '-', '')), UNHEX(REPLACE('0233b482-8d18-4fb5-92eb-2ed6737931bf', '-', ''))), -- Algebra Basics
    (UNHEX(REPLACE('a9d7c1e5-4b8a-4e5b-9476-6a8e1d2b9c3f', '-', '')), 10, UNHEX(REPLACE('442ddaf5-3845-4ee6-b48e-071e0a9c2d9f', '-', '')), UNHEX(REPLACE('0233b482-8d18-4fb5-92eb-2ed6737931bf', '-', ''))); -- Linear Equations

INSERT INTO requests (id, status, student_id, teacher_id) VALUES
    (UNHEX(REPLACE('e50d89d7-21b5-45f3-a3f2-5c2a15fc61d4', '-', '')), 0, UNHEX(REPLACE('c9f6e1a8-2d8b-4f3c-9e0d-5b9f3d8a0c7f', '-', '')), NULL),
    (UNHEX(REPLACE('e0cbf4f4-a833-4246-ad51-9e77ef783313', '-', '')), 1, UNHEX(REPLACE('82107de4-968c-46de-84ea-c75a443d42ba', '-', '')), NULL), -- John Doe
    (UNHEX(REPLACE('d536248e-ab72-42a3-936d-d1622f129660', '-', '')), 1, UNHEX(REPLACE('3f17a404-6b04-4e49-bf47-5c97a9fef2a6', '-', '')), NULL), -- Alice Smith
    (UNHEX(REPLACE('d42a225c-71e4-4258-9bf7-d92c7c5c72ee', '-', '')), 1, UNHEX(REPLACE('39b3c9e6-e72f-4c75-a83f-c7c097a36eec', '-', '')), NULL), -- Bob White
    (UNHEX(REPLACE('d23c99ff-d088-43a6-8776-0df764a4cab4', '-', '')), 1, NULL, UNHEX(REPLACE('4a1c1185-3f8f-4e0d-a6f3-3c24155a7a7f', '-', ''))), -- Emma Jones
    (UNHEX(REPLACE('30f117a2-f774-423f-8dd1-92ffc458bd0b', '-', '')), 1, NULL, UNHEX(REPLACE('5bce19f6-9078-4e34-8d5b-b2e6537b2330', '-', ''))), -- Liam Brown
    (UNHEX(REPLACE('a5a42779-8394-43e5-b313-c2de24b4a8c2', '-', '')), 1, NULL, UNHEX(REPLACE('be3b84f3-1a9f-4c7e-9447-9ae484d8e0cc', '-', ''))); -- Olivia Smith

INSERT INTO admins (id, email, firstname, is_verified, lastname, password, salt) VALUES
    (UNHEX(REPLACE('e2437152-2648-41ac-bdd6-5f6f273839b0', '-', '')), 'admin@admin.com', 'admin', 1, 'admin', 'FsG84D3siq+GzQxIaKcFtw==', 'skiSARuzQLlM1E3RBsk4dg==');
