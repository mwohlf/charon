#
#
# this script is for setting up the k8s cluster in azure
#   https://docs.microsoft.com/en-us/cli/azure/install-azure-cli
#
# it needs
#  - az (sudo yay -S az-cli)
#  - kubectl (sudo az aks install-cli)
#  - kubelogin
#

set -e

CURRENT_DIR="${PWD}"
SCRIPT_DIR="${0%/*}"
function cleanup {
    echo "finishing the script, error code is ${?}"
    # back to where we came from
    cd "${CURRENT_DIR}"
}
trap cleanup EXIT

# config values
export RESOURCE_GROUP="charonResourceGroup"
export CLUSTER="charonCluster"
export LOCATION="germanywestcentral"
export CREDENTIALS_FILE="credentials.txt"
export TOKEN_FILE="token.txt"



function create_container_registry() {
    # this creates acr charon.azurecr.io
    az acr create \
        --resource-group ${RESOURCE_GROUP:=charonResourceGroup} \
        --location ${LOCATION:=germanywestcentral} \
        --name charon \
        --sku Basic
}

function get_pods() {
    az aks command invoke \
        --resource-group ${RESOURCE_GROUP:=charonResourceGroup} \
        --name ${CLUSTER:=charonCluster} \
        --command "kubectl get pods -n kube-system"
}

function create_public_ip_address() {
    NODE_RESOURCE_GROUP=$(az aks show \
        -g ${RESOURCE_GROUP} \
        -n ${CLUSTER} \
        --query 'nodeResourceGroup' -o tsv)
    export NODE_RESOURCE_GROUP
    PUBLIC_IP=$(az network public-ip create \
        -g "${NODE_RESOURCE_GROUP}" \
        -n applicationIp \
        --sku Standard \
        --allocation-method Static \
        --query 'publicIp.ipAddress' \
        -o tsv)
    export PUBLIC_IP
    echo "Your public IP address is ${PUBLIC_IP}."
}

function delete_secrets() {
    rm "${SCRIPT_DIR}/${TOKEN_FILE}"
    rm "${SCRIPT_DIR}/${CREDENTIALS_FILE}"
}

#
# see: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/
#      https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md
#
function deploy_dashboard() {
    kubectl apply -f ./dashboard-setup.yaml
    kubectl proxy &
    SECRET_NAME=$(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}{'\n'}")
    kubectl -n kubernetes-dashboard get secret "${SECRET_NAME}" -o go-template="{{.data.token | base64decode}}" >"${SCRIPT_DIR}/${TOKEN_FILE}"
    cat <<EOF
copy the token from ${TOKEN_FILE} to login at
http://127.0.0.1:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/node?namespace=default
EOF
}

########## helm ###########

#
# create a chart:
#   helm create azure-vote-front
#
# deploy the chart:
#   helm install charon ../helm/charon/
#   helm uninstall charon
#
# info:
#   kubectl cluster-info
#
function deploy_chart() {
    helm upgrade --install --wait --timeout 30s charon ../helm/charon/

    POD_NAME=$(kubectl get pods \
        --namespace default \
        -l "app.kubernetes.io/name=charon,app.kubernetes.io/instance=charon" \
        -o jsonpath="{.items[0].metadata.name}")
    export POD_NAME

    CONTAINER_PORT=$(kubectl get pod \
        --namespace default "${POD_NAME}" \
        -o jsonpath="{.spec.containers[0].ports[0].containerPort}")
    export CONTAINER_PORT
}

function delete_chart() {
    helm delete charon
}

########## credentials ###########

#
# creating the service principal for github to access azure and k8s
#
function create_credentials() {
    SUBSCRIPTION_ID=$(az account show --query id -o tsv)
    export SUBSCRIPTION_ID
    cat >"${SCRIPT_DIR}/${CREDENTIALS_FILE}" <<EOF
// stored as repository secret with name AZURE_SP_CREDENTIALS
// GitHub -> Repo -> Secrets -> Actions -> New Repository secret -> insert Name, Value
EOF
    az ad sp create-for-rbac \
        --name "charonApp" \
        --role contributor \
        --scopes /subscriptions/"${SUBSCRIPTION_ID}"/resourceGroups/${RESOURCE_GROUP} \
        --sdk-auth >>${CREDENTIALS_FILE}
    # setup the secret in the github repo as "AZURE_SP_CREDENTIALS"
    # the github deploy action will pick up the secret as secrets.AZURE_SP_CREDENTIALS

    echo "credentials have been stored in ${CREDENTIALS_FILE}"
}

########## cluster ###########

function has_cluster() {
    # az aks show -n charonCluster -g charonResourceGroup -o json --query id 2>/dev/null
    # -n: check for non-empty
    if [[ -n "$(az aks show -n ${CLUSTER} -g ${RESOURCE_GROUP} -o json --query id 2>/dev/null)" ]]; then
        return 0 # true
    else
        return 1 # false
    fi
}

#
# create the azure k8s cluster inside the resource group
function create_cluster() {
    if has_cluster; then
        echo "cluster ${CLUSTER} already exists"
        return
    fi
    echo "creating cluster ${CLUSTER}..."
    az aks create \
        --resource-group ${RESOURCE_GROUP} \
        --name ${CLUSTER} \
        --node-count 2 \
        --enable-addons http_application_routing \
        --generate-ssh-keys >/dev/null
    echo "getting credentials"
    az aks get-credentials \
        --resource-group ${RESOURCE_GROUP} \
        --name ${CLUSTER} \
        --overwrite-existing
    echo "...finished creating ${CLUSTER}"
}

########## resource group ###########

function has_resource_group() {
    # az group exists --name charonResourceGroup
    if [[ "$(az group exists --name ${RESOURCE_GROUP})" == "true" ]]; then
        return 0 # true
    else
        return 1 # false
    fi
}

function create_resource_group() {
    if has_resource_group; then
        echo "resource group ${RESOURCE_GROUP} already exists"
        return
    fi
    echo "creating resource group ${RESOURCE_GROUP}..."
    az group create \
        --name ${RESOURCE_GROUP} \
        --location ${LOCATION:=germanywestcentral} >/dev/null
    echo "...finished creating resource group ${RESOURCE_GROUP}"
    az configure --defaults group=${RESOURCE_GROUP} >/dev/null
}

function delete_resource_group() {
    if has_resource_group; then
        az group delete \
            --name ${RESOURCE_GROUP} \
            --yes >/dev/null
    else
        echo "resource group ${RESOURCE_GROUP} already removed"
    fi
}

########## authentication ###########

function is_authenticated() {
    if [[ -n "$(az account show 2>/dev/null)" ]]; then
        return 0
    else
        return 1
    fi
}

function login_azure() {
    if is_authenticated; then
        echo "already authenticated, skipping login"
        return
    fi
    az login >/dev/null
}

function logout_azure() {
    if is_authenticated; then
        az logout
    fi
}

########## usage ###########

function show_usage() {
    cat <<EOF
This script is intended to simplify setup and deployment in azure cli or azure cloud cli
Arguments are:
 - create: to setup up the cluster
 - deploy_dashboard: to show the k8s dashboard
 - deploy_chart: to deploy the helm chart
 - delete_chart: to delete the helm chart
 - login_azure: to login for local az, not needed in azure cloud cli
 - create_public_ip_address: create an ip address
 - delete: to remove the cluster
EOF
}

#
#    az aks command invoke \
#       --resource-group ${RESOURCE_GROUP:=charonResourceGroup} \
#       --name ${CLUSTER:=charonCluster} \
#       --command "kubectl get pods -n kube-system"
#

# install kubectl (already installed in azure cloud shell)
# it is even better to use the azure shell in the browser
# az aks install-cli

# for local shell
# merging the cluster credential as current context into /home/michael/.kube/config
# az aks get-credentials \
#    --resource-group ${RESOURCE_GROUP} \
#    --name ${CLUSTER} \

#
# query for the service principal appId
#  az aks show \
#    --resource-group ${RESOURCE_GROUP} \
#    --name ${CLUSTER} \
#    --query servicePrincipalProfile.clientId \
#    -o tsv \

##################################################
#  main
##################################################

if [[ $# -eq 0 ]]; then
    show_usage
    exit 1
fi

for var in "$@"; do
    case $var in
    create)
        login_azure
        create_resource_group
        create_cluster
        create_credentials
        deploy_chart
        create_public_ip_address
        ;;
    deploy_dashboard)
        deploy_dashboard
        ;;
    deploy_chart)
        deploy_chart
        ;;
    delete_chart)
        delete_chart
        ;;
    delete)
        delete_resource_group
        logout_azure
        delete_secrets
        ;;
    login_azure)
        login_azure
        ;;
    logout_azure)
        logout_azure
        ;;
    create_public_ip_address)
        create_public_ip_address
        ;;
    help | h | --help | -h)
        show_usage
        ;;
    *)
        show_usage
        exit 1
        ;;
    esac
done
