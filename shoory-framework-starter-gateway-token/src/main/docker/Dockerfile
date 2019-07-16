FROM openjdk:8-jdk-stretch

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    SPRING_OPTIONS="" 
    
ADD *.jar gateway-token.jar

CMD echo "The Gateway application will start " && \
	java ${JAVA_OPTS} -jar /gateway-token.jar --spring.cloud.nacos.config.server-addr=nacos:8848
	
EXPOSE 8080
	