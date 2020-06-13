package com.grpc.greeting;

import com.proto.hello.HelloRequest;
import com.proto.hello.HelloResponse;
import com.proto.hello.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(HelloResponse.newBuilder()
                .setResult("Hello " + request.getName())
                .build());

        responseObserver.onCompleted();
    }
}
