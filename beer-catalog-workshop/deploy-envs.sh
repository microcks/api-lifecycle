USER=
LABEL_SUFFIX=
DEV_ENV=beer-catalog-dev
TEST_ENV=beer-catalog-test
PROD_ENV=beer-catalog-prod
MICROCKS_PROJECT=microcks

if [ -n "$1" ]; then
  USER=$1
  DEV_ENV=${USER}-${DEV_ENV}
  TEST_ENV=${USER}-${TEST_ENV}
  PROD_ENV=${USER}-${PROD_ENV}
  LABEL_SUFFIX=" - ${USER}"
fi

if [ -n "$2" ]; then
  MICROCKS_PROJECT=$2
fi

oc new-project ${TEST_ENV} --display-name="Beer Catalog (TEST)"
oc new-project ${PROD_ENV} --display-name="Beer Catalog (PROD)"

oc adm policy add-role-to-user edit system:serviceaccount:${MICROCKS_PROJECT}:jenkins -n ${DEV_ENV}
oc adm policy add-role-to-user edit system:serviceaccount:${MICROCKS_PROJECT}:jenkins -n ${TEST_ENV}
oc adm policy add-role-to-user edit system:serviceaccount:${MICROCKS_PROJECT}:jenkins -n ${PROD_ENV}

oc adm policy add-role-to-group system:image-puller system:serviceaccounts:${TEST_ENV} -n ${DEV_ENV}
oc adm policy add-role-to-group system:image-puller system:serviceaccounts:${PROD_ENV} -n ${DEV_ENV}

oc get dc beer-catalog-impl -o json -n ${DEV_ENV} | jq '.spec.triggers |= []' | oc replace -f -
oc patch dc/beer-catalog-impl --type=json -p '[{"op":"add", "path":"/spec/template/spec/containers/0/readinessProbe", "value": {"failureThreshold": 3, "httpGet": { "path": "/api/beer?page=0", "port": 8080, "scheme": "HTTP" }, "initialDelaySeconds": 1, "periodSeconds": 10, "successThreshold": 1, "timeoutSeconds": 1}}]' -n ${DEV_ENV}

oc create deploymentconfig beer-catalog-impl --image=docker-registry.default.svc:5000/${DEV_ENV}/beer-catalog-impl:promoteToTest -n ${TEST_ENV}
oc rollout cancel dc/beer-catalog-impl -n ${TEST_ENV}
oc get dc beer-catalog-impl -o json -n ${TEST_ENV} | jq '.spec.triggers |= []' | oc replace -f -
oc get dc beer-catalog-impl -o yaml -n ${TEST_ENV} | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc expose dc beer-catalog-impl --port=8080 -n ${TEST_ENV}
oc expose svc beer-catalog-impl -n ${TEST_ENV}

oc create deploymentconfig beer-catalog-impl --image=docker-registry.default.svc:5000/${DEV_ENV}/beer-catalog-impl:promoteToProd -n ${PROD_ENV}
oc rollout cancel dc/beer-catalog-impl -n ${PROD_ENV}
oc get dc beer-catalog-impl -o json -n ${PROD_ENV} | jq '.spec.triggers |= []' | oc replace -f -
oc get dc beer-catalog-impl -o yaml -n ${PROD_ENV} | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc expose dc beer-catalog-impl --port=8080 -n ${PROD_ENV}
oc expose svc beer-catalog-impl -n ${PROD_ENV}
