.PHONY: build
build:
	@./gradlew build

.PHONY: run-infra
run-infra:
	@docker compose -f compose.infra.yaml up -d

.PHONY: run
run:
	@docker compose up --build -d

.PHONY: stop
stop:
	@docker compose down -v
