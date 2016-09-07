CREATE USER docker WITH SUPERUSER PASSWORD 'docker';
CREATE DATABASE docker;
\c docker

CREATE TABLE awesomeTable (
	id INT PRIMARY KEY NOT NULL,
	value TEXT NOT NULL
);

INSERT INTO awesomeTable VALUES (1, 'Hello'), (2, 'World');
