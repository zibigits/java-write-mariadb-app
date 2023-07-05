FROM openjdk:11
COPY JavaReadMariadbExampleApp.jar /opt
COPY mariadb-java-client-3.1.2.jar /opt
ENV TZ="Europe/Warsaw"
ENV JAVA_OPTS=""
ENV APP_OPTIONS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/JavaReadMariadbExampleApp.jar $APP_OPTIONS" ]
