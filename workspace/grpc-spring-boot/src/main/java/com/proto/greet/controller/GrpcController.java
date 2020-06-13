package com.proto.greet.controller;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetingGrpc;
import com.proto.greet.service.GreetService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class GrpcController extends GreetingGrpc.GreetingImplBase {

    @Autowired
    GreetService greetService;

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
         responseObserver.onNext(greetService.greet(request));
         responseObserver.onCompleted();
    }
}
