package com.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public void run() throws SSLException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                                                        .usePlaintext()
                                                        .build();

        ManagedChannel secureChannel = NettyChannelBuilder.forAddress("localhost", 50051)
                                                            .sslContext(GrpcSslContexts.forClient().trustManager(new File("ssl/ca.crt")).build())
                                                            .build();

        doUnary(secureChannel);
        // doServerStreaming(channel);
        // doClientStreaming(channel);
        // doBidiStreaming(channel);
        // doDeadlineCall(channel);

        System.out.println("Shutting down Channel");
        channel.shutdown();
    }

    private void doDeadlineCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub blockingStub = GreetServiceGrpc.newBlockingStub(channel);

        GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(550, TimeUnit.MILLISECONDS))
                                                         .greetWithDeadline(GreetWithDeadlineRequest.newBuilder()
                                                                                                    .setGreeting(Greeting.newBuilder().setFirstName("Leela").build())
                                                                                                    .build());


        System.out.println(response);
    }

    private void doBidiStreaming(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncGreetClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetEveryoneRequest> requestObserver = asyncGreetClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse value) {
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList("Leela","Jagu","Prasad").forEach(name -> {
            System.out.println("Sending " + name);
            requestObserver.onNext(GreetEveryoneRequest.newBuilder()
                                                        .setGreeting(Greeting.newBuilder()
                                                                .setFirstName(name)
                                                                .build())
                                                        .build());

            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        requestObserver.onCompleted();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doClientStreaming(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncGreetClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncGreetClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                // we get a response from the server
                // Here in client streaming it will be called only once
                System.out.println("Received Response from server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from the server
            }

            @Override
            public void onCompleted() {
                // server is done sending data
                // onCompleted will be called right after onNext()
                System.out.println("Server has completed sending data");
                latch.countDown();
            }
        });

        // streaming message #1
        System.out.println("Sending Message1");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                                                .setGreeting(Greeting.newBuilder().setFirstName("Leela").build())
                                                .build());

        // streaming message #2
        System.out.println("Sending Message2");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                                                .setGreeting(Greeting.newBuilder().setFirstName("Jagu").build())
                                                .build());

        // streaming message #3
        System.out.println("Sending Message3");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                                                .setGreeting(Greeting.newBuilder().setFirstName("Prasad").build())
                                                .build());

        // we tell server that client is done sending data
        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doServerStreaming(ManagedChannel channel) {
        // created a Greet Service Client (blocking - synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Leela")
                .build();

        Iterator<GreetManyTimesResponse> iterator = greetClient.greetManyTimes(GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build());

        iterator.forEachRemaining(response -> {
            System.out.println(response.getResult());
        });
    }

    private void doUnary(ManagedChannel channel) {
        // created a Greet Service Client (blocking - synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                                    .setFirstName("Leela")
                                    .setLastName("Jagu")
                                    .build();

        // GreetRequest
        GreetRequest request = GreetRequest.newBuilder()
                                            .setGreeting(greeting)
                                            .build();

        // call RPC and get back GreetResponse (protocol buffers)
        GreetResponse response = greetClient.greet(request);

        System.out.println(response.getResult());
    }

    public static void main(String[] args) throws SSLException {
        GreetingClient greetingClient = new GreetingClient();
        greetingClient.run();
    }
}
