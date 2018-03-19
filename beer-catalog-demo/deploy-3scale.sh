oc login <openshift-cluster-url> -u <user> -p <password>

oc project beer-catalog-prod

oc secret new-basicauth threescale-portal-endpoint-secret --password=https://<3scale-token>@<3scale-user>.3scale.net
oc secret new-basicauth apicast-configuration-url-secret --password=https://<3scale-token>@<3scale-user>.3scale.net

oc new-app 3scale-gateway \
  --param=APICAST_NAME=beer-catalog-api \
  --param=LOG_LEVEL=info \
  --param=RESPONSE_CODES=true

oc expose service/beer-catalog-api --name beer-catalog-api
