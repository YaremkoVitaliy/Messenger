# Messenger

A simple messenger built on Java, Spring, PostgreSQL and Angular.

It contains only one common chat where you can exchange messages with other users in real time.

### How to start project

1. Install [Docker](https://docs.docker.com/engine/install/)
   and [Docker Compose](https://docs.docker.com/compose/install/).
2. In docker-compose.yml replace *your_database*, *your_username* and *your_password* with the actual database data:

```
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/your_database
  SPRING_DATASOURCE_USERNAME: your_username
  SPRING_DATASOURCE_PASSWORD: your_password
```

and

```
environment:
  POSTGRES_USER: your_username
  POSTGRES_PASSWORD: your_password
  POSTGRES_DB: your_database
```

3. Execute this command:

```bash
docker-compose up --build
```

4. Go to http://localhost:4200.