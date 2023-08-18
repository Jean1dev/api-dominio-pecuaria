FROM gradle:7.6.1-jdk17-alpine AS builder

WORKDIR /usr/app/
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

COPY . .

RUN gradle build -x test

FROM eclipse-temurin:17.0.6_10-jre-alpine

COPY --from=builder /usr/app/build/libs/*.jar /opt/app/application.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

RUN ls /opt/app/
CMD java -jar /opt/app/application.jar
