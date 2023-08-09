CREATE TABLE users
(
    id           VARCHAR(255) PRIMARY KEY,
    user_name    VARCHAR(255),
    teacher_id   VARCHAR(255),
    student_id   VARCHAR(255)
);

CREATE TABLE teachers
(
    id      VARCHAR(255) PRIMARY KEY,
    name    VARCHAR(255),
    user_id  VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE students
(
    id      VARCHAR(255) PRIMARY KEY,
    name    VARCHAR(255),
    user_id  VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE courses
(
    id           VARCHAR(255) PRIMARY KEY,
    name         VARCHAR(255)
);

CREATE TABLE teacher_course
(
    teacher_id   VARCHAR(255) REFERENCES teachers (id),
    course_id    VARCHAR(255) REFERENCES courses (id),
    PRIMARY KEY (teacher_id, course_id)
);



