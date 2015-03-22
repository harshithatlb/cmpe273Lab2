package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class CreatePollServer {
  private static final Logger logger = Logger.getLogger(CreatePollServer.class.getName());

  /* The port on which the server should run */
  private int port = 50051;
  private ServerImpl server;

  private void start() throws Exception {
    server = NettyServerBuilder.forPort(port)
        .addService(PollServiceGrpc.bindService(new PollImpl()))
        .build().start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        CreatePollServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws Exception {
    final CreatePollServer server = new CreatePollServer();
    server.start();
  }

  private class PollImpl implements PollServiceGrpc.PollService {

    @Override
    public void createPoll(edu.sjsu.cmpe273.lab2.PollRequest req,io.grpc.stub.StreamObserver<edu.sjsu.cmpe273.lab2.PollResponse> responseObserver) {
     PollResponse reply =PollResponse.newBuilder().setId( req.getModeratorId()).build();
      responseObserver.onValue(reply);
      responseObserver.onCompleted();
    }
  }
}
