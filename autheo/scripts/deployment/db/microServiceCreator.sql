INSERT INTO organizations (name) VALUES ('NA');

INSERT INTO roles (name) VALUES ('micro_service');

INSERT INTO business_roles (name, description, organization) VALUES ('micro_service', 'Micro service role', 'NA');

INSERT INTO system_business_roles ( id, systemrole, businessrole, organization) VALUES (100000, 'micro_service', 'micro_service', 'NA');

INSERT INTO users (name, documentType, documentNumber, email, username, password, salt, api_client, organization_id, role_id, image) 
VALUES ('workflow_manager', 'NA', 'NA', 'NA', 'workflow_manager', 'NA', 'NA', TRUE, 'NA', 100000, null);

INSERT INTO tokens ( token_value, username, organization_id, role_id, token_type, system_role) VALUES ('1', 'workflow_manager', 'NA', 100000, true, 'micro_service');



