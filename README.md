# usd-management
Tooling for learning about asset management and USD.

### Intentions
- Build Spring Boot RPC service to expose and serve USD assets
- Add python layer to call RPC svc and use this within houdini

### Generating Things

`./gradlew build` - Generates Assets+Assets svc to be used on the Java side

Generate gRPC contracts for use on Python side (run from `/src/main/proto` dir)

`python -m grpc_tools.protoc -I . --python_out=../../../scripts --grpc_python_out=../../../scripts assets.proto`
