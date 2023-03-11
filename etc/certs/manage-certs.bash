#!/usr/bin/env bash
#
# script to re-generate the cert for wired-heart.com
#  The idea is we update the cloudflare dns config to prove to letsencrypt
#  that we own the domain in order to get the cert.
#
#  There is a docker image that can be used to run the steps we just have to provide the CLOUDFLARE_API_TOKEN
#
# this script can be called with
#
#   create_cert:
#      to create a new cert from letsencrypt, encrypt them and push them to github
#
#   create_secrets:
#      to create secrets.yaml for k8s deployment from the encrypted certs
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


function usage_exit {
    echo "Error: usage ${0} [create_cert|create_secrets]" >&2
    exit 2
}

# for running local we read content from file,
# otherwise it should be in the env already when running as GitHub action

if [[ -z "${GPG_PASSPHRASE}" ]]; then
    GPG_PASSPHRASE=$(cat "${SCRIPT_DIR}/../setup/gpg-passphrase.txt")
fi

# ===== main building blocks


#
# this refreshes the cert keys from letsencrypt and stores them
# in 2 encrypted files:
#    fullchain.pem -> tls.crt.bin
#    privkey.pem   -> tls.key.bin
#
function create_cert {

    if [[ -z "${CLOUDFLARE_API_TOKEN}" ]]; then
        CLOUDFLARE_API_TOKEN=$(cat "${SCRIPT_DIR}/../setup/cloudflare-api-token.txt")
    fi

    # this is where the keys are stored, the dirs are pretty much defined by the certbot
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
        --server https://acme-v02.api.letsencrypt.org/directory

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
    # sudo cat "${SCRIPT_DIR}/etc/live/${DOMAIN}/fullchain.pem"

    echo "---privkey---"
    # sudo cat "${SCRIPT_DIR}/etc/live/${DOMAIN}/privkey.pem"

    echo
    echo "---tls.crt.bin---"
    sudo cat "${SCRIPT_DIR}/${TSL_CRT_FILE}.bin"

    echo
    echo "---tls.key.bin---"
    sudo cat "${SCRIPT_DIR}/${TSL_KEY_FILE}.bin"
}


function create_secrets {
    SECRETS_YAML="${SCRIPT_DIR}/../helm/charon/templates/secrets.yaml"
    echo " writing cert keys..."

    gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${DOMAIN}-fullchain.pem" \
        --decrypt "${SCRIPT_DIR}/${TSL_CRT_FILE}.bin"

    gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${DOMAIN}-privkey.pem" \
        --decrypt "${SCRIPT_DIR}/${TSL_KEY_FILE}.bin"

    rm -f "${SECRETS_YAML}"
    cat > "${SECRETS_YAML}" <<EOF
apiVersion: v1
kind: Secret
type: kubernetes.io/tls
metadata:
  name: wired-heart-tls
  namespace: development

data:
EOF
    {
    printf "  tls.crt: "
    base64 -w 0 < "${SCRIPT_DIR}/${DOMAIN}-fullchain.pem"
    printf "\n"

    printf "  tls.key: "
    base64 -w 0 < "${SCRIPT_DIR}/${DOMAIN}-privkey.pem"
    printf "\n"

    } >> "${SECRETS_YAML}"

    echo "...cert keys written, appending secrets..."
    cat >> "${SECRETS_YAML}" <<EOF

---

apiVersion: v1
kind: Secret
type: Opaque
metadata:
  name: wired-heart-secrets
  namespace: development

data:
EOF
    {
    printf "  jasypt-encryptor-password: %s\n" "${GPG_PASSPHRASE}"
    } >> "${SECRETS_YAML}"
    echo "... finished appending secrets"

}

#################
#   main
#################

if [[ $# -ne 1 ]]; then
    usage_exit
fi

case ${1} in

  "create_cert")
    create_cert
    ;;

  "create_secrets")
    create_secrets
    ;;

  *)
    usage_exit
    ;;
esac

exit 0
