CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

INSERT INTO students (id, email, firstname, lastname, date_of_birth) VALUES
    ('82107de4-968c-46de-84ea-c75a443d42ba', 'jdoe@example.com', 'John', 'Doe', '2000-01-15'),
    ('3f17a404-6b04-4e49-bf47-5c97a9fef2a6', 'asmith@example.com', 'Alice', 'Smith', '1999-07-23'),
    ('39b3c9e6-e72f-4c75-a83f-c7c097a36eec', 'bwhite@example.com', 'Bob', 'White', '2001-03-10');

INSERT INTO teachers (id, email, firstname, lastname) VALUES
    ('4a1c1185-3f8f-4e0d-a6f3-3c24155a7a7f', 'prof.jones@example.com', 'Emma', 'Jones'),
    ('5bce19f6-9078-4e34-8d5b-b2e6537b2330', 'prof.brown@example.com', 'Liam', 'Brown'),
    ('be3b84f3-1a9f-4c7e-9447-9ae484d8e0cc', 'prof.smith@example.com', 'Olivia', 'Smith');

INSERT INTO courses (id, name, description, teacher_id) VALUES
    ('1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', 'Mathematics', 'An introductory course to Mathematics', '4a1c1185-3f8f-4e0d-a6f3-3c24155a7a7f'),
    ('cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', 'Physics', 'Fundamentals of Physics', '5bce19f6-9078-4e34-8d5b-b2e6537b2330'),
    ('e6cfd17c-3071-4a8d-87ec-5b5c314f44f8', 'Chemistry', 'Introduction to Chemistry concepts', 'be3b84f3-1a9f-4c7e-9447-9ae484d8e0cc');

INSERT INTO enrollments (id, course_id, student_id) VALUES
    ('a5b8f80b-0c8e-4c84-bb43-3e5b7b07f1c7', '1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '82107de4-968c-46de-84ea-c75a443d42ba'), -- John inscrite à Mathematics
    ('1bcf76e1-08b3-4c12-b654-c7f5a0f6aa01', 'cc5e0d3a-1cb3-4e1f-bc02-b5b646f6be68', '3f17a404-6b04-4e49-bf47-5c97a9fef2a6'), -- Alice inscrit à Physics
    ('3f22c5eb-64f4-4c08-914f-99d737ddf586', 'e6cfd17c-3071-4a8d-87ec-5b5c314f44f8', '39b3c9e6-e72f-4c75-a83f-c7c097a36eec'), -- Bob inscrite à Chemistry
    ('64ad7577-6d6d-43f0-b0c2-47da16009d1a', '1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '3f17a404-6b04-4e49-bf47-5c97a9fef2a6'), -- Alice inscrit à Mathematics
    ('0233b482-8d18-4fb5-92eb-2ed6737931bf', '1e9b7b9b-3c76-4f2e-b8b0-8b47b8a5e7a8', '39b3c9e6-e72f-4c75-a83f-c7c097a36eec'); -- Bob inscrite à Mathematics
