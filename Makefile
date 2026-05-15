setup:
	cd app
	./gradlew wrapper --gradle-version 9.2.1
	./gradlew build

app:
	cd app
	./gradlew bootRun --args='--spring.profiles.active=dev'

clean:
	cd app
	./gradlew clean

build:
	cd app
	./gradlew clean build

dev: app

reload-classes:
	cd app
	./gradlew -t classes

install:
	cd app
	./gradlew installDist

test:
	cd app
	./gradlew test

lint:
	cd app
	./gradlew spotlessApply

.PHONY: setup app clean build dev reload-classes install test lint
