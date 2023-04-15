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
#      to create secrets.yaml for k8s deployment from the encrypted certs plus some
#      values from the environment
#

set -e


# ===== global variables

TSL_CRT_FILE="tls.crt"
TSL_KEY_FILE="tls.key"
SETENV_FILE="setup_env.bash"

DOMAIN="wired-heart.com"
# KEYVAULT="finalrestingheartrateVlt"


CURRENT_DIR="${PWD}"
SCRIPT_DIR="$(
    cd -- "$(dirname "$0")" >/dev/null 2>&1
    pwd -P
)"
function finally {
    EXIT_CODE="${?}"
    if [[ ${EXIT_CODE} -ne "0" ]]; then
        echo
        echo "finishing the script, error code is ${?}"
    fi
    # back to where we came from
    cd "${CURRENT_DIR}"
}
trap finally EXIT


function usage_exit {
    echo "Error: usage ${0} [create_cert|create_secrets|encrypt_env]" >&2
    exit 2
}

# for running local we read content from file,
# otherwise it should be in the env already when running as GitHub action

if [[ -z "${GPG_PASSPHRASE}" ]]; then
    GPG_PASSPHRASE=$(cat "${SCRIPT_DIR}/../setup/gpg-passphrase.txt")
fi

# the file to create containing our secrets, we can't check that in so we have to create it before deployment
SECRETS_YAML="${SCRIPT_DIR}/../helm/charon/templates/secrets.yaml"

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
    mkdir -p "${SCRIPT_DIR}/etc/secrets/etc/live"
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


# secret file consists of 2 sections
# - tls config
# - secret values
function create_secrets {
    rm -f "${SECRETS_YAML}"
    cat > "${SECRETS_YAML}" <<EOF
EOF
}


function add_cert_keys {
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

    echo "...cert keys written"
}


function add_env_values {
    echo "decrypt env file..."

    gpg --quiet --batch --yes \
        --passphrase="${GPG_PASSPHRASE}" \
        --output "${SCRIPT_DIR}/${SETENV_FILE}" \
        --decrypt "${SCRIPT_DIR}/${SETENV_FILE}.bin"

    echo "source env file..."
    # shellcheck source=./env.txt
    source "${SCRIPT_DIR}/${SETENV_FILE}"
    echo "display env"
    env

    cat >> "${SECRETS_YAML}" <<EOF

---

apiVersion: v1
kind: Secret
type: Opaque
metadata:
  name: wired-heart-secrets
  namespace: development

stringData:
EOF
    { # we need to quote the strings because yaml returns number for [0-9]+ parameters
    printf "  redis-password: '%s'\n" "${REDIS_PASSWORD}"
    printf "  spring-mail-host: '%s'\n" "${SPRING_MAIL_HOST}"
    printf "  spring-mail-username: '%s'\n" "${SPRING_MAIL_USERNAME}"
    printf "  spring-mail-password: '%s'\n" "${SPRING_MAIL_PASSWORD}"
    printf "  spring-mail-port: '%s'\n" "${SPRING_MAIL_PORT}"
    printf "  postgres-user: '%s'\n" "${SPRING_MAIL_PORT}"
    printf "  postgres-password: '%s'\n" "${SPRING_MAIL_PORT}"
    } >> "${SECRETS_YAML}"
    echo "...finished appending secrets"

}


function encrypt_env {
    cd "${SCRIPT_DIR}"
    echo "encrypting ${SETENV_FILE} -> ${SETENV_FILE}.bin"
    gpg --quiet --batch --yes \
            --passphrase="${GPG_PASSPHRASE}" \
            --output "./${SETENV_FILE}.bin" \
            --symmetric \
            "./${SETENV_FILE}"
    echo "done"
}


function do_select {
    select opt in "create certs from letsencrypt (needs docker installed)" \
                  "create the secrets.yaml file for the helm config" \
                  "encrypt the environment file config for git checkin"
    do
        echo "you selected: ${REPLY} for ${opt}"
        case ${REPLY} in
            "1")
                echo "executing: ${0} create_cert..." && \
                $0 create_cert && \
                exit 0
                ;;
            "2")
                echo "executing: ${0} create_secrets..." && \
                $0 create_secrets && \
                exit 0
                ;;
            "3")
                echo "executing: ${0} encrypt_env..." && \
                $0 encrypt_env && \
                exit 0
                ;;
            *)
                exit 0
                ;;
        esac
    done
}

#################
#   main
#################

if [[ $# -eq 0 ]]; then
    do_select
fi

if [[ $# -ne 1 ]]; then
    usage_exit
fi

case ${1} in

    # creating a docker container to create certs from letsencrypt
    "create_cert")
        create_cert
        ;;

    # create the secrets.yaml file
    "create_secrets")
        create_secrets
        add_cert_keys
        add_env_values
        ;;

    "encrypt_env")
        encrypt_env
        ;;

    *)
        usage_exit
        ;;
esac

exit 0


#
#!manage-secrets.bash
