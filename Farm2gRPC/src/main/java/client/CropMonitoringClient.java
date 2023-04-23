package CropMonitoringService.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import utils.JmDNSUtil;

import java.io.IOException;
import java.net.InetAddress;
import javax.jmdns.ServiceInfo;

public class CropMonitoringClient {

    private final ManagedChannel channel;
    private final CropMonitoringServiceGrpc.CropMonitoringServiceBlockingStub blockingStub;

    public CropMonitoringClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public CropMonitoringClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = CropMonitoringServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void getTemperature() {
        TemperatureRequest request = TemperatureRequest.newBuilder().build();
        TemperatureResponse response = blockingStub.getTemperature(request);
        System.out.println("Temperature: " + response.getTemperature() + "°C");
    }

    public void getHumidity() {
        HumidityRequest request = HumidityRequest.newBuilder().build();
        HumidityResponse response = blockingStub.getHumidity(request);
        System.out.println("Humidity: " + response.getHumidity() + "%");
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String serviceName = "crop_monitoring_service";
        ServiceInfo serviceInfo = JmDNSUtil.discoverService(serviceName);
        InetAddress[] addresses = serviceInfo.getInetAddresses();
        int port = serviceInfo.getPort();

        if (addresses.length == 0) {
            System.err.println("No instances of the service found");
            return;
        }

        String host = addresses[0].getHostAddress();
        CropMonitoringClient client = new CropMonitoringClient(host, port);

        try {
            client.getTemperature();
            client.getHumidity();
        } finally {
            client.shutdown();
        }
    }
}
