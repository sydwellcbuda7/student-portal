#!/bin/bash
# kubernetes/deploy.sh

set -e

echo "🚀 Starting Student Portal Kubernetes Deployment..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null; then
    print_error "kubectl is not installed. Please install kubectl first."
    exit 1
fi

# Explicitly set KUBECONFIG to microk8s config
export KUBECONFIG=/var/snap/microk8s/current/credentials/client.config

KUBECTL_PATH="/snap/bin/kubectl"


# Check if cluster is accessible
if ! "$KUBECTL_PATH" get nodes &> /dev/null; then
    echo "[ERROR] Cannot connect to Kubernetes cluster. Please check your kubeconfig."
    exit 1
fi

print_status "Connected to Kubernetes cluster"

# Create namespace
print_status "Creating namespace..."
kubectl apply -f namespace.yaml

# Wait for namespace to be ready
#kubectl wait --for=condition=Ready namespace/student-portal --timeout=60s

# Apply ConfigMaps and Secrets
print_status "Applying ConfigMaps and Secrets..."
kubectl apply -f configmaps.yaml
kubectl apply -f secrets.yaml
kubectl apply -f monitoring-configmaps.yaml

# Apply Persistent Volume Claims
print_status "Creating Persistent Volume Claims..."
kubectl apply -f persistent-volumes.yaml

# Wait for PVCs to be bound (with timeout)
print_status "Waiting for PVCs to be bound..."
#kubectl wait --for=condition=Bound pvc/mysql-pvc -n student-portal --timeout=120s
#kubectl wait --for=condition=Bound pvc/prometheus-pvc -n student-portal --timeout=120s
#kubectl wait --for=condition=Bound pvc/grafana-pvc -n student-portal --timeout=120s
#kubectl wait --for=condition=Bound pvc/loki-pvc -n student-portal --timeout=120s

# Deploy MySQL first (required by other services)
print_status "Deploying MySQL database..."
kubectl apply -f mysql-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/mysql-db -n student-portal

# Deploy core application services
print_status "Deploying Student Portal application..."
kubectl apply -f student-portal-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/student-portal -n student-portal

print_status "Deploying Student Portal UI..."
kubectl apply -f student-portal-ui-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/student-portal-ui -n student-portal

# Deploy supporting services
print_status "Deploying MailDev..."
kubectl apply -f maildev-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/maildev -n student-portal

# Deploy monitoring stack
print_status "Deploying monitoring stack..."

# Deploy Loki first (required by Promtail)
kubectl apply -f loki-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/loki -n student-portal

# Deploy Prometheus and related services
kubectl apply -f prometheus-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/prometheus -n student-portal

# Deploy Alertmanager
kubectl apply -f alertmanager-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/alertmanager -n student-portal

# Deploy VictoriaMetrics
kubectl apply -f victoriametrics-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/victoriametrics -n student-portal

# Deploy Grafana
kubectl apply -f grafana-deployment.yaml
#kubectl wait --for=condition=available --timeout=300s deployment/grafana -n student-portal

# Deploy exporters and monitoring agents
print_status "Deploying monitoring exporters..."
kubectl apply -f mysql-exporter-deployment.yaml
kubectl apply -f nginx-exporter-deployment.yaml
kubectl apply -f blackbox-exporter-deployment.yaml

# Deploy DaemonSets
print_status "Deploying DaemonSets..."
kubectl apply -f node-exporter-daemonset.yaml
kubectl apply -f cadvisor-daemonset.yaml

# Deploy Promtail with RBAC
kubectl apply -f promtail-rbac.yaml
kubectl apply -f promtail-daemonset.yaml


kubectl apply -f alertmanager-deployment.yaml

# Wait for all deployments to be ready
print_status "Waiting for all deployments to be ready..."
#kubectl wait --for=condition=available --timeout=300s deployment/mysql-exporter -n student-portal
#kubectl wait --for=condition=available --timeout=300s deployment/nginx-exporter -n student-portal
#kubectl wait --for=condition=available --timeout=300s deployment/blackbox-exporter -n student-portal

# Apply Ingress (optional)
if kubectl get ingressclass nginx &>/dev/null; then
    print_status "Deploying Ingress..."
    kubectl apply -f ingress.yaml
else
    print_warning "Nginx Ingress Controller not found. Skipping ingress deployment."
    print_warning "You can access services using port-forward or LoadBalancer IPs."
fi

# Display service information
print_success "Deployment completed successfully!"
echo ""
print_status "Service Access Information:"
echo "=================================="

# Get LoadBalancer IPs/Ports
kubectl get services -n student-portal -o wide

echo ""
print_status "To access services locally, use kubectl port-forward:"
echo "kubectl port-forward -n student-portal svc/student-portal-ui-service 4200:4200"
echo "kubectl port-forward -n student-portal svc/grafana-service 3000:3000"
echo "kubectl port-forward -n student-portal svc/prometheus-service 9090:9090"
echo "kubectl port-forward -n student-portal svc/alertmanager-service 9093:9093"
echo "kubectl port-forward -n student-portal svc/maildev-service 1080:1080"

echo ""
print_status "Checking pod status..."
kubectl get pods -n student-portal

echo ""
print_success "Student Portal is now running on Kubernetes!"
print_status "Monitor the deployment with: kubectl get pods -n student-portal -w"