package samwells.io.usd_management.service;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import studio.assets.AssetServiceGrpc;
import studio.assets.Assets;

import java.util.List;
import java.util.UUID;

@GrpcService
// AssetServiceGrpc.AssetServiceImplBase is generated based on the .proto files
public class AssetService extends AssetServiceGrpc.AssetServiceImplBase {
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
                .setName("Asset 1")
                .setType("MODEL")
                .setVersion(1)
                .setDescription("Testy 1")
                .build();

        var response = Assets.ListAssetsResponse.newBuilder()
                .addAllAssets(List.of(a1, a2))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
