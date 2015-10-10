cd ..
mvn clean compile test package install
cd target/
mvn install:install-file -Dfile=autheo-1.0-SNAPSHOT-client.jar -DgroupId=gp.e3.autheo.client -DartifactId=client -Dversion=0.1 -Dpackaging=jar