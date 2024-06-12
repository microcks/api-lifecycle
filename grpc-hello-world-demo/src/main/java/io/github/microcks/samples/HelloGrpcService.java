package io.github.microcks.samples;

import io.quarkus.grpc.GrpcService;

import io.smallrye.mutiny.Uni;

@GrpcService
public class HelloGrpcService implements HelloService {

    @Override
    public Uni<HelloResponse> greeting(HelloRequest request) {
        return Uni.createFrom().item("Hello " + request.getFirstname() + " " + request.getLastname() + "!")
                .map(msg -> HelloResponse.newBuilder().setGreeting(msg).build());
    }

}
