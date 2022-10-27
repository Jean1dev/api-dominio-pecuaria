FROM maven:3.6.3-jdk-11-slim AS build

RUN mkdir -p /workspace

WORKDIR /workspace

COPY pom.xml /workspace

COPY src /workspace/src

RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:11-jdk-slim

COPY --from=build /workspace/target/*.jar app.jar

ENV PORT 8080
ENV PG_HOST ec2-34-233-114-40.compute-1.amazonaws.com
ENV DATABASE_NAME dbdddk4ukgbn66
ENV DATABASE_USER vfteulbqcmbgjl
ENV DATABASE_PWD c50256b8ca315c69fdd1250f65c8cc503f0814eaf6c48dac9e5fa31690d6d361
ENV JWT_KEY DOCKER
ENV AWS_KEY AKIAIOSFODNN7EXAMPLE
ENV AWS_SECRET wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
ENV S3_BUCKET binno-agro
ENV S3_REGION us-east-1
ENV API_KEY_MAIL nao_tem

EXPOSE $PORT

ENTRYPOINT ["java","-jar","app.jar"]