CREATE DATABASE studentDB;
USE studentDB;

CREATE TABLE students (
    student_id INT PRIMARY KEY,
    name VARCHAR(50),
    study_program VARCHAR(50)
);


INSERT INTO students (student_id, name, study_program)
VALUES
    (1, 'Karius', 'Biolog'),
    (2, 'anders', 'Fysikk'),
    (3, 'Bob', 'Informatikk');

