FROM chainguard/jre:latest

WORKDIR /app

ARG JAR_FILE="xnote-springboot-v0.0.0.jar"
COPY target/${JAR_FILE} /app/xnote.jar
COPY src/main/resources/application-release.yml /app/config/application-release.yml

LABEL org.opencontainers.image.source https://github.com/xclhove/xnote-springboot

EXPOSE 8080

CMD ["-jar", "/app/xnote.jar"]