#
#
# this script is for setting up the k8s cluster in azure
#   https://docs.microsoft.com/en-us/cli/azure/install-azure-cli
#
#


# config values
export RESOURCE_GROUP="charonResourceGroup"
export CLUSTER="charonCluster"
export CREDENTIALS_FILE="AZURE_SP_CREDENTIALS.json"


function createResourceGroup () {
    if [ $(az group exists --name ${RESOURCE_GROUP}) = true ]; then
        echo "resource group ${RESOURCE_GROUP} already exists"
        return
    fi
    echo "creating resource group ${RESOURCE_GROUP}..."
    az group create \
         --name ${RESOURCE_GROUP} \
         --location germanywestcentral
    echo "...finished creating resource group ${RESOURCE_GROUP}"
}

function deleteResourceGroup () {
    az group delete \
         --name ${RESOURCE_GROUP}
}

#
# create the azure k8s cluster inside the resource group
function createCluster () {
    if [ -n "$(az aks show -n ${CLUSTER} -g ${RESOURCE_GROUP} -o json --query id)" ]; then
        echo "cluster ${CLUSTER} already exists"
        return
    fi
    az aks create \
        --resource-group ${RESOURCE_GROUP} \
        --name ${CLUSTER} \
        --node-count 2 \
        --enable-addons http_application_routing \
        --generate-ssh-keys
}



#
# creating the service principal for github to access azure and k8s
#
function createCredentials () {
export SUBSCRIPTION_ID=$(az account show --query id -o tsv)
az ad sp create-for-rbac \
    --name "charonApp" \
    --role contributor \
    --scopes /subscriptions/${SUBSCRIPTION_ID}/resourceGroups/${RESOURCE_GROUP} \
    --sdk-auth > ${CREDENTIALS_FILE}
  #
  # setup the secret in the gibhub repo as "AZURE_SP_CREDENTIALS"
  # the github deploy action will pick up the secret as secrets.AZURE_SP_CREDENTIALS
}




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

for i in "$@"; do
  case $i in
    create|c|--create|-c)
        createResourceGroup
        createCluster
        createCredentials
        ;;
    delete|d|--delete|-d)
        deleteResourceGroup
        ;;
    *)
        echo "Unknown option $i"
        echo "  use [create|delete]"
        exit 1
        ;;
  esac
done
