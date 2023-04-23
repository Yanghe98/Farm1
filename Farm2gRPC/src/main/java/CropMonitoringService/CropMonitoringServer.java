package CropMonitoringService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CropMonitoringServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                .addService(new CropMonitoringServiceImpl())
                .build();

        server.start();
        System.out.println("Server started, listening on " + port);

        server.awaitTermination();
    }
}
