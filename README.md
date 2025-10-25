# usd-management
Tooling for learning about asset management and USD.

### Intentions
- Build Spring Boot RPC service to expose and serve USD assets
- Add python layer to call RPC svc and use this within houdini

### Generating Things

`./gradlew build` - Generates Assets+Assets svc to be used on the Java side

Generate gRPC contracts for use on Python side (run from root dir)

`python -m grpc_tools.protoc -Iscripts=src/main/proto --python_out=. --grpc_python_out=. src/main/proto/assets.proto`
