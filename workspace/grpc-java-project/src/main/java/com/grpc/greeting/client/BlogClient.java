package com.grpc.greeting.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {
        BlogClient blogClient = new BlogClient();
        blogClient.run();

    }

    public void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053)
                                                        .usePlaintext()
                                                        .build();

        BlogServiceGrpc.BlogServiceBlockingStub stub = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder()
                        .setAuthorId("Leela")
                        .setTitle("New Blog!")
                        .setContent("Welcome! First Blog")
                        .build();

        System.out.println("Create Blog...");
        CreateBlogResponse response = stub.createBlog(CreateBlogRequest.newBuilder()
                                                                        .setBlog(blog)
                                                                        .build());

        System.out.println("Create Blog Response from Server");
        System.out.println(response.getBlog());

        String blogId = response.getBlog().getId();

        System.out.println("Searching Blog...");
        ReadBlogResponse readBlogResponse = stub.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());

        System.out.println("Search Blog Response");
        System.out.println(readBlogResponse);

        /*System.out.println("Searching Fake Blog...");
        ReadBlogResponse readBlogResponseNotFound = stub.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId("5ee4a1cb6155910374add401")
                .build());

        System.out.println(readBlogResponseNotFound);*/

        System.out.println("Updating Blog...");
        Blog updatedBlog = Blog.newBuilder()
                .setId(blogId)
                .setTitle("New Blog! (updated)")
                .setAuthorId("Leela")
                .setContent("Welcome! First Blog (updated)")
                .build();

        UpdateBlogResponse updateBlogResponse = stub.updateBlog(UpdateBlogRequest.newBuilder()
                .setBlog(updatedBlog)
                .build());

        System.out.println("Update Blog Response");
        System.out.println(updateBlogResponse);

        System.out.println("Deleting Blog...");
        stub.deleteBlog(DeleteBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());
        System.out.println("Deleted Blog");

       /*stub.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());*/

       stub.listBlog(ListBlogRequest.newBuilder().build()).forEachRemaining(ListBlogResponse -> {
           System.out.println(ListBlogResponse.getBlog());
       });

    }
}
