#!/bin/bash
sqlplus <<EOF
\x
create user single_window identified by single_window_12345 default tablespace USERS;
Grant connect to single_window;
Grant resource to single_window;

EOF
