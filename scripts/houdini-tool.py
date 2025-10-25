# This script can be added to Houdini as a tool

import sys
# This should be the path to wherever the repo has been cloned
sys.path.append("/usd-management")

import hou
from scripts.asset_client import AssetClient

with AssetClient(host="localhost", port=9090) as client:
    resp = client.list_assets()
    names = [a.name for a in resp.assets]

hou.ui.displayMessage(f"Available assets:\n\n" + "\n".join(names))
