CREATE USER docker WITH SUPERUSER PASSWORD 'docker';
CREATE DATABASE docker;
\c docker

CREATE TABLE awesomeTable (
	id serial PRIMARY KEY,
	message text NOT NULL
);

INSERT INTO awesomeTable (message) VALUES ('Hello'), ('World');
