FROM maven:3.8.3-openjdk-17 AS build

RUN mkdir -p /workspace

WORKDIR /workspace

COPY pom.xml /workspace

COPY src /workspace/src

RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /workspace/target/*.jar app.jar

ENV PORT 8080
ENV PG_HOST containers-us-west-114.railway.app
ENV DATABASE_NAME railway
ENV DATABASE_USER postgres
ENV DATABASE_PWD x7vCwu1hfm3GccUSskbS
ENV PG_PORT 5756
ENV JWT_KEY DOCKER
ENV AWS_KEY AKIAIOSFODNN7EXAMPLE
ENV AWS_SECRET wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
ENV S3_BUCKET binno-agro
ENV S3_REGION us-east-1
ENV API_KEY_MAIL nao_tem

EXPOSE $PORT

ENTRYPOINT ["java","-jar","app.jar"]
