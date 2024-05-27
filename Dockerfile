FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY ./server/build/libs/*-all.jar /app/dbms-server.jar
ENTRYPOINT ["java","-jar","/app/dbms-server.jar"]