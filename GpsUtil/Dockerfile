#Use the OpenJDK 11 image as the base image
FROM openjdk:11

#Create a new app directory for my application files
RUN mkdir /app

#Set the direcotry for excution future commands
WORKDIR /app

#Copy the jar from host machine to image filesystem
COPY build/libs/GpsUtil.jar gps-utils.jar

#expose in hot:8081
EXPOSE 8081

ENTRYPOINT ["java","-jar","gps-utils.jar"]