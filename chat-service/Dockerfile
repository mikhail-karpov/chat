FROM bellsoft/liberica-openjre-debian:25-cds AS builder
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM bellsoft/liberica-openjre-debian:25-cds
WORKDIR /app
COPY --from=builder /app/extracted/dependencies/ ./
COPY --from=builder /app/extracted/spring-boot-loader/ ./
COPY --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/extracted/application/ ./

ENV JAVA_OPTS="-XX:ActiveProcessorCount=1 \
-XX:InitialRAMPercentage=30.0 \
-XX:MaxRAMPercentage=70.0 \
-XX:MaxMetaspaceSize=128m \
-XX:CompressedClassSpaceSize=32m"

ENTRYPOINT exec java ${JAVA_OPTS} -jar application.jar
EXPOSE 8080