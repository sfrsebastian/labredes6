#!/bin/bash
psql <<EOF
\x
CREATE USER portal_functionary PASSWORD 'portal_functionary_12345';
CREATE DATABASE portal_functionary_db WITH OWNER portal_functionary;
GRANT ALL PRIVILEGES ON DATABASE portal_functionary_db TO portal_functionary;
EOF
