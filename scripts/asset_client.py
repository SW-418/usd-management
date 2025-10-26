# asset_client.py
import grpc
from scripts.assets_pb2 import ListAssetsRequest, GetAssetRequest
from scripts.assets_pb2_grpc import AssetServiceStub
from scripts.config import GRPC_HOST, GRPC_PORT

class AssetClient:
    def __init__(self, host: str = f"{GRPC_HOST}", port: int = f"{GRPC_PORT}"):
        self.channel = grpc.insecure_channel(f"{host}:{port}")
        self.stub = AssetServiceStub(self.channel)

    def list_assets(self):
        request = ListAssetsRequest()
        return self.stub.ListAssets(request)

    def fetch_asset(self, asset_id: str, version: int = 1):
        """Fetch a specific asset by ID."""
        request = GetAssetRequest(id=asset_id, version=version)
        return self.stub.GetAsset(request)

    def close(self):
        self.channel.close()

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close()
