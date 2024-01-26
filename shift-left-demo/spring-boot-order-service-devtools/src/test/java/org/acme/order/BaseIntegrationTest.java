package org.acme.order;

import io.github.microcks.testcontainers.MicrocksContainersEnsemble;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ContainersConfiguration.class)
public abstract class BaseIntegrationTest {

   @Autowired
   protected MicrocksContainersEnsemble microcksEnsemble;

   @LocalServerPort
   protected Integer port;

   @BeforeEach
   void setupPort() {
      // Host port exposition should be done here.
      Testcontainers.exposeHostPorts(port);
   }
}
