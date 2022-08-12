#!/usr/bin/env bash
#
#
# https://hub.docker.com/r/certbot/dns-cloudflare
# https://certbot-dns-cloudflare.readthedocs.io/en/stable/#credentials
# https://www.nodinrogers.com/post/2022-03-10-certbot-cloudflare-docker/
# https://stackoverflow.com/questions/50389883/generate-crt-key-ssl-files-from-lets-encrypt-from-scratch
# https://www.sslshopper.com/article-most-common-openssl-commands.html

set -e


DOMAIN="wired-heart.com"
KEYVAULT="finalrestingheartrateVlt"

CURRENT_DIR="${PWD}"
SCRIPT_DIR="$(
    cd -- "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)"
function cleanup {
    echo "finishing the script, error code is ${?}"
    # back to where we came from
    cd "${CURRENT_DIR}"
}
trap cleanup EXIT

function create_cert() {
    # for running local
    if [[ -z "${CLOUDFLARE_TOKEN}" ]]; then
        CLOUDFLARE_TOKEN=$(cat "${SCRIPT_DIR}/../setup/cloudflare-token.txt") #the output of 'cat $file' is assigned to the $name variable
    fi

    mkdir -p "${SCRIPT_DIR}/etc"
    echo "dns_cloudflare_api_token = ${CLOUDFLARE_TOKEN}" >"${SCRIPT_DIR}/etc/credentials"
    # chmod 400 ./etc/credentials

    docker run \
        --rm \
        --name certbot \
        --net=host \
        -v "${SCRIPT_DIR}/log:/var/log" \
        -v "${SCRIPT_DIR}/etc:/etc/letsencrypt" \
        -v "${SCRIPT_DIR}/var:/var/lib/letsencrypt" \
        -v "${SCRIPT_DIR}/certbot:/var/lib/certbot" \
        certbot/dns-cloudflare \
        certonly \
        --non-interactive \
        --dns-cloudflare \
        --dns-cloudflare-credentials /etc/letsencrypt/credentials \
        --dns-cloudflare-propagation-seconds 120 \
        --agree-tos \
        --email mwhlfrt@gmail.com \
        -d \*.${DOMAIN} \
        --server https://acme-v02.api.letsencrypt.org/directory

    ls -alr "${SCRIPT_DIR}"
    rm "${SCRIPT_DIR}/etc/credentials"
}

function create_config() {
    cat >"${SCRIPT_DIR}/secrets.yaml" <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: wired-heart-tls
  namespace: development
data:
EOF
    {
    printf "\n  tls.crt: "
    base64 -w 0 < "${SCRIPT_DIR}/etc/live/${DOMAIN}/fullchain.pem"
    printf "\n  tls.key: "
    base64 -w 0 < "${SCRIPT_DIR}/etc/live/${DOMAIN}/privkey.pem"
    printf "\n\ntype: kubernetes.io/tls\n"
    } >>"${SCRIPT_DIR}/secrets.yaml"
}


#################
#   main
#################

# create_cert
# convert_pem
create_config

exit 0


echo "Uploads the certificate and gets the Thumbprint"
THUMBPRINT=$(az functionapp config ssl upload --certificate-file $DOMAIN.pfx --certificate-password pwd123 -n $FUNCTION_APP -g $RESOURCE_GROUP | jq --raw-output '.|.thumbprint')

echo "Binds the funciton app to the certificate"
az functionapp config ssl bind --certificate-thumbprint $THUMBPRINT --ssl-type SNI -n $FUNCTION_APP -g $RESOURCE_GROUP

ls -l

echo "Cleans Up Files"
rm ./cftoken
rm $DOMAIN.pfx

az keyvault secret set \
    --name SecretPassword \
    --value reindeer_flotilla \
    --vault-name your-unique-vault-name

# to check the token
curl -X \
    GET "https://api.cloudflare.com/client/v4/user/tokens/verify" \
    -H "Authorization: Bearer GerhwqHsooFIEVzX2DPgcxqbPtpj4jR_PQK0M8ne" \
    -H "Content-Type:application/json"

# sudo certbot certonly --dns-cloudflare --dns-cloudflare-credentials /root/.secrets/cloudflare.ini -d example.com,*.example.com --preferred-challenges dns-01
