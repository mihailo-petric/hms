# HR Management System

### Try it out

### Requirements

- Java 21
- docker
- shell

You can run the application by using the following command:

```
cd scripts
./setupLocal.sh
./startLocalServer.sh
```
Swagger url: http://localhost:8080/swagger-ui/index.html

There is "Production" mode available. 

- It creates docker image and runs it
```
./scripts/runProd.sh
```

### Completed:

- Create Employee REST
- Unit tests with coverage
- Integration tests
- Dockerfile
- Docker compose for easier local setup
- Spring profiles for running in different environments


### TODO:

- Add mutation tests
- Add TLS for production
- CI/CD
