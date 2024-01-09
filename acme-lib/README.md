## Main webapp extension

### Docker compose mount

```sh
docker-compose -f docker-compose-mount.yml up -d
```

```groovy
def java = new org.acme.lib.Greeting();
def groovy = new org.acme.lib.GroovyGreeting();

log.info java.greet("World")
log.info groovy.greet("My Dear")

return "pastries_json"
```

```
08:47:26.491 DEBUG 1 --- [80-exec-10] i.github.microcks.web.RestController     : Found a valid operation GET /pastry with rules: def java = new org.acme.lib.Greeting();
def groovy = new org.acme.lib.GroovyGreeting();

log.info java.greet("World")
log.info groovy.greet("my Dear")

return "pastries_json"
08:47:27.272  INFO 1 --- [80-exec-10] i.g.m.util.script.ScriptEngineBinder     : Hello World!
08:47:27.279  INFO 1 --- [80-exec-10] i.g.m.util.script.ScriptEngineBinder     : Groovy my Dear!
08:47:27.279 DEBUG 1 --- [80-exec-10] i.github.microcks.web.RestController     : Dispatch criteria for finding response is pastries_json
```

```sh
docker-compose -f docker-compose-mount.yml down
```

### Custom image

``` sh
docker build -f Dockerfile.acme -t acme/microcks-ext:nightly .
```

```sh
docker-compose -f docker-compose-acme.yml up -d 
```

```
08:39:01.062 DEBUG 1 --- [080-exec-6] i.github.microcks.web.RestController     : Found a valid operation GET /pastry with rules: def java = new org.acme.lib.Greeting();
def groovy = new org.acme.lib.GroovyGreeting();

log.info java.greet("World")
log.info groovy.greet("My Dear")

return "pastries_json"
08:39:01.433  INFO 1 --- [080-exec-6] i.g.m.util.script.ScriptEngineBinder     : Hello World!
08:39:01.437  INFO 1 --- [080-exec-6] i.g.m.util.script.ScriptEngineBinder     : Groovy My Dear!
08:39:01.438 DEBUG 1 --- [080-exec-6] i.github.microcks.web.RestController     : Dispatch criteria for finding response is pastries_json
```

```sh
docker-compose -f docker-compose-acme.yml down
```

## Async Minion extension

### Docker compose mount

```sh
docker-compose -f docker-compose-mount-async.yml up -d
```

```sh
$ docker ps
=== OUTPUT ===
CONTAINER ID   IMAGE                                            COMMAND                  CREATED         STATUS         PORTS                                                                       NAMES
5d314d3bf8b0   quay.io/microcks/microcks-async-minion:nightly   "/deployments/run-ja…"   5 seconds ago   Up 1 second    8080/tcp, 0.0.0.0:8081->8081/tcp                                            microcks-async-minion
052dd9777229   quay.io/microcks/microcks:nightly                "/deployments/run-ja…"   6 seconds ago   Up 5 seconds   0.0.0.0:8080->8080/tcp, 8778/tcp, 0.0.0.0:9090->9090/tcp, 9779/tcp          microcks
5ec66cc0910d   mongo:3.6.23                                     "docker-entrypoint.s…"   6 seconds ago   Up 5 seconds   27017/tcp                                                                   microcks-db
ca98a4b0ed9e   vectorized/redpanda:v22.2.2                      "/entrypoint.sh redp…"   6 seconds ago   Up 5 seconds   8081-8082/tcp, 0.0.0.0:9092->9092/tcp, 9644/tcp, 0.0.0.0:19092->19092/tcp   microcks-kafka
```

```
2024-01-09 12:46:08,568 INFO  [io.sma.rea.mes.kafka] (main) SRMSG18229: Configured topics for channel 'microcks-services-updates': [microcks-services-updates]
Handling the callback...
2024-01-09 12:46:08,641 INFO  [org.apa.kaf.com.sec.oau.int.exp.ExpiringCredentialRefreshingLogin] (smallrye-kafka-consumer-thread-0) Successfully logged in.
```

```sh
docker-compose -f docker-compose-mount-async.yml down
```

### Custom image + docker compose mount for properties

``` sh
docker build -f Dockerfile.acme.minion -t acme/microcks-async-minion-ext:nightly .
```

```sh
docker-compose -f docker-compose-acme-async.yml up -d
```

```sh
$ docker ps
=== OUTPUT ===
CONTAINER ID   IMAGE                                    COMMAND                  CREATED         STATUS         PORTS                                                                       NAMES
9349ef4c43c9   acme/microcks-async-minion-ext:nightly   "/deployments/run-ja…"   4 seconds ago   Up 2 seconds   8080/tcp, 0.0.0.0:8081->8081/tcp                                            microcks-async-minion
513540c57512   quay.io/microcks/microcks:nightly        "/deployments/run-ja…"   4 seconds ago   Up 3 seconds   0.0.0.0:8080->8080/tcp, 8778/tcp, 0.0.0.0:9090->9090/tcp, 9779/tcp          microcks
fc4d95eb2222   mongo:3.6.23                             "docker-entrypoint.s…"   4 seconds ago   Up 3 seconds   27017/tcp                                                                   microcks-db
3cf96a1a85a7   vectorized/redpanda:v22.2.2              "/entrypoint.sh redp…"   4 seconds ago   Up 3 seconds   8081-8082/tcp, 0.0.0.0:9092->9092/tcp, 9644/tcp, 0.0.0.0:19092->19092/tcp   microcks-kafka
```


```
2024-01-09 09:09:22,399 INFO  [io.sma.rea.mes.kafka] (main) SRMSG18229: Configured topics for channel 'microcks-services-updates': [microcks-services-updates]
Handling the callback...
2024-01-09 09:09:22,566 INFO  [org.apa.kaf.com.sec.oau.int.exp.ExpiringCredentialRefreshingLogin] (smallrye-kafka-consumer-thread-0) Successfully logged in.
```

```sh
docker-compose -f docker-compose-acme-async.yml down
```