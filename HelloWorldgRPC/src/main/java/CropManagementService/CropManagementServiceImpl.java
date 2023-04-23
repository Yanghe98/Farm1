package CropManagementService;

public class CropManagementServiceImpl implements CropManagementService {

    @Override
    public GrowthStatusResponse getGrowthStatus(GrowthStatusRequest request) {
        return GrowthStatusResponse.newBuilder()
                .setStatus("Healthy")
                .build();
    }

    @Override
    public LightingDataResponse streamLightingData(LightingDataRequest request) {
        return LightingDataResponse.newBuilder()
                .setIntensity(100)
                .setDuration(30)
                .build();
    }

    @Override
    public PestControlResponse controlPestControl(PestControlRequest request) {
        return PestControlResponse.newBuilder()
                .setDispenserStatus("Active")
                .setChemicalAmount(42.5f)
                .build();
    }
}
