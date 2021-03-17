# Se recomienda crear un repositorio de imágenes base propio en lugar de usar imágenes oficiales
FROM openjdk:11-jdk-slim
LABEL maintainer=Arquitectura
VOLUME /tmp
ADD ./target/msjava.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xmx200m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
