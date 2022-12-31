CREATE TABLE users(
    id int not null AUTO_INCREMENT,
    name VARCHAR(255),
    surname VARCHAR(255),
    username VARCHAR(255),
    email VARCHAR(255) unique,
    password VARCHAR(255),
    phone_number VARCHAR(255),
    address VARCHAR(255),
    profile_image VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
);


CREATE TABLE chat_groups(
    id int not null AUTO_INCREMENT,
    name VARCHAR(255),
    owner int not null,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    FOREIGN KEY (owner) REFERENCES users(id)
);





CREATE TABLE groups_members(
    chat_group int NOT NULL,
    member int NOT NULL,
    
    PRIMARY KEY (chat_group, member),
    FOREIGN KEY (chat_group) REFERENCES chat_groups(id),
    FOREIGN KEY (member) REFERENCES users(id)
);






CREATE TABLE messages(
    id int NOT NULL AUTO_INCREMENT,
    sender VARCHAR(255) NOT NULL,
    /*chat_group int NOT NULL,*/
    message VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)/*,
    FOREIGN KEY (sender) REFERENCES users(id),
    FOREIGN KEY (chat_group) REFERENCES chat_groups(id)*/
);