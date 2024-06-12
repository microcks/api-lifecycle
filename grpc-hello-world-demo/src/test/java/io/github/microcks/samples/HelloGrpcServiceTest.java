package io.github.microcks.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

@QuarkusTest
class HelloGrpcServiceTest {
    @GrpcClient
    HelloService helloGrpc;

    @Test
    void testHello() {
        HelloResponse reply = helloGrpc
                .greeting(HelloRequest.newBuilder().setFirstname("Thomas").setLastname("Anderson").build()).await().atMost(Duration.ofSeconds(5));
        assertEquals("Hello Thomas Anderson!", reply.getGreeting());
    }

}
