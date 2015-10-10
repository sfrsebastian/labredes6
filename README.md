Configuracion Servidor con VLC
==============================

1. Descargar la aplicacion VLC Media Player
2. Definir en archivo *bash_profile (OSX)* o *bashrc (Linux)* el siguiente alias: <code>alias vlc='(ruta archivo VLC ejecutable) -I rc' </code>
3. Comprobar el funcionamiento de la linea de comandos escribiendo en la linea de comandos <code>vlc</code>

Creacion de stream UDP RTP Multicast
====================================
1. Ejecutar el comando <code>stream.sh (ruta archivo de video) </code>
2. Desde VLC o cualquier cliente que soporte Streams RTP, ingresar la direccion <code>rtp://239.255.0.1:9001</code>
3. Reproducir video.

Configuración Autheo
====================================
instalar postgress

CREATE DATABASE autheodb;
CREATE USER autheo WITH PASSWORD 'Autheo1234';
GRANT ALL PRIVILEGES ON DATABASE "autheodb" to autheo;

install redis server

Ejecutar el jar con el siguiente comando java -jar target/autheo-1.0-SNAPSHOT.jar server autheo-postgresql-develop.yml

Crear usuario 
ip:port/users/{{username}}/tokens  
{ 
	username: {{username}},
	password: {{hashedPassword}}, 
	organizationId: {{organization}} 
}
var bitArray = sjcl.hash.sha256.hash(password);
var hashedPassword = sjcl.codec.hex.fromBits(bitArray);