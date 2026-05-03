# Centrifugo Chat Demo

Demo project showing the capabilities of the Centrifugo. Centrifugo is a scalable messaging server 
that handles persistent connections over various real-time transports, i.e. WebSocket, SSE, gRPC, etc. 
Visit https://centrifugal.dev/ to learn more.

<img src="./docs/images/chat-architecture.png" alt="Architecture" width="500" height="auto">

## Setup

### Prerequisites

- Java 25
- Gradle
- Docker Compose

### Building and Running

```shell
# Build the project
./gradlew build
```

```shell
# Running containers
docker compose up --build -d
```

Once the containers are running, open your browser at http://localhost:5173

Several test users are preconfigured:

| Username  | Password |
|:----------|:---------|
| jamesbond | password |
| johnwayne | password |
| annasmith | password |
| chelsea   | password |

Grafana is available at http://localhost:3000

```shell
# Stop containers
docker compose down -v
```
