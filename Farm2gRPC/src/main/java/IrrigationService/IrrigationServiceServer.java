import grpc.services.IrrigationService.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrrigationServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50052)
            .addService(new IrrigationServiceImpl())
            .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
        }));

        server.awaitTermination();
    }

    static class IrrigationServiceImpl extends IrrigationServiceGrpc.IrrigationServiceImplBase {
        private AtomicBoolean isOn = new AtomicBoolean(false);
        private AtomicBoolean isValveOpen = new AtomicBoolean(false);

        @Override
        public void getIrrigationStatus(IrrigationStatusRequest request, StreamObserver
