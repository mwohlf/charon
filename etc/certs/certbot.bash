#
#
# https://hub.docker.com/r/certbot/dns-cloudflare
# https://certbot-dns-cloudflare.readthedocs.io/en/stable/#credentials
# https://www.nodinrogers.com/post/2022-03-10-certbot-cloudflare-docker/
#
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

echo "dns_cloudflare_api_token = ${CLOUDFLARE_TOKEN}" > ./etc/credentials
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
  -d \*.wired-heart.com \
  --server https://acme-v02.api.letsencrypt.org/directory \
  --dry-run

exit 0





echo "Renews the certificates"
sudo certbot certonly --dns-cloudflare --dns-cloudflare-credentials ./cftoken -d $DOMAIN -m $CB_EMAIL --config-dir . --cert-path . --non-interactive --agree-tos

# Certbot runs as root, so it creates all the files as root. This changes the permissions so that other utilities can read the file.
echo "Set file permissions"
sudo chmod -R 777 ./*

echo "Creates a PFX for Azure"
openssl pkcs12 -inkey ./live/$DOMAIN/privkey.pem -in live/$DOMAIN/fullchain.pem -export -out $DOMAIN.pfx -passout pass:pwd123

echo "Login to Azure with a Service Principal"
az login --service-principal -u $SP_CLIENT_ID -p $SP_SECRET --tenant $TENANT_ID

echo "Uploads the certificate and gets the Thumbprint"
THUMBPRINT=$(az functionapp config ssl upload --certificate-file $DOMAIN.pfx --certificate-password pwd123 -n $FUNCTION_APP -g $RESOURCE_GROUP | jq --raw-output '.|.thumbprint')

echo "Binds the funciton app to the certificate"
az functionapp config ssl bind --certificate-thumbprint $THUMBPRINT --ssl-type SNI -n $FUNCTION_APP -g $RESOURCE_GROUP

ls -l

echo "Cleans Up Files"
rm ./cftoken
rm $DOMAIN.pfx


echo "Creates a PFX for Azure"
openssl pkcs12 -inkey ./live/$DOMAIN/privkey.pem -in live/$DOMAIN/fullchain.pem -export -out $DOMAIN.pfx -passout pass:pwd123

echo "Login to Azure with a Service Principal"
az login --service-principal -u $SP_CLIENT_ID -p $SP_SECRET --tenant $TENANT_ID

echo "Uploads the certificate and gets the Thumbprint"
THUMBPRINT=$(az functionapp config ssl upload --certificate-file $DOMAIN.pfx --certificate-password pwd123 -n $FUNCTION_APP -g $RESOURCE_GROUP | jq --raw-output '.|.thumbprint')

echo "Binds the funciton app to the certificate"
az functionapp config ssl bind --certificate-thumbprint $THUMBPRINT --ssl-type SNI -n $FUNCTION_APP -g $RESOURCE_GROUP



# check the token

curl -X \
  GET "https://api.cloudflare.com/client/v4/user/tokens/verify" \
 -H "Authorization: Bearer GerhwqHsooFIEVzX2DPgcxqbPtpj4jR_PQK0M8ne" \
 -H "Content-Type:application/json"


# sudo certbot certonly --dns-cloudflare --dns-cloudflare-credentials /root/.secrets/cloudflare.ini -d example.com,*.example.com --preferred-challenges dns-01

