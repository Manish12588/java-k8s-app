# java-k8s-app

A simple Java Spring Boot app for Kubernetes practice.
Opens a webpage showing the tech stack and live system health of the pod.

## Tech Stack
- Java 17
- Spring Boot 3.2
- Thymeleaf (HTML templates)
- Embedded Tomcat
- Port: 8080

## What the page shows
- Tech stack details
- Pod name, Pod IP, Namespace, Node name
- Heap memory usage with visual bar
- CPU load average
- Pod uptime

---

## Run locally

### Prerequisites
- Java 17+
- Maven 3.8+

```bash
./mvnw spring-boot:run
```
Open http://localhost:8080

---

## Run with Docker

### Build
```bash
docker build -t YOUR_DOCKERHUB_USERNAME/java-k8s-app:latest .
```

### Run
```bash
docker run -p 8080:8080 YOUR_DOCKERHUB_USERNAME/java-k8s-app:latest
```
Open http://localhost:8080

### Push to Docker Hub
```bash
docker push YOUR_DOCKERHUB_USERNAME/java-k8s-app:latest
```

---

## Deploy to Kubernetes

### Step 1 — update image name
Open `k8s.yml` and replace `YOUR_DOCKERHUB_USERNAME` with your Docker Hub username.

### Step 2 — apply
```bash
kubectl apply -f k8s.yml
```

### Step 3 — verify
```bash
kubectl get pods
kubectl get svc
```

### Step 4 — access
```bash
kubectl port-forward service/java-app-service 8080:8080
```
Open http://localhost:8080

---

## Deploy in a namespace
```bash
kubectl create namespace dev
kubectl apply -f k8s.yml -n dev
kubectl port-forward service/java-app-service 8080:8080 -n dev
```

---

## Useful commands
```bash
kubectl logs java-app                  # view logs
kubectl describe pod java-app          # inspect pod
kubectl exec -it java-app -- sh        # shell into pod
kubectl delete -f k8s.yml              # delete pod and service
```
