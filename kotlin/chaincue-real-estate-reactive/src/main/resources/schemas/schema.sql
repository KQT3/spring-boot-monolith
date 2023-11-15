CREATE TABLE IF NOT EXISTS broker
(
    id           VARCHAR(255) PRIMARY KEY,
    name         VARCHAR(255),
    phone_number VARCHAR(20),
    email        VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS country
(
    id   VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS house
(
    id           VARCHAR(255) PRIMARY KEY,
    title        VARCHAR(255),
    description  VARCHAR(255),
    location     VARCHAR(255),
    number_rooms INT,
    beds         INT,
    price        VARCHAR(50),
    src          VARCHAR(255) NOT NULL,
    sold         BOOLEAN,
    house_types  VARCHAR(50)  NOT NULL,
    broker_id    VARCHAR(36),
    created      TIMESTAMP,
    CONSTRAINT FK_BROKER FOREIGN KEY (broker_id) REFERENCES Broker (id)
);

CREATE TABLE IF NOT EXISTS house_image
(
    id  VARCHAR(255) PRIMARY KEY,
    url VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS house_images
(
    id       VARCHAR(255) PRIMARY KEY,
    house_id VARCHAR(255),
    image_id VARCHAR(255),
    CONSTRAINT fk_house_images_house FOREIGN KEY (house_id) REFERENCES house (id) ON DELETE CASCADE,
    CONSTRAINT fk_house_images_image FOREIGN KEY (image_id) REFERENCES house_image (id) ON DELETE CASCADE
);
