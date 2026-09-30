CREATE TABLE IF NOT EXISTS password_reset_tokens (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT UNSIGNED NOT NULL,
 token_hash VARCHAR(255) NOT NULL UNIQUE,
 expires_at DATETIME NOT NULL,
 used_at DATETIME NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
 INDEX ix_password_reset_user (user_id), INDEX ix_password_reset_expiry (expires_at)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reschedule_request_statuses (
 id SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 code VARCHAR(40) NOT NULL UNIQUE,
 name VARCHAR(80) NOT NULL,
 is_terminal BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

INSERT IGNORE INTO reschedule_request_statuses(id, code, name, is_terminal) VALUES
 (1, 'PENDING', 'Pendiente', FALSE), (2, 'APPROVED', 'Aprobada', TRUE),
 (3, 'REJECTED', 'Rechazada', TRUE), (4, 'CANCELLED', 'Cancelada', TRUE);

CREATE TABLE IF NOT EXISTS reschedule_requests (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 appointment_id BIGINT UNSIGNED NOT NULL,
 requested_by_user_id BIGINT UNSIGNED NOT NULL,
 requested_location_id SMALLINT UNSIGNED NOT NULL,
 status_id SMALLINT UNSIGNED NOT NULL,
 previous_start_at DATETIME NOT NULL,
 previous_end_at DATETIME NOT NULL,
 requested_start_at DATETIME NOT NULL,
 requested_end_at DATETIME NOT NULL,
 decision_reason VARCHAR(500) NULL,
 decided_by_user_id BIGINT UNSIGNED NULL,
 decided_at DATETIME NULL,
 patient_action_after_rejection VARCHAR(30) NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_reschedule_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
 CONSTRAINT fk_reschedule_requested_by FOREIGN KEY (requested_by_user_id) REFERENCES users(id) ON DELETE RESTRICT,
 CONSTRAINT fk_reschedule_location FOREIGN KEY (requested_location_id) REFERENCES locations(id) ON DELETE RESTRICT,
 CONSTRAINT fk_reschedule_status FOREIGN KEY (status_id) REFERENCES reschedule_request_statuses(id) ON DELETE RESTRICT,
 CONSTRAINT fk_reschedule_decided_by FOREIGN KEY (decided_by_user_id) REFERENCES users(id) ON DELETE RESTRICT,
 INDEX ix_reschedule_appointment (appointment_id), INDEX ix_reschedule_status (status_id)
) ENGINE=InnoDB;
