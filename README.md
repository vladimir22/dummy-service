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


## Install dummy-service
helm repo add vladimir22 https://vladimir22.github.io/dummy-service
helm install ds -n default vladimir22/dummy-service --set db.host=$DB_HOST --set db.adminUsername=$DB_USERNAME --set db.adminPassword=$DB_PASSWORD


## Check DB access
http://localhost:8081/dummy-service/echo
http://localhost:8081/dummy-service/db
```
