FROM tomcat:8.5-jdk8-openjdk

WORKDIR $CATALINA_HOME

# create a reference to some file on the physical machine (optional)
ARG WAR_FILE=./target/*.war

# copy the file from the physical machine to the webapps dir in the PWD
COPY $WAR_FILE ./webapps

EXPOSE 8080

CMD ["catalina.sh", "run"]