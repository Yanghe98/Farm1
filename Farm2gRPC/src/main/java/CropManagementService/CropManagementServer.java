import CropManagementService.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CropManagementServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50053)
            .addService(new CropManagementServiceImpl())
            .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
        }));

        server.awaitTermination();
    }

    static class CropManagementServiceImpl extends CropManagementServiceGrpc.CropManagementServiceImplBase {
        private AtomicBoolean isHealthy = new AtomicBoolean(true);
        private AtomicBoolean dispenserStatus = new AtomicBoolean(false);
        private double chemicalAmount = 0;

        @Override
        public void getGrowthStatus(GrowthStatusRequest request, StreamObserver<GrowthStatusResponse> responseObserver) {
            GrowthStatusResponse response = GrowthStatusResponse.newBuilder()
                    .setStatus("Crops are currently in the vegetative stage and are healthy.")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void streamLightingData(LightingDataRequest request, StreamObserver<LightingDataResponse> responseObserver) {
            for (int i = 0; i < 5; i++) {
                LightingDataResponse response = LightingDataResponse.newBuilder()
                        .setIntensity(5000)
                        .setDuration(12)
                        .build();
                responseObserver.onNext(response);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<PestControlRequest> controlPestControl(StreamObserver<PestControlResponse> responseObserver) {
            return new StreamObserver<PestControlRequest>() {
                @Override
                public void onNext(PestControlRequest request) {
                    if (request.getCommand() == PestControlRequest.Command.TURN_ON_DISPENSER) {
                        dispenserStatus.set(true);
                        chemicalAmount += request.getValue();
                        PestControlResponse response = PestControlResponse.newBuilder()
                                .setDispenserStatus("Dispenser " + request.getValue() + " is currently on.")
                                .setChemicalAmount(chemicalAmount)
                                .build();
                        responseObserver.onNext(response);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println("Error occurred in controlPestControl: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
