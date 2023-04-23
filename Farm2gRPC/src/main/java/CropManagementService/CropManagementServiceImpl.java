package CropManagementService;

import io.grpc.stub.StreamObserver;

public class CropManagementServiceImpl extends CropManagementServiceGrpc.CropManagementServiceImplBase {

    // Custom logic variables
    private String currentGrowthStatus = "Crops are currently in the vegetative stage and are healthy.";
    private float currentIntensity = 5000;
    private float currentDuration = 12;

    @Override
    public void getGrowthStatus(GrowthStatusRequest request, StreamObserver<GrowthStatusResponse> responseObserver) {
        GrowthStatusResponse response = GrowthStatusResponse.newBuilder()
                .setStatus(currentGrowthStatus)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void streamLightingData(LightingDataRequest request, StreamObserver<LightingDataResponse> responseObserver) {
        LightingDataResponse response = LightingDataResponse.newBuilder()
                .setIntensity(currentIntensity)
                .setDuration(currentDuration)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GrowthEventRequest> recordGrowthEvents(final StreamObserver<GrowthEventResponse> responseObserver) {
        return new StreamObserver<GrowthEventRequest>() {
            StringBuilder events = new StringBuilder();

            @Override
            public void onNext(GrowthEventRequest value) {
                events.append(value.getEvent()).append(", ");
                // Update the currentGrowthStatus based on the new event
                currentGrowthStatus = "Crops are currently " + value.getEvent();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error occurred in recordGrowthEvents: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(GrowthEventResponse.newBuilder()
                        .setResult("Recorded growth events: " + events.toString())
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<LightIntensityRequest> controlLightIntensity(final StreamObserver<LightIntensityResponse> responseObserver) {
        return new StreamObserver<LightIntensityRequest>() {
            @Override
            public void onNext(LightIntensityRequest value) {
                int intensity = value.getIntensity();
                // Update the currentIntensity based on the new intensity value
                currentIntensity = intensity;

                responseObserver.onNext(LightIntensityResponse.newBuilder()
                        .setResult("Adjusted light intensity to: " + intensity)
                        .build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error occurred in controlLightIntensity: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
