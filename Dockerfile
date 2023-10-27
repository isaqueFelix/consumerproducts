FROM openjdk:8
ADD target/javaspringapp-1.0-SNAPSHOT.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
