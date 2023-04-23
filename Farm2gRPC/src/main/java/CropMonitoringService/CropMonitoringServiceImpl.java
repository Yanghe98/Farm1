package CropMonitoringService;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.Random;

public class CropMonitoringServiceImpl extends CropMonitoringServiceProto.CropMonitoringServiceGrpc.CropMonitoringServiceImplBase {

    private final Random random = new Random();

    @Override
    public void getTemperature(Empty request, StreamObserver<CropMonitoringServiceProto.Temperature> responseObserver) {
        CropMonitoringServiceProto.Temperature temperature = CropMonitoringServiceProto.Temperature.newBuilder()
                .setValue(randomTemperature())
                .build();
        responseObserver.onNext(temperature);
        responseObserver.onCompleted();
    }

    @Override
    public void getHumidity(Empty request, StreamObserver<CropMonitoringServiceProto.Humidity> responseObserver) {
        CropMonitoringServiceProto.Humidity humidity = CropMonitoringServiceProto.Humidity.newBuilder()
                .setValue(randomHumidity())
                .build();
        responseObserver.onNext(humidity);
        responseObserver.onCompleted();
    }

    @Override
    public void streamSensorData(Empty request, StreamObserver<CropMonitoringServiceProto.SensorData> responseObserver) {
        for (int i = 0; i < 10; i++) {
            CropMonitoringServiceProto.SensorData sensorData = CropMonitoringServiceProto.SensorData.newBuilder()
                    .setTemperature(randomTemperature())
                    .setHumidity(randomHumidity())
                    .build();
            responseObserver.onNext(sensorData);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }

    private float randomTemperature() {
        return 20.0f + random.nextFloat() * 10.0f;
    }

    private float randomHumidity() {
        return 30.0f + random.nextFloat() * 40.0f;
    }
}
