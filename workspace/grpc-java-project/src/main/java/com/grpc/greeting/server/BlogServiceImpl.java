package com.grpc.greeting.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("mydb");
    private MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        Blog blog = request.getBlog();

        System.out.println("Received Blog");
        Document doc = new Document("author_id", blog.getAuthorId())
                            .append("title", blog.getTitle())
                            .append("content", blog.getContent());

        System.out.println("Inserting Blog...");
        collection.insertOne(doc);

        String id = doc.getObjectId("_id").toString();
        System.out.println("Inserted Blog with id: " + id);

        responseObserver.onNext(CreateBlogResponse.newBuilder()
                                                    .setBlog(blog.toBuilder().setId(id))
                                                    .build());

        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
        String blogId = request.getBlogId();

        System.out.println("Searching Blog...");

        Blog blog = searchBlogById(blogId);

        if(blog == null) {
            System.out.println("Blog NOT Found");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Blog with id = " + blogId + " Not Found")
                    .asRuntimeException()
            );
        } else {
            System.out.println("Blog Found");
            responseObserver.onNext(ReadBlogResponse.newBuilder()
                    .setBlog(blog)
                    .build());

            responseObserver.onCompleted();
        }

    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {

        System.out.println("Updating Blog...");
        Blog blog = request.getBlog();
        String blogId = blog.getId();
        Blog result = searchBlogById(blogId);

        if(result == null) {
            System.out.println("Blog Not Found");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Blog with id = " + blogId + "Not Found")
                    .asRuntimeException());
        } else {
            Document replacement = new Document()
                    .append("_id", new ObjectId(blogId))
                    .append("author_id", blog.getAuthorId())
                    .append("title", blog.getTitle())
                    .append("content", blog.getContent());

            collection.replaceOne(eq("_id", new ObjectId(blogId)), replacement);

            System.out.println("Blog Updated");
            responseObserver.onNext(UpdateBlogResponse.newBuilder()
                    .setBlog(documentToBlog(replacement))
                    .build());

            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {

        String blogId = request.getBlogId();

        DeleteResult deleteResult = collection.deleteOne(eq("_id", new ObjectId(blogId)));

        if(deleteResult.getDeletedCount() == 0){
            System.out.println("No Blog Found");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Blog with Id = " + blogId + " Not Found")
                    .asRuntimeException()
            );

        }else {
            responseObserver.onNext(DeleteBlogResponse.newBuilder()
                    .setBlogId(blogId)
                    .build());

            responseObserver.onCompleted();
        }
    }

    @Override
    public void listBlog(ListBlogRequest request, StreamObserver<ListBlogResponse> responseObserver) {

        collection.find().iterator().forEachRemaining(document -> {
            responseObserver.onNext(ListBlogResponse.newBuilder()
                    .setBlog(documentToBlog(document))
                    .build());
        });

        responseObserver.onCompleted();
    }

    private Blog searchBlogById(String blogId) {
        Document result = null;

        try {
            result = collection.find(eq("_id", new ObjectId(blogId))).first();
        } catch (Exception e) {
            return null;
        }

        if(result == null) {
            return null;
        }else {
            return documentToBlog(result);
        }
    }

    private Blog documentToBlog(Document document) {
        return Blog.newBuilder()
                .setId(document.get("_id").toString())
                .setAuthorId(document.getString("author_id"))
                .setTitle(document.getString("title"))
                .setContent(document.getString("content"))
                .build();
    }

}
