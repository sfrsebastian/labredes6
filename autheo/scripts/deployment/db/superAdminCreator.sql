INSERT INTO roles (name) VALUES ('superAdmin');

INSERT INTO business_roles (name, description, organization) VALUES ('superAdmin', 'superAdmin', 'certicamara');

INSERT INTO system_business_roles ( systemrole, businessrole, organization) VALUES ('superAdmin', 'superAdmin', 'certicamara');

/* 
 * User: superAdmin    
 * Password: qwe
 */
INSERT INTO users (name, documentType, documentNumber, email, username, password, salt, api_client, organization_id, role_id, image) 
VALUES ('superAdmin', 'NIT', '000011', 'soporte@certicamara.com', 'superAdmin', '1000:849f0ae5acfd0c39cf01d0ad1ec1a73c2f9dea85f8f1433c:649213aadfb70ff739d945437562be9cc8ed374e0c22028a', '849f0ae5acfd0c39cf01d0ad1ec1a73c2f9dea85f8f1433c', FALSE, 'certicamara', 1, null);

INSERT INTO roles_users (username, role_name, organization) VALUES ('superAdmin', 'superAdmin', 'certicamara');
