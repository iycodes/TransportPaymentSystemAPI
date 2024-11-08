FROM maven:3.9.0-eclipse-temurin-19 AS build

RUN mkdir /project

COPY . /project

WORKDIR /project

# ENV DB_PORT_TP='5432'
# ENV DB_NAME_TP='transportp'
# ENV DB_USERNAME='iyanuoluwa'
# ENV DB_USER_PASSWORD='pyrex007'
# ENV PORT_TP='8001'

RUN mvn clean package

FROM maven:3.9.0-eclipse-temurin-19

RUN mkdir /app

RUN addgroup --gid 1001 --system iyanuoluwagroup

RUN adduser --system iyanuoluwa -u 1001

COPY --from=build /project/target/transportp-1.0.jar /app/transportp.jar

WORKDIR /app

RUN chown -R iyanuoluwa:iyanuoluwagroup /app

CMD java $JAVA_OPTS -jar transportp.jar
