# dummy-service
Spring Boot microservice which is helpful for testing and debugging


## Tips how to install


### - Install [db-operator](https://github.com/kloeckner-i/db-operator#quickstart)
```sh
VERSION=1.5.1
NS=db-oper
## Available versions: https://kloeckner-i.github.io/charts/index.yaml
helm repo add kloeckneri https://kloeckner-i.github.io/charts/
helm repo update kloeckneri
kubectl create ns $NS
helm install dbo kloeckneri/db-operator -n $NS --version $VERSION
```


### - Install postgres-operator
```sh
git clone https://github.com/zalando/postgres-operator.git
cd postgres-operator/charts/postgres-operator
kubectl create ns pgo
helm install pgo . -n pgo
```


### - Create PG Cluster
```sh
SITEA_NS=sa
kubectl create ns $SITEA_NS
SITEA_NAME=postgres-db-site-a

DB_ADMIN_NAME=postgres
DB_ADMIN_PASSWORD=postgresPwd

DB_NAME=testdb
DB_USER_NAME=testuser
DB_USER_PASSWORD=testuserPwd


## Create secrets with pre-defined passwords
cat <<EOF | kubectl apply -f -
apiVersion: v1
type: Opaque
kind: Secret
metadata:
  name: standby.$SITEA_NAME.credentials.postgresql.acid.zalan.do
  namespace: $SITEA_NS
stringData:
  password: standbyPwd
  username: standby
EOF

cat <<EOF | kubectl apply -f -
apiVersion: v1
type: Opaque
kind: Secret
metadata:
  name: postgres.$SITEA_NAME.credentials.postgresql.acid.zalan.do
  namespace: $SITEA_NS
stringData:
  username: $DB_ADMIN_NAME
  password: $DB_ADMIN_PASSWORD
EOF

cat <<EOF | kubectl apply -f -
apiVersion: v1
type: Opaque
kind: Secret
metadata:
  name: $DB_USER_NAME.$SITEA_NAME.credentials.postgresql.acid.zalan.do
  namespace: $SITEA_NS
stringData:
  username: $DB_USER_NAME
  password: $DB_USER_PASSWORD
EOF


## SiteA: Create postgresql : https://github.com/zalando/postgres-operator/blob/v1.8.1/manifests/complete-postgres-manifest.yaml
kubectl apply -n $SITEA_NS -f - <<EOF
apiVersion: "acid.zalan.do/v1"
kind: postgresql
metadata:
  name: $SITEA_NAME ## Cluster name prefix must match with "teamId" value !!!
spec:
  teamId: "postgres-db"
  volume:
    size: 128Mi
  numberOfInstances: 1
  users:
    ## Create users, set up roles
    $DB_USER_NAME:  # database owner
    - superuser
    - createdb
    testdb_user: []  # ordinary role for application
  ## Create db & assign owner
  databases:
    $DB_NAME: $DB_USER_NAME  # dbname: owner
  postgresql:
    version: "14"
    parameters: ## addons for 'postgresql.conf': https://github.com/postgres/postgres/blob/master/src/backend/utils/misc/postgresql.conf.sample
      log_statement: "all"
      log_replication_commands: "on"
  patroni: ## addons for patroni 'postgres.yml': https://github.com/zalando/patroni/blob/master/postgres0.yml
    synchronous_mode: false
    synchronous_mode_strict: false
EOF

```



### - Resolve DB Credentials
`dummy-service` helm chart includes optional [dbinstance](https://github.com/kloeckner-i/db-operator/blob/master/docs/creatinginstances.md#genericdbinstance) and [database](https://github.com/kloeckner-i/db-operator/blob/master/docs/creatingdatabases.md#creatingdatabases) objects which are managed by [db-operator](https://github.com/kloeckner-i/db-operator#to-install-db-operator-with-helm)
```sh
## Reslove DB Credentials
DB_HOST=$SITEA_NAME.$SITEA_NS.svc.cluster.local
DB_NAME=postgres
DB_ADMIN_USERNAME=postgres
## Get DB password from the generated secret
DB_ADMIN_PASSWORD=$(kubectl get secret -n $SITEA_NS "postgres.$SITEA_NAME.credentials.postgresql.acid.zalan.do" -o jsonpath='{.data.password}' | base64 --decode)
echo "DB_PASSWORD=$DB_ADMIN_PASSWORD"
```


### - Install `dummy-service`
```sh
## Install dummy-service helm chart: https://vladimir22.github.io/dummy-service/index.yaml
helm repo add vladimir22 https://vladimir22.github.io/dummy-service
HELM_VERSION=1.0.2
NS=ds
kubectl create ns $NS
helm repo update vladimir22

## 1st Option: Specify DB_HOST, DB_ADMIN_PASSWORD --> db-operator will create automatically testdb:testdb_user/testdb_password
helm install ds -n $NS vladimir22/dummy-service --version $HELM_VERSION --set db.host=$DB_HOST --set db.adminUsername=$DB_ADMIN_USERNAME --set db.adminPassword=$DB_ADMIN_PASSWORD

## 2nd Option: Specify DB_HOST, DB Creds only --> db-operator resources won't be created without 'db.adminPassword' value
DB_NAME=testdb
DB_USER_NAME=testuser
DB_USER_PASSWORD=$(kubectl get secret -n $SITEA_NS "$DB_USER_NAME.$SITEA_NAME.credentials.postgresql.acid.zalan.do" -o jsonpath='{.data.password}' | base64 --decode)
echo -e "Installing dummy-service:\n  DB_HOST=$DB_HOST\n  DB_USER_NAME=$DB_USER_NAME\n  DB_USER_PASSWORD=$DB_USER_PASSWORD"
helm install ds -n $NS vladimir22/dummy-service --version $HELM_VERSION --set db.host=$DB_HOST --set db.name=$DB_ADMIN_USERNAME --set db.username=$DB_USER_NAME --set db.password=$DB_USER_PASSWORD


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
`dummy-service` has its own *HTTP Client* which can be configured using REST API. Example:
```
## Ask dummy-service to send 10 parallel requests to specfied url
curl http://localhost:8081/dummy-service/http -X POST -d "url=http://dummy-service:8080/dummy-service/echo" -d "threads=10"
  url: http://google.com, threads: 10, success:10, unsuccessful: 0, errors: 0, duration: 964
```

