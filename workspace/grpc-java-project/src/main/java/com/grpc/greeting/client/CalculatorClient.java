package com.grpc.greeting.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class CalculatorClient {

    public void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                                                        .usePlaintext()
                                                        .build();

        // doUnary(channel);
        // doServerStreaming(channel);
        // doClientStreaming(channel);
        // doBidiStreaming(channel);
        doErrorCall(channel);

        channel.shutdown();
    }

    private void doErrorCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        int number = -19;

        /*try {
            SquareRootResponse response = blockingStub.squareRoot(SquareRootRequest.newBuilder()
                    .setNumber(number)
                    .build());
            System.out.println("Response from server: " + response.getNumberRoot());
        } catch (StatusRuntimeException e) {
            System.out.println("Exception occured while calling square root service");
            e.printStackTrace();
        }*/

        SquareRootResponse response = blockingStub.squareRoot(SquareRootRequest.newBuilder()
                .setNumber(number)
                .build());
        System.out.println("Response from server: " + response);

    }

    private void doBidiStreaming(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncCalculatorClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<FindMaximumRequest> requestObserver = asyncCalculatorClient.findMaximum(new StreamObserver<FindMaximumResponse>() {
            @Override
            public void onNext(FindMaximumResponse value) {
                System.out.println("Maximum value received from the server " + value.getMaximum());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server completed sending data");
                latch.countDown();
            }
        });

        Arrays.asList(3, 5, 17, 19, 8, 30, 12).forEach(number -> {
            System.out.println("Sending Number " + number);
            requestObserver.onNext(FindMaximumRequest.newBuilder().setNumber(number).build());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
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

    private void doServerStreaming(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        PrimeNumberDecompositionRequest request = PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(345678901)
                .build();

        Iterator<PrimeNumberDecompositionResponse> iterator = calculatorClient.primeNumberDecomposition(request);

        iterator.forEachRemaining(primeFactor -> System.out.println(primeFactor.getPrimeFactor()));
    }

    private void doClientStreaming(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncCalculatorClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver = asyncCalculatorClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Received Response from server");
                System.out.println("Average: " + value.getAverage());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending data");
                latch.countDown();
            }
        });

        // requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(1).build());
        // requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(2).build());
        // requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(3).build());
        // requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(4).build());

        System.out.println("Sending Numbers");
        for(int i=1; i<1000; ++i) {
            requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(i).build());
        }

        requestObserver.onCompleted();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doUnary(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        CalculatorRequest request = CalculatorRequest.newBuilder()
                                                    .setFirstNumber(10)
                                                    .setSecondNumber(25)
                                                    .build();

        CalculatorResponse response = calculatorClient.calculator(request);

        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() + " = " + response.getSumResult());
    }

    public static void main(String[] args) {
        CalculatorClient client = new CalculatorClient();
        client.run();
    }
}
