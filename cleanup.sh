#!/bin/bash
# kubernetes/cleanup.sh

set -e

echo "🧹 Starting Student Portal Kubernetes Cleanup..."

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

## Check if kubectl is installed
#if ! command -v kubectl &> /dev/null; then
#    print_error "kubectl is not installed. Please install kubectl first."
#    exit 1
#fi
#
## Check if namespace exists
#if ! kubectl get namespace student-portal &> /dev/null; then
#    print_warning "Namespace 'student-portal' does not exist. Nothing to clean up."
#    exit 0
#fi

print_status "Found namespace 'student-portal'. Starting cleanup..."

# Ask for confirmation
echo ""
read -p "Are you sure you want to delete the entire Student Portal deployment? This will remove all data! (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_status "Cleanup cancelled."
    exit 0
fi

# Delete Ingress first
print_status "Deleting Ingress..."
kubectl delete ingress --all -n student-portal --ignore-not-found=true

# Delete Services
print_status "Deleting Services..."
kubectl delete services --all -n student-portal --ignore-not-found=true

# Delete Deployments
print_status "Deleting Deployments..."
kubectl delete deployments --all -n student-portal --ignore-not-found=true

# Delete DaemonSets
print_status "Deleting DaemonSets..."
kubectl delete daemonsets --all -n student-portal --ignore-not-found=true

# Delete StatefulSets (if any)
print_status "Deleting StatefulSets..."
kubectl delete statefulsets --all -n student-portal --ignore-not-found=true

# Delete Jobs and CronJobs (if any)
print_status "Deleting Jobs and CronJobs..."
kubectl delete jobs --all -n student-portal --ignore-not-found=true
kubectl delete cronjobs --all -n student-portal --ignore-not-found=true

# Delete ConfigMaps
print_status "Deleting ConfigMaps..."
kubectl delete configmaps --all -n student-portal --ignore-not-found=true

# Delete Secrets
print_status "Deleting Secrets..."
kubectl delete secrets --all -n student-portal --ignore-not-found=true

# Delete PersistentVolumeClaims (this will delete data!)
print_status "Deleting Persistent Volume Claims (this will delete all data!)..."
kubectl delete pvc --all -n student-portal --ignore-not-found=true

# Delete RBAC resources
print_status "Deleting RBAC resources..."
kubectl delete clusterrolebindings promtail --ignore-not-found=true
kubectl delete clusterroles promtail --ignore-not-found=true
kubectl delete serviceaccounts --all -n student-portal --ignore-not-found=true

# Wait for pods to terminate
print_status "Waiting for pods to terminate..."
#kubectl wait --for=delete pods --all -n student-portal --timeout=300s || true

# Delete the namespace
print_status "Deleting namespace 'student-portal'..."
kubectl delete namespace student-portal --ignore-not-found=true

# Wait for namespace to be fully deleted
print_status "Waiting for namespace to be fully deleted..."
#kubectl wait --for=delete namespace/student-portal --timeout=120s || true

print_success "Cleanup completed successfully!"
print_status "All Student Portal resources have been removed from the cluster."

# Check for any remaining resources
print_status "Checking for any remaining resources..."
if kubectl get namespace student-portal &> /dev/null; then
    print_warning "Namespace still exists. Some resources might still be terminating."
    print_status "You can monitor with: kubectl get all -n student-portal"
else
    print_success "Namespace completely removed."
fi

echo ""
print_status "If you want to redeploy, run: ./deploy.sh"