If we want to build an API then we need to think about following.
1. API to exchange (i.e., endpoints)
2. Data format (xml, json, binary)
3. Error Patterns (success and error scenarios)
4. Load Balancing.
5. Latency
6. Scalability

Most popular choice to build an API is REST.

REST is an Architecture Style and not a framework which will solve most of the problems that we have to build an API.

gRPC is a Framework allows you to define Request and Response for RPC(Remote Procedure Calls) and Handles(above problems) all the rest for you.

gRPc is modern, fast and efficient, build on top of HTTP/2, low latency, supports streaming, language independent and makes it super easy to plugin authentication, load balancing, logging and monitoring.

In gRPC we need to define messages and services using protocol buffers, rest of gRPC code will be generated for you and you will have to provide an implementation for it.

Here we will define messages using .proto file which will work for over 12 programming languages(server and client) and allows you to use a framework that scales to millions of RPC per second.

Why Protocol Buffers?
Language Agnostic.
Code can be generated for pretty much any language.
Data is binary and efficiently serialized
Very convenient for transporting lot of data.
Protocol Buffers allows for easy API evolution using rules.
Protocol Buffers payload size is less when compared to JSON so it saves Network Bandwidth
Parsing JSON is a CPU intensive while Protocol Buffer requires less CPU.
because of network bandwidth and less cpu intensive protocol buffer best suitable for mobile devices or iot devices that have slower CPU.

HTTP/2:
gRPC leverages HTTP/2 as a backbone for communications.

In HTTP1 opens a new TCP connection to server at each request.
It doesn't compress headers (which are plain text).
It only works with Request Response mechanism (no server push).
These inefficiencies add latency and increase network packet size.

HTTP2 supports multiplexing i.e., client and server can push messages in parallel over the same TCP connection, so reduces latency.
HTTP2 supports server push i.e., server can push streams or multiple messages for one request from the client, so round trips are reduced.
HTP2 suppors header compression. (an average http request may have over 20 headers due to cookies, content cache, and application headers)
HTTP2 is binary
HTTP2 is secure (i.e. in HTTP2 security is the first class citizen and SSL is not required but recommended by default)


API Types in gRPC:
There are 4 types of API's we can write in gRPC
1. Unary
2. Server Streaming
3. Client Streaming
4. Bidirectional Streaming

Unary is a traditional API like HTTP Rest

HTTP/2 enables APIs now to have streaming capabilities.

Server and client can push multiple messages as part of one request.

gRPC Server:
gRPC servers are asynchronous by default.
This means they do not block threads on request.
Therefore each gRPC Server can serve millions of requests in parallel.

gRPC Client:
gRPC client can be asynchronous or synchronous (blocking).
gRPC Clients can perform client side load balancing.


Security in gRPC:
By default gRPC strongly advocates for you to use SSL(encryption over wire) in your API. This means that gRPC has security as a first class citizen.

Each language will provide an API to load gRPC with required certificates and provide encryption capability out of the box.

Additionally using interceptors we can also provide authentication.

gRPC vs REST
				gRPC 																REST
Protocol Buffers - smaller, faster													JSON - text based, bigger and slower.
HTTP/2 (lower latency)																HTTP 1.1 (higher latency).
Bidirectional and Async 															client -> server requests only.
Stream supports 																	Request/Response support only.
API Oriented - "What"																CRUD Oriented
(no constraints - free design)
Code Generation through Protocol Buffers in any language - first class citizen		Code Generation through Open API/Swagger (addon) - second class c
citizen
RPC Based - gRPC does the plumbing for us 											HTTP Verb based - we have to write plumbing or use 3rd party library.



SSL:

export SERVER_CN=localhost

# step1: Generate Certificate Authority + Trust Certificatge (ca.crt)
openssl genrsa -passout pass:1111 -des3 -out ca.key 4096
openssl req -passin pass:1111 -new -x509 -days 365 -key ca.key -out ca.crt -subj "/CN=${SERVER_CN}"

#step2: Generate Server Private Key (server.key)
openssl genrsa -passout pass:1111 -des3 -out server.key 4096

#step3: Generate Server Signing Request (CSR) - server.csr
openssl req -passin pass:1111 -new -key server.key -out server.csr -subj "/CN=${SERVER_CN}"

#step4: Get the signed certificate with CA - server.crt
openssl x509 -req -passin pass:1111 -days 365 -in server.csr -CA ca.crt -CAkey ca.key -set_serial 01 -out server.crt 

#step5: convert server.key in to .pem format - usable by gRPC
openssl pkcs8 -topk8 -nocrypt -passin pass:1111 -in server.key -out server.pem


Evan CLI:
this CLI is used to run grpc calls without having proto files.
we can see what are the grpc calls available
what are the services available etc


./evans -r -p 50052

show package
shows the packages where proto files are there.

+-------------------------+
|         PACKAGE         |
+-------------------------+
| calculator              |
| grpc.reflection.v1alpha |
+-------------------------+

show service
will return running services

+-------------------+--------------------------+---------------------------------+----------------------------------+
|      SERVICE      |           RPC            |          REQUEST TYPE           |          RESPONSE TYPE           |
+-------------------+--------------------------+---------------------------------+----------------------------------+
| CalculatorService | Calculator               | CalculatorRequest               | CalculatorResponse               |
| CalculatorService | PrimeNumberDecomposition | PrimeNumberDecompositionRequest | PrimeNumberDecompositionResponse |
| CalculatorService | ComputeAverage           | ComputeAverageRequest           | ComputeAverageResponse           |
| CalculatorService | FindMaximum              | FindMaximumRequest              | FindMaximumResponse              |
| CalculatorService | SquareRoot               | SquareRootRequest               | SquareRootResponse               |
+-------------------+--------------------------+---------------------------------+----------------------------------+

calculator.CalculatorService@127.0.0.1:50052> show message
show Request Response Types

+----------------------------------+
|             MESSAGE              |
+----------------------------------+
| CalculatorRequest                |
| CalculatorResponse               |
| ComputeAverageRequest            |
| ComputeAverageResponse           |
| FindMaximumRequest               |
| FindMaximumResponse              |
| PrimeNumberDecompositionRequest  |
| PrimeNumberDecompositionResponse |
| SquareRootRequest                |
| SquareRootResponse               |
+----------------------------------+


calculator.CalculatorService@127.0.0.1:50052> call Calculator
first_number (TYPE_INT32) => 1
second_number (TYPE_INT32) => 23
{
  "sum_result": 24
}

calculator.CalculatorService@127.0.0.1:50052> call PrimeNumberDecomposition
number (TYPE_INT32) => 2133473
{
  "prime_factor": 107
}
{
  "prime_factor": 127
}
{
  "prime_factor": 157
}
{
  "prime_factor": 13589
}
{
  "prime_factor": 16799
}
{
  "prime_factor": 19939
}


calculator.CalculatorService@127.0.0.1:50052> call ComputeAverage
number (TYPE_INT32) => 23
number (TYPE_INT32) => 4
number (TYPE_INT32) => 7
number (TYPE_INT32) => 1
number (TYPE_INT32) => 9
number (TYPE_INT32) => 3
number (TYPE_INT32) => 
{
  "average": 7.833333333333333
}

calculator.CalculatorService@127.0.0.1:50052> call SquareRoot
number (TYPE_INT32) => 3
{
  "number_root": 1.7320508075688772
}

calculator.CalculatorService@127.0.0.1:50052> call SquareRoot
number (TYPE_INT32) => -3
{
  "status_structure": {
    "code": "API_9999",
    "description": "Number being sent is negative"
  }
}

calculator.CalculatorService@127.0.0.1:50052> call FindMaximum
number (TYPE_INT32) => 2
number (TYPE_INT32) => {
  "maximum": 2
}
number (TYPE_INT32) => 4
number (TYPE_INT32) => {
  "maximum": 4
}
number (TYPE_INT32) => 6
number (TYPE_INT32) => {
  "maximum": 6
}
number (TYPE_INT32) => 1
number (TYPE_INT32) => 17
number (TYPE_INT32) => {
  "maximum": 17
}
number (TYPE_INT32) => 3
number (TYPE_INT32) => 5
number (TYPE_INT32) => 2
number (TYPE_INT32) => 20
number (TYPE_INT32) => {
  "maximum": 20
}
number (TYPE_INT32) => 


MongoDB installation for Yosemite
curl -O https://fastdl.mongodb.org/osx/mongodb-osx-x86_64-3.0.5.tgz

extract the zip file

create data directory
mkdir /Users/Leela/Desktop/mongodb-data

mongod --dbpath /Users/Leela/Desktop/mongodb-data
