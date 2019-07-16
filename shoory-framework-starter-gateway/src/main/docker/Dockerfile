FROM openjdk:8-jdk-stretch

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    SPRING_OPTIONS="" 
    
ADD *.jar gateway.jar

CMD echo "The Gateway application will start " && \
	java ${JAVA_OPTS} -jar /gateway.jar $SPRING_OPTIONS
	
EXPOSE 8080
	