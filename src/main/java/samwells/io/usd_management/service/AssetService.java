package samwells.io.usd_management.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import studio.assets.AssetServiceGrpc;
import studio.assets.Assets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@GrpcService
// AssetServiceGrpc.AssetServiceImplBase is generated based on the .proto files
public class AssetService extends AssetServiceGrpc.AssetServiceImplBase {
    private final byte[] testFile;
    public AssetService() {
        this.testFile = readResourceFile("big_ass_cube.usd");
    }

    @Override
    public void listAssets(Assets.ListAssetsRequest request, StreamObserver<Assets.ListAssetsResponse> responseObserver) {

        var a1 = Assets.AssetMetadata.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName("Asset 1")
                .setType("MODEL")
                .setVersion(1)
                .setDescription("Testy 1")
                .build();
        var a2 = Assets.AssetMetadata.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName("Asset 2")
                .setType("MODEL")
                .setVersion(1)
                .setDescription("Testy 2")
                .build();

        var response = Assets.ListAssetsResponse.newBuilder()
                .addAllAssets(List.of(a1, a2))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAsset(Assets.GetAssetRequest request, StreamObserver<Assets.GetAssetResponse> responseObserver) {
        log.info("Got request for asset {}", request.getId());

        Assets.GetAssetResponse response = Assets
                .GetAssetResponse
                .newBuilder()
                .setFormat("USD")
                .setData(ByteString.copyFrom(testFile))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private byte[] readResourceFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) throw new IllegalArgumentException("Resource not found: " + fileName);

            return inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Failed to read resource: {}", fileName, e);
            throw new RuntimeException(e);
        }
    }
}
