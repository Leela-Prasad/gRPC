package com.grpc.greeting.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void calculator(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {

        CalculatorResponse response = CalculatorResponse.newBuilder()
                                                        .setSumResult(request.getFirstNumber() + request.getSecondNumber())
                                                        .build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        Integer number = request.getNumber();

        for (int i=2; i<=number/2; ++i) {
            if(number%i == 0) {
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder().setPrimeFactor(i).build());
            }
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {

        StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {

            int sum = 0;
            int count = 0;

            @Override
            public void onNext(ComputeAverageRequest value) {
                sum += value.getNumber();
                count += 1;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                double average = (double) sum / count;
                responseObserver.onNext(ComputeAverageResponse.newBuilder()
                                                                .setAverage(average)
                                                                .build());
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {

        StreamObserver<FindMaximumRequest> requestObserver = new StreamObserver<FindMaximumRequest>() {

            int currentMaximum = 0;

            @Override
            public void onNext(FindMaximumRequest value) {
                int currentValue = value.getNumber();

                if (currentValue > currentMaximum) {
                    currentMaximum = currentValue;
                    responseObserver.onNext(FindMaximumResponse.newBuilder().setMaximum(currentMaximum).build());
                }

            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        int number = request.getNumber();

        if(number > 0) {
            double numberRoot = Math.sqrt(number);

            responseObserver.onNext(SquareRootResponse.newBuilder()
                                                        .setNumberRoot(numberRoot)
                                                        .build());

            responseObserver.onCompleted();
        }else {
            /* responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The number being sent is not positive")
                            .augmentDescription("Number sent: " + number)
                            .asRuntimeException()
            ); */

            responseObserver.onNext(SquareRootResponse.newBuilder()
                                                        .setStatusStructure(StatusStructure.newBuilder()
                                                                                            .setCode("API_9999")
                                                                                            .setDescription("Number being sent is negative")
                                                                                            .build())
                                                        .build());

            responseObserver.onCompleted();
        }
    }
}
