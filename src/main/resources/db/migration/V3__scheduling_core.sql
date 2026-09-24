CREATE TABLE locations (
 id SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, code VARCHAR(30) NOT NULL UNIQUE, name VARCHAR(180) NOT NULL,
 address VARCHAR(255) NOT NULL, city VARCHAR(100) NOT NULL, department VARCHAR(100) NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;
CREATE TABLE specialties (
 id SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, code VARCHAR(50) NOT NULL UNIQUE, name VARCHAR(150) NOT NULL UNIQUE,
 appointment_duration_minutes SMALLINT UNSIGNED NOT NULL, is_general BOOLEAN NOT NULL DEFAULT FALSE,
 requires_admin_approval BOOLEAN NOT NULL DEFAULT TRUE, active BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT ck_specialty_duration CHECK (appointment_duration_minutes IN (30,60))
) ENGINE=InnoDB;
CREATE TABLE professionals (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, user_id BIGINT UNSIGNED NOT NULL UNIQUE,
 professional_code VARCHAR(40) NOT NULL UNIQUE, license_number VARCHAR(80) NOT NULL UNIQUE, active BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_prof_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE TABLE professional_specialties (
 professional_id BIGINT UNSIGNED NOT NULL, specialty_id SMALLINT UNSIGNED NOT NULL, is_primary BOOLEAN NOT NULL DEFAULT FALSE, active BOOLEAN NOT NULL DEFAULT TRUE,
 PRIMARY KEY(professional_id,specialty_id), FOREIGN KEY(professional_id) REFERENCES professionals(id) ON DELETE CASCADE,
 FOREIGN KEY(specialty_id) REFERENCES specialties(id) ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE TABLE professional_locations (
 professional_id BIGINT UNSIGNED NOT NULL, location_id SMALLINT UNSIGNED NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE,
 PRIMARY KEY(professional_id,location_id), FOREIGN KEY(professional_id) REFERENCES professionals(id) ON DELETE CASCADE,
 FOREIGN KEY(location_id) REFERENCES locations(id) ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE TABLE appointment_statuses (id SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, code VARCHAR(40) NOT NULL UNIQUE, name VARCHAR(80) NOT NULL, is_terminal BOOLEAN NOT NULL DEFAULT FALSE) ENGINE=InnoDB;
CREATE TABLE appointments (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, patient_user_id BIGINT UNSIGNED NOT NULL, professional_id BIGINT UNSIGNED NOT NULL,
 location_id SMALLINT UNSIGNED NOT NULL, specialty_id SMALLINT UNSIGNED NOT NULL, status_id SMALLINT UNSIGNED NOT NULL,
 reason VARCHAR(500), scheduled_start_at DATETIME NOT NULL, scheduled_end_at DATETIME NOT NULL, created_by_user_id BIGINT UNSIGNED NOT NULL,
 approved_by_user_id BIGINT UNSIGNED, approved_at DATETIME, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_ap_patient FOREIGN KEY(patient_user_id) REFERENCES users(id), CONSTRAINT fk_ap_prof FOREIGN KEY(professional_id) REFERENCES professionals(id),
 CONSTRAINT fk_ap_location FOREIGN KEY(location_id) REFERENCES locations(id), CONSTRAINT fk_ap_specialty FOREIGN KEY(specialty_id) REFERENCES specialties(id),
 CONSTRAINT fk_ap_status FOREIGN KEY(status_id) REFERENCES appointment_statuses(id), CONSTRAINT fk_ap_created FOREIGN KEY(created_by_user_id) REFERENCES users(id),
 CONSTRAINT fk_ap_approved FOREIGN KEY(approved_by_user_id) REFERENCES users(id), INDEX ix_ap_patient(patient_user_id,scheduled_start_at), INDEX ix_ap_prof(professional_id,scheduled_start_at), INDEX ix_ap_status(status_id)
) ENGINE=InnoDB;
CREATE TABLE availability_blocks (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, professional_id BIGINT UNSIGNED NOT NULL, location_id SMALLINT UNSIGNED NOT NULL,
 available_date DATE NOT NULL, start_time TIME NOT NULL, end_time TIME NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE,
 FOREIGN KEY(professional_id) REFERENCES professionals(id), FOREIGN KEY(location_id) REFERENCES locations(id),
 INDEX ix_block_prof_date(professional_id,available_date,start_time)
) ENGINE=InnoDB;
CREATE TABLE professional_slots (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, availability_block_id BIGINT UNSIGNED NOT NULL, start_at DATETIME NOT NULL, end_at DATETIME NOT NULL,
 appointment_id BIGINT UNSIGNED NULL, FOREIGN KEY(availability_block_id) REFERENCES availability_blocks(id) ON DELETE RESTRICT,
 FOREIGN KEY(appointment_id) REFERENCES appointments(id) ON DELETE SET NULL, UNIQUE KEY uq_block_slot(availability_block_id,start_at),
 INDEX ix_slot_search(start_at,appointment_id)
) ENGINE=InnoDB;
CREATE TABLE appointment_status_history (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, appointment_id BIGINT UNSIGNED NOT NULL, status_id SMALLINT UNSIGNED NOT NULL,
 changed_by_user_id BIGINT UNSIGNED NULL, change_source VARCHAR(20) NOT NULL, reason VARCHAR(500) NULL, changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(appointment_id) REFERENCES appointments(id) ON DELETE CASCADE, FOREIGN KEY(status_id) REFERENCES appointment_statuses(id),
 FOREIGN KEY(changed_by_user_id) REFERENCES users(id) ON DELETE SET NULL, INDEX ix_history_appointment(appointment_id,changed_at)
) ENGINE=InnoDB;
INSERT INTO locations(id,code,name,address,city,department,active) VALUES
 (1,'HIC','Hospital Internacional de Colombia (HIC)','Km 7 Autopista Bucaramanga - Piedecuesta, Valle de Menzulí','Piedecuesta','Santander',TRUE),
 (2,'ICV','Fundación Cardiovascular de Colombia - Instituto Cardiovascular (ICV)','Calle 155A No. 23-58, Urbanización El Bosque','Floridablanca','Santander',TRUE);
INSERT INTO specialties(id,code,name,appointment_duration_minutes,is_general,requires_admin_approval,active) VALUES
 (1,'MEDICINA_GENERAL','Medicina General',30,TRUE,FALSE,TRUE),(2,'CARDIOLOGIA_ADULTO','Cardiología Adulto',30,FALSE,TRUE,TRUE),
 (3,'ORTOPEDIA_TRAUMATOLOGIA','Ortopedia y Traumatología',60,FALSE,TRUE,TRUE);
INSERT INTO appointment_statuses(id,code,name,is_terminal) VALUES
 (1,'REQUESTED','Solicitada',FALSE),(2,'APPROVED','Aprobada',FALSE),(3,'REJECTED','Rechazada',TRUE),(4,'CANCELLED','Cancelada',TRUE),(5,'COMPLETED','Completada',TRUE),(6,'NO_SHOW','No asistió',TRUE);
-- Synthetic laboratory identities only. The password hashes are solely for isolated training data.
INSERT INTO users(id,first_name,last_name,document_type,document_number,email,phone,password_hash,active,email_verified) VALUES
 (900,'Admin','Laboratorio','CC','900000900','admin.s3@demo.invalid','3000000900','$2y$10$QAPT/bPvvEILB0ovqykfTuwSBznwY2p0rJhZJguneKjk2dr7VQFeG',TRUE,TRUE),
 (901,'General','Sintético','CC','900000901','general.s3@demo.invalid','3000000901','$2y$10$QAPT/bPvvEILB0ovqykfTuwSBznwY2p0rJhZJguneKjk2dr7VQFeG',TRUE,TRUE),
 (902,'Cardióloga','Sintética','CC','900000902','cardio.s3@demo.invalid','3000000902','$2y$10$QAPT/bPvvEILB0ovqykfTuwSBznwY2p0rJhZJguneKjk2dr7VQFeG',TRUE,TRUE);
INSERT INTO user_roles(user_id,role_id) VALUES (900,3),(901,2),(902,2);
INSERT INTO professionals(id,user_id,professional_code,license_number,active) VALUES (901,901,'PROF-S3-001','RM-S3-001',TRUE),(902,902,'PROF-S3-002','RM-S3-002',TRUE);
INSERT INTO professional_specialties(professional_id,specialty_id,is_primary,active) VALUES (901,1,TRUE,TRUE),(902,2,TRUE,TRUE),(902,3,FALSE,TRUE);
INSERT INTO professional_locations(professional_id,location_id,active) VALUES (901,1,TRUE),(901,2,TRUE),(902,1,TRUE),(902,2,TRUE);
