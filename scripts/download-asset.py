# This script can be added to Houdini as a tool

import hou
import sys
# This should be the path to wherever the repo has been cloned
sys.path.append("/Users/samwells/dev/usd-management/")
import tempfile
import base64
from scripts.asset_client import AssetClient
from PySide6 import QtWidgets
from google.protobuf.json_format import MessageToDict

with AssetClient(host="localhost", port=9090) as client:
    resp = client.fetch_asset("test-asset", 1)
    asset_response = [MessageToDict(resp, preserving_proto_field_name=True)][0]
    print(asset_response)

    # Create temp file to store usd contents
    tmp = tempfile.NamedTemporaryFile(suffix=".usd", delete=False)
    decoded_usd = base64.b64decode(asset_response.get("data", ""))

    # Open temp file and write decoded bytes
    with open(tmp.name, "wb") as f:
        f.write(decoded_usd)

    # Create new node and assign filename
    sublayer = hou.node("/stage").createNode("sublayer", "remote_layer")
    sublayer.parm("filepath1").set(tmp.name)
