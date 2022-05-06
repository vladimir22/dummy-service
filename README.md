# dummy-service
Spring Boot microservice which is helpful for testing and debugging


## Tips how to install

### - Resolve DB Credentials
`dummy-service` helm chart includes optional [dbinstance](https://github.com/kloeckner-i/db-operator/blob/master/docs/creatinginstances.md#genericdbinstance) and [database](https://github.com/kloeckner-i/db-operator/blob/master/docs/creatingdatabases.md#creatingdatabases) objects which are managed by [db-operator](https://github.com/kloeckner-i/db-operator#to-install-db-operator-with-helm)
```sh
## Reslove DB Credentials
DB_HOST=postgres-db-pg-cluster.pf.svc.cluster.local
DB_NAME=postgres
DB_USERNAME=postgres
## Get DB password from the generated secret
DB_PASSWORD=$(kubectl get secret -n pf "postgres.postgres-db-pg-cluster.credentials.postgresql.acid.zalan.do" -o jsonpath='{.data.password}' | base64 --decode)
echo "DB_PASSWORD=$DB_PASSWORD"
```

### - Install `dummy-service`
```sh
## Install dummy-service
## https://vladimir22.github.io/dummy-service/index.yaml
helm repo add vladimir22 https://vladimir22.github.io/dummy-service
HELM_VERSION=1.0.2
helm repo update vladimir22
helm install ds -n default vladimir22/dummy-service --version $HELM_VERSION --set db.host=$DB_HOST --set db.adminUsername=$DB_USERNAME --set db.adminPassword=$DB_PASSWORD
```


## Usage example

### - Echoserver Endpoint
```
## Echoserver endpoint
curl http://localhost:8081/dummy-service/echo
{"requestHeaders":{"x-real-ip":"10.42.0.1","x-forwarded-server":"traefik-55fdc6d984-59mq7","x-forwarded-host":"localhost:8081","x-forwarded-proto":"http","host":"localhost:8081","x-forwarded-port":"8081","accept-encoding":"gzip","user-agent":"curl/7.79.1","accept":"*/*"},"body":"","requestURI":"/dummy-service/echo"}
```

### - Check DB
```
## Check DB status
curl http://localhost:8081/dummy-service/db
{"jdbcUrl":"jdbc:postgresql://postgres-db-pg-cluster.pf.svc.cluster.local:5432/testdb","username":"testdb_user","password":"t***d","valid":true}
```

### - HTTP Client
`dummy-service` has its own *HTTP Client* which can be configured using REST API. 
```
## Ask dummy-service to send 10 parallel requests to specfied url
curl http://localhost:8081/dummy-service/http -X POST -d "url=http://google.com" -d "threads=10"
  url: http://google.com, threads: 10, success:10, unsuccessful: 0, errors: 0, duration: 964
```

