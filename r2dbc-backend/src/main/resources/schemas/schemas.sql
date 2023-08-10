CREATE TABLE IF NOT EXISTS users
(
    id           VARCHAR(255) PRIMARY KEY,
    user_name    VARCHAR(255),
    teacher_id   VARCHAR(255),
    student_id   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS teacher
(
    id      VARCHAR(255) PRIMARY KEY,
    name    VARCHAR(255),
    user_id  VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS student
(
    id      VARCHAR(255) PRIMARY KEY,
    name    VARCHAR(255),
    user_id  VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS course
(
    id           VARCHAR(255) PRIMARY KEY,
    name         VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS teacher_course
(
    course_id    VARCHAR(255) REFERENCES course (id),
    teacher_id   VARCHAR(255) REFERENCES teacher (id),
    PRIMARY KEY (course_id, teacher_id)
);



