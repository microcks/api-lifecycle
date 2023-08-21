docker run -it quay.io/microcks/microcks-cli:latest microcks-cli test 'Order Service API:0.1.0' \
    http://host.docker.internal:8080/api OPEN_API_SCHEMA \
    --microcksURL=http://host.docker.internal:9090/api/ \
    --keycloakClientId=foo --keycloakClientSecret=bar --insecure --waitFor=8sec
    