#!/usr/bin/env bash
#
# script to re-generate the cert for wired-heart.com
#  the idea is we update the cloudflare dns config to prove to letsencrypt that we own the domain
#  then we get a certificate...
#   - make sure the API Token is still valid in .../setup/cloudflare-token.txt
#     you can roll a new token: https://dash.cloudflare.com/profile/api-tokens
#   - run this script with access to cloudflare (use a custom ubuntu 22 WSL image, not the standard BB image)
#     the standard BB ubuntu WSL image might have a router/firewall setting that does not allow access to cloudflare
#   - the secrets.yaml file will be generated
#   - copy secrets into the values config in the helm chart
#
# todo: store the secrets inside the github vault and use k8sbake to insert them into the value file of the chart

set -e


DOMAIN="wired-heart.com"
# KEYVAULT="finalrestingheartrateVlt"

CURRENT_DIR="${PWD}"
SCRIPT_DIR="$(
    cd -- "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)"
function finally {
    echo "finishing the script, error code is ${?}"
    # back to where we came from
    cd "${CURRENT_DIR}"
}
trap finally EXIT

function create_cert() {
    # for running local
    if [[ -z "${CLOUDFLARE_TOKEN}" ]]; then
        CLOUDFLARE_TOKEN=$(cat "${SCRIPT_DIR}/../setup/cloudflare-token.txt")
    fi

    mkdir -p "${SCRIPT_DIR}/etc"
    echo "dns_cloudflare_api_token = ${CLOUDFLARE_TOKEN}" >"${SCRIPT_DIR}/etc/credentials"
    # chmod 400 "${SCRIPT_DIR}/etc/credentials"

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

create_cert
create_config

exit 0
