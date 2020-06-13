package com.proto.greet.controller;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.service.GreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    GreetService greetService;

    @PostMapping(value = "/api/greet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GreetResponse> greet (@RequestBody GreetRequest request) {
        return ResponseEntity.ok(greetService.greet(request));
    }
}
