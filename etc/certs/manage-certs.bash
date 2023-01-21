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
# this script creates two tsl config files:
#
#    tls.crt.bin
#    tls.key.bin
#


set -e


# ===== global variables

TSL_CRT_FILE="tls.crt"
TSL_KEY_FILE="tls.key"
DOMAIN="wired-heart.com"
# KEYVAULT="finalrestingheartrateVlt"


CURRENT_DIR="${PWD}"
SCRIPT_DIR="$(
    cd -- "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)"
function finally {
    echo
    echo "finishing the script, error code is ${?}"
    # back to where we came from
    cd "${CURRENT_DIR}"
}
trap finally EXIT

# for running local we read content from file,
# otherwise it should be in the env already when running as GitHub action

if [[ -z "${GPG_PASSPHRASE}" ]]; then
    GPG_PASSPHRASE=$(cat "${SCRIPT_DIR}/../setup/gpg-passphrase.txt")
fi

# ===== main building blocks

function create_cert() {

    if [[ -z "${CLOUDFLARE_API_TOKEN}" ]]; then
        CLOUDFLARE_API_TOKEN=$(cat "${SCRIPT_DIR}/../setup/cloudflare-api-token.txt")
    fi

    # this is where the keys are stored the dirs are pretty much defined by the certbot
    mkdir -p "${SCRIPT_DIR}/etc/certs/etc/live"
    echo "dns_cloudflare_api_token = ${CLOUDFLARE_API_TOKEN}" >"${SCRIPT_DIR}/etc/credentials"
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
        --server https://acme-staging-v02.api.letsencrypt.org/directory

# for staging:   --server https://acme-staging-v02.api.letsencrypt.org/directory
# for prod:      --server https://acme-v02.api.letsencrypt.org/directory

    # sudo ls -alR "${SCRIPT_DIR}"

    sudo gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${TSL_CRT_FILE}.bin" \
        --symmetric \
        "${SCRIPT_DIR}/etc/live/${DOMAIN}/fullchain.pem"
    sudo chmod 777 "${SCRIPT_DIR}/${TSL_CRT_FILE}.bin"

    sudo gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${TSL_KEY_FILE}.bin" \
        --symmetric \
        "${SCRIPT_DIR}/etc/live/${DOMAIN}/privkey.pem"
    sudo chmod 777 "${SCRIPT_DIR}/${TSL_KEY_FILE}.bin"

    # sudo cat "${SCRIPT_DIR}/log/letsencrypt/letsencrypt.log"

    echo "---fullchain---"
    sudo cat "${SCRIPT_DIR}/etc/live/${DOMAIN}/fullchain.pem"

    echo "---privkey---"
    sudo cat "${SCRIPT_DIR}/etc/live/${DOMAIN}/privkey.pem"

    echo
    echo "---tls.crt.bin---"
    sudo cat "${SCRIPT_DIR}/${TSL_CRT_FILE}.bin"

    echo
    echo "---tls.key.bin---"
    sudo cat "${SCRIPT_DIR}/${TSL_KEY_FILE}.bin"
}


function create_values() {

    gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${DOMAIN}-fullchain.pem" \
        --decrypt "${SCRIPT_DIR}/${TSL_CRT_FILE}.bin"

    gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${DOMAIN}-privkey.pem" \
        --decrypt "${SCRIPT_DIR}/${TSL_KEY_FILE}.bin"

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
    base64 -w 0 < "${SCRIPT_DIR}/${DOMAIN}-fullchain.pem"
    printf "\n  tls.key: "
    base64 -w 0 < "${SCRIPT_DIR}/${DOMAIN}-privkey.pem"
    printf "\n\ntype: kubernetes.io/tls\n"
    } >>"${SCRIPT_DIR}/secrets.yaml"

    cp "${SCRIPT_DIR}/secrets.yaml" "${SCRIPT_DIR}/../helm/charon/values.yaml"
}

#################
#   main
#################

if [[ $# -ne 1 ]]; then
    echo "Error: usage ${0} [create_cert|create_secrets]" >&2
    exit 1
fi

if [[ ${1} == "create_cert" ]]; then
    create_cert
fi

if [[ ${1} == "create_values" ]]; then
    create_values
fi

exit 0
