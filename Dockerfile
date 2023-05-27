FROM gradle:8.1.1-jdk8

COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY src/ ./src/

RUN gradle --no-daemon installDist

COPY .env ./.env
CMD ["build/install/transl/bin/transl"]
