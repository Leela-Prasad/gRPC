package com.proto.greet.service;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import org.springframework.stereotype.Service;

@Service
public class GreetService {

    public GreetResponse greet(GreetRequest request) {
        return GreetResponse.newBuilder()
                .setResult("Hello " + request.getName())
                .build();
    }
}
