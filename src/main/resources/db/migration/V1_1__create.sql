-- CREATE SCHEMA IF NOT EXISTS migrations;

CREATE SEQUENCE users_id_seq
INCREMENT 1
START 1;

CREATE SEQUENCE customer_id_seq
INCREMENT 1
START 1;

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'),
    username character varying(255),
    password character varying(255),
    created_by character varying(255),
    last_modified_by character varying(255),
    last_modified_date timestamp without time zone,
    created_date timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT username_unique UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS customer
(
    id bigint NOT NULL DEFAULT nextval('customer_id_seq'),
    first_name character varying(255),
    last_name character varying(255),
    email character varying(255),
    photo_url character varying(255),
    created_by character varying(255),
    created_date timestamp without time zone NOT NULL,
    last_modified_by character varying(255),
    last_modified_date timestamp without time zone,
    CONSTRAINT customer_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles
(
    id integer NOT NULL,
    name character varying(20),
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

