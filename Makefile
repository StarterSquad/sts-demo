build:
	cd webapp-jpa-hibernate && mvn clean package -DskipTests
	cp webapp-jpa-hibernate/target/hibernate3/sql/schema.ddl container-db/init.sql

docker-db: build
	cd container-db && docker build -t stackstate-demo-db:latest .

docker-app:
	docker build --no-cache -t stackstate-demo-app:latest .
