FROM openjdk:11-jdk-stretch

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    SPRING_OPTIONS="" 
    
ADD *.jar app.jar

CMD echo "The application will start " && \
	java ${JAVA_OPTS} -jar /app.jar ${SPRING_OPTIONS}
	
EXPOSE 8080