// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: greet/greet.proto

package com.proto.greet;

public final class Greet {
  private Greet() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_greet_GreetRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_greet_GreetRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_greet_GreetResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_greet_GreetResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021greet/greet.proto\022\005greet\"\034\n\014GreetReque" +
      "st\022\014\n\004name\030\001 \001(\t\"\037\n\rGreetResponse\022\016\n\006res" +
      "ult\030\001 \001(\t2@\n\010Greeting\0224\n\005Greet\022\023.greet.G" +
      "reetRequest\032\024.greet.GreetResponse\"\000B\023\n\017c" +
      "om.proto.greetP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_greet_GreetRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_greet_GreetRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_greet_GreetRequest_descriptor,
        new java.lang.String[] { "Name", });
    internal_static_greet_GreetResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_greet_GreetResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_greet_GreetResponse_descriptor,
        new java.lang.String[] { "Result", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
