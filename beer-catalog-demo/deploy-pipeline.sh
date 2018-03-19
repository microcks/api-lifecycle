oc login <openshift-cluster-url> -u <user> -p <password>

oc new-project beer-catalog-prod --display-name="Beer Catalog (PROD)"

oc adm policy add-role-to-user edit system:serviceaccount:microcks:jenkins -n beer-catalog-dev
oc adm policy add-role-to-user edit system:serviceaccount:microcks:jenkins -n beer-catalog-prod

oc adm policy add-role-to-group system:image-puller system:serviceaccounts:beer-catalog-prod -n beer-catalog-dev

oc get dc beer-catalog-impl -o json -n beer-catalog-dev | jq '.spec.triggers |= []' | oc replace -f -
oc patch dc/beer-catalog-impl --type=json -p '[{"op":"add", "path":"/spec/template/spec/containers/0/readinessProbe", "value": {"failureThreshold": 3, "httpGet": { "path": "/api/beer?page=0", "port": 8080, "scheme": "HTTP" }, "initialDelaySeconds": 1, "periodSeconds": 10, "successThreshold": 1, "timeoutSeconds": 1}}]' -n beer-catalog-dev

oc create deploymentconfig beer-catalog-impl --image=docker-registry.default.svc:5000/beer-catalog-dev/beer-catalog-impl:promoteToProd -n beer-catalog-prod
oc rollout cancel dc/beer-catalog-impl -n beer-catalog-prod
oc get dc beer-catalog-impl -o json -n beer-catalog-prod | jq '.spec.triggers |= []' | oc replace -f -
oc get dc beer-catalog-impl -o yaml -n beer-catalog-prod | sed 's/imagePullPolicy: IfNotPresent/imagePullPolicy: Always/g' | oc replace -f -
oc expose dc beer-catalog-impl --port=8080 -n beer-catalog-prod
oc expose svc beer-catalog-impl -n beer-catalog-prod

oc login <openshift-cluster-url> -u <user> -p <password>

oc create -f pipeline-template.yml -n microcks

oc start-build beer-catalog-pipeline -n microcks
