# Dummy Business Service
Contains endpoint that executes parallel requests to a corresponding url. 

Usage examples:

```
## install dummy-service
helm install ds -n pf dummy-service
## expose outside
kubectl expose deployment dummy-service -n pf --type=LoadBalancer --name=dummy-service-ext --port=8080

## get external port
kubectl get service -n pf dummy-service-ext
NAME                TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
dummy-service-ext   LoadBalancer   10.43.70.105   <pending>     8080:30241/TCP   26s

## ask dummy-service to send 10 requests in a parallel to a corresponding url
curl http://kube-master:30241/run -d "url=http://echo-server.pf.svc.cluster.local:8080/test" -d "threads=10"
url: http://echo-server.pf.svc.cluster.local:8080/test, threads: 10, success:10, unsuccess: 0, errors: 0, duration: 542[
```
