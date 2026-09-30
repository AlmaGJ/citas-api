-- UserInsuranceAffiliationEntity has mapped membership_number/valid_from/valid_to since the
-- identity-lifecycle increment, but no prior migration ever added them to this table.
-- Table is empty at this point (see V2), so no backfill is required.
ALTER TABLE user_insurance_affiliations
    ADD COLUMN membership_number VARCHAR(80) NOT NULL AFTER plan_id,
    ADD COLUMN valid_from DATE NULL AFTER is_current,
    ADD COLUMN valid_to DATE NULL AFTER valid_from;
