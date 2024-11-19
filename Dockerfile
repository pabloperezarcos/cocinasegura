FROM openjdk:21-ea-24-oracle

WORKDIR /app
COPY target/cs-0.0.1-SNAPSHOT.jar app.jar
COPY Wallet_cocinasegurabd /app/oracle_wallet
EXPOSE 9090

CMD [ "java", "-jar", "app.jar" ]