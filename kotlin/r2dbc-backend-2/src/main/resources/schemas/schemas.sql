CREATE TABLE IF NOT EXISTS teachers
(
    id      VARCHAR(255) PRIMARY KEY,
    version VARCHAR(255),
    name    VARCHAR(255),
    events  INT,
    user_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS students
(
    id      VARCHAR(255) PRIMARY KEY,
    version VARCHAR(255),
    name    VARCHAR(255),
    events  INT,
    user_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id           VARCHAR(255) PRIMARY KEY,
    version      VARCHAR(255),
    user_name    VARCHAR(255),
    password     VARCHAR(255),
    teacher_id   VARCHAR(255),
    student_id   VARCHAR(255),
    events       INT,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    email        VARCHAR(255),
    gender       VARCHAR(10),
    phone_number VARCHAR(20),
    FOREIGN KEY (teacher_id) REFERENCES teachers (id),
    FOREIGN KEY (student_id) REFERENCES students (id)
);

CREATE TABLE IF NOT EXISTS courses
(
    id          VARCHAR(255) PRIMARY KEY,
    version     VARCHAR(255),
    name        VARCHAR(255),
    description VARCHAR(255),
    events      INT,
    school_name VARCHAR(255),
    status      VARCHAR(20),
    start_date  DATE,
    end_date    DATE
);

CREATE TABLE IF NOT EXISTS teacher_course_relations
(
    id                             VARCHAR(255) PRIMARY KEY,
    version                        VARCHAR(255),
    teacher_id                     VARCHAR(255) REFERENCES teachers (id),
    course_id                      VARCHAR(255) REFERENCES courses (id),
    teacher_course_relation_status VARCHAR(20) NOT NULL,
    events                         INT
);

CREATE TABLE IF NOT EXISTS student_course_relations
(
    id                             VARCHAR(255) PRIMARY KEY,
    version                        VARCHAR(255),
    student_id                     VARCHAR(255) REFERENCES students (id),
    course_id                      VARCHAR(255) REFERENCES courses (id),
    student_course_relation_status VARCHAR(20) NOT NULL,
    events                         INT
);

CREATE TABLE IF NOT EXISTS units
(
    id          VARCHAR(255) PRIMARY KEY,
    version     VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    events      INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS unit_course_relations
(
    id        VARCHAR(255) PRIMARY KEY,
    version   VARCHAR(255) NOT NULL,
    unit_id   VARCHAR(255) NOT NULL REFERENCES units (id),
    course_id VARCHAR(255) NOT NULL REFERENCES courses (id),
    name      VARCHAR(255),
    events    INT
);

CREATE TABLE IF NOT EXISTS assignments
(
    id                 VARCHAR(255) PRIMARY KEY,
    version            VARCHAR(255),
    name               VARCHAR(255),
    assignment_type    VARCHAR(255),
    unit_id            VARCHAR(255) REFERENCES units (id),
    assignment_type_id VARCHAR(255),
    events             INT,
    created_at         DATE
);


CREATE TABLE IF NOT EXISTS materials
(
    id            VARCHAR(255) PRIMARY KEY,
    version       VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    material_type VARCHAR(50)  NOT NULL,
    description   TEXT,
    uri           TEXT         NOT NULL,
    uploaded      BOOLEAN      NOT NULL,
    events        INT          NOT NULL
);

CREATE TABLE tags
(
    id      VARCHAR(255) PRIMARY KEY,
    version VARCHAR(255),
    name    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS material_relations
(
    id            VARCHAR(255) PRIMARY KEY,
    material_id   VARCHAR(255) REFERENCES materials (id),
    course_id     VARCHAR(255) REFERENCES courses (id),
    unit_id       VARCHAR(255) REFERENCES units (id),
    assignment_id VARCHAR(255) REFERENCES Assignments (id),
    tag_id        VARCHAR(255) REFERENCES tags (id),
    relation_type VARCHAR(50) NOT NULL,
    events        INT
);


