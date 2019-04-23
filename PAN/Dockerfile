FROM openjdk:8
ADD target/PAN-0.0.1-SNAPSHOT.war PAN-0.0.1-SNAPSHOT.war
EXPOSE 6060
ENTRYPOINT ["java","-jar","PAN-0.0.1-SNAPSHOT.war"]

FROM tomcat:8.5.39-jre8
COPY 
