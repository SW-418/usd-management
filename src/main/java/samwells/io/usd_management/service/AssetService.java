package samwells.io.usd_management.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import studio.assets.AssetServiceGrpc;
import studio.assets.Assets;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
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
        var response = Assets.ListAssetsResponse.newBuilder()
                .addAllAssets(List.of(
                        createAsset("Asset 1", "MODEL", "Testy 1"),
                        createAsset("Asset 2", "MODEL", "Testy 2"),
                        createAsset("SCENE 1", "SCENE", "Testy Scene 1")
                ))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAsset(Assets.GetAssetRequest request, StreamObserver<Assets.GetAssetResponse> responseObserver) {
        log.info("Got request for asset {} V{}", request.getId(), request.getVersion());

        Assets.GetAssetResponse response = Assets
                .GetAssetResponse
                .newBuilder()
                .setFormat("USD")
                .setData(ByteString.copyFrom(testFile))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Assets.AssetMetadata createAsset(String name, String type, String description) {
        return Assets.AssetMetadata.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName(name)
                .setType(type)
                .setVersion(1)
                .setDescription(description)
                .setCreatedAt(Timestamps.fromMillis(Instant.now().toEpochMilli()))
                .build();
    }

    // This is here temp as we'll probably start storing and updating these elsewhere
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
