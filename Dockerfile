FROM openjdk:17-oracle
COPY target/*.jar yourmusic.jar
ENTRYPOINT ["java","-jar", "yourmusic.jar" ]