-- liquibase formatted sql

-- changeset zetta-dev:1
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
    due_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    category_id BIGINT
);

CREATE TABLE subtasks (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
    task_id BIGINT NOT NULL
);

CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL
);

CREATE TABLE task_tags (
    task_id BIGINT NOT NULL, -- BIGINT
    tag_id BIGINT NOT NULL, -- BIGINT
    PRIMARY KEY (task_id, tag_id)
);