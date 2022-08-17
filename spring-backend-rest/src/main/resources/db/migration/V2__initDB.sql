CREATE TABLE IF NOT EXISTS vets (
  id SERIAL,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  CONSTRAINT pk_vets PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_vets_last_name ON vets (last_name);

CREATE SEQUENCE IF NOT EXISTS vets_id_seq START WITH  100;
ALTER SEQUENCE vets_id_seq RESTART WITH 100;


CREATE TABLE IF NOT EXISTS specialties (
  id SERIAL,
  name VARCHAR(80),
  CONSTRAINT pk_specialties PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_specialties_name ON specialties (name);

CREATE SEQUENCE IF NOT EXISTS specialties_id_seq START  WITH   100;
ALTER SEQUENCE specialties_id_seq RESTART WITH 100;


CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id INT NOT NULL,
  specialty_id INT NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets(id),
  FOREIGN KEY (specialty_id) REFERENCES specialties(id),
  CONSTRAINT unique_ids UNIQUE (vet_id,specialty_id)
);



CREATE TABLE IF NOT EXISTS types (
  id SERIAL,
  name VARCHAR(80),
  CONSTRAINT pk_types PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_types_name ON types (name);

CREATE SEQUENCE IF NOT EXISTS types_id_seq START  WITH   100;
ALTER SEQUENCE types_id_seq RESTART WITH 100;

CREATE TABLE IF NOT EXISTS owners (
  id SERIAL,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20),
  CONSTRAINT pk_owners PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_owners_last_name ON owners (last_name);

CREATE SEQUENCE IF NOT EXISTS owners_id_seq START  WITH   100;
ALTER SEQUENCE owners_id_seq RESTART WITH 100;


CREATE TABLE IF NOT EXISTS pets (
  id SERIAL,
  name VARCHAR(30),
  birth_date DATE,
  type_id INT NOT NULL,
  owner_id INT NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES owners(id),
  FOREIGN KEY (type_id) REFERENCES types(id),
  CONSTRAINT pk_pets PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_pets_name ON pets (name);

CREATE SEQUENCE IF NOT EXISTS pets_id_seq START  WITH    100;
ALTER SEQUENCE pets_id_seq RESTART WITH 100;


CREATE TABLE IF NOT EXISTS visits (
  id SERIAL,
  pet_id INT NOT NULL,
  visit_date DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets(id),
  CONSTRAINT pk_visits PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS visits_id_seq START WITH 100;
ALTER SEQUENCE visits_id_seq RESTART WITH 100;

CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(20) NOT NULL ,
  password VARCHAR(20) NOT NULL ,
  enabled boolean NOT NULL DEFAULT true ,
  CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS roles (
  id SERIAL,
  username varchar(20) NOT NULL,
  role varchar(20) NOT NULL,
  CONSTRAINT pk_roles PRIMARY KEY (id),
  FOREIGN KEY (username) REFERENCES users (username)
);

ALTER TABLE roles ADD CONSTRAINT uni_username_role UNIQUE (role,username);

CREATE SEQUENCE IF NOT EXISTS roles_id_seq START WITH 100;
ALTER SEQUENCE roles_id_seq RESTART WITH 100;

--BATCH Tables

CREATE SEQUENCE IF NOT EXISTS batch_step_execution_seq START WITH 100;
CREATE SEQUENCE IF NOT EXISTS batch_job_execution_seq START WITH 100;
CREATE SEQUENCE IF NOT EXISTS batch_job_seq START WITH 100;

ALTER SEQUENCE batch_step_execution_seq RESTART WITH 100;
ALTER SEQUENCE batch_job_execution_seq RESTART WITH 100;
ALTER SEQUENCE batch_job_seq RESTART WITH 100;

CREATE TABLE IF NOT EXISTS batch_job_instance (
  job_instance_id BIGINT,
  version BIGINT,
  job_name VARCHAR(100) NOT NULL,
  job_key VARCHAR(2500),
  CONSTRAINT pk_batch_job_instance PRIMARY KEY (job_instance_id)
);

CREATE TABLE IF NOT EXISTS batch_job_execution (
  job_execution_id BIGINT,
  version BIGINT,
  job_configuration_location VARCHAR(2000),
  job_instance_id BIGINT NOT NULL,
  create_time TIMESTAMP NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  status VARCHAR(10),  
  exit_code VARCHAR(20),
  exit_message VARCHAR(2500),  
  last_updated TIMESTAMP,  
  FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance(job_instance_id),
  CONSTRAINT pk_batch_job_execution PRIMARY KEY (job_execution_id)
);

CREATE TABLE IF NOT EXISTS batch_job_execution_params (
  job_execution_id BIGINT NOT NULL,
  type_cd VARCHAR(6) NOT NULL,
  key_name VARCHAR(100) NOT NULL,
  string_val VARCHAR(250),
  start_time TIMESTAMP,
  date_val TIMESTAMP,
  long_val BIGINT,  
  double_val DOUBLE PRECISION,  
  identifying VARCHAR(1) NOT NULL, 
  FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id)
);

CREATE TABLE IF NOT EXISTS batch_job_execution_context (
  job_execution_id BIGINT NOT NULL,
  short_context VARCHAR(2500) NOT NULL,
  serialized_context TEXT,
  FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id),
  CONSTRAINT pk_batch_job_execution_context PRIMARY KEY (job_execution_id)
);

CREATE TABLE IF NOT EXISTS batch_step_execution (
  step_execution_id BIGINT NOT NULL,
  version BIGINT NOT NULL,
  step_name VARCHAR(100) NOT NULL,
  job_execution_id BIGINT NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time timestamp,
  status VARCHAR(10),
  commit_count BIGINT,
  read_count BIGINT,
  filter_count BIGINT,
  write_count BIGINT,
  read_skip_count BIGINT,
  write_skip_count BIGINT,
  process_skip_count BIGINT,
  rollback_count BIGINT,
  exit_code VARCHAR(20),
  exit_message VARCHAR(2500),
  last_updated TIMESTAMP,
  FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id),
  CONSTRAINT pk_batch_step_execution PRIMARY KEY (step_execution_id)
);

CREATE TABLE IF NOT EXISTS batch_step_execution_context (
  step_execution_id BIGINT,
  short_context VARCHAR(2500) NOT NULL,
  serialized_context TEXT,
  FOREIGN KEY (step_execution_id) REFERENCES batch_step_execution(step_execution_id),
  CONSTRAINT pk_batch_step_execution_context PRIMARY KEY (step_execution_id)
);


