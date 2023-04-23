package CropManagementService;

import io.grpc.stub.StreamObserver;

public class CropManagementServiceGrpc extends CropManagementServiceGrpc.CropManagementServiceImplBase {

    private final CropManagementService service;

    public CropManagementServiceGrpc() {
        this.service = new CropManagementServiceImpl();
    }

    @Override
    public void getGrowthStatus(GrowthStatusRequest request, StreamObserver<GrowthStatusResponse> responseObserver) {
        GrowthStatusResponse response = service.getGrowthStatus(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void streamLightingData(LightingDataRequest request, StreamObserver<LightingDataResponse> responseObserver) {
        LightingDataResponse response = service.streamLightingData(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<PestControlRequest> controlPestControl(StreamObserver<PestControlResponse> responseObserver) {
        return new StreamObserver<PestControlRequest>() {
            @Override
            public void onNext(PestControlRequest request) {
                PestControlResponse response = service.controlPestControl(request);
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error occurred: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
