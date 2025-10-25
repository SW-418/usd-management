# This script can be added to Houdini as a tool

import hou
import sys
# This should be the path to wherever the repo has been cloned
sys.path.append("/usd-management/")
from scripts.asset_client import AssetClient
from PySide6 import QtWidgets
from google.protobuf.json_format import MessageToDict

with AssetClient(host="localhost", port=9090) as client:
    resp = client.list_assets()
    data_dicts = [MessageToDict(a, preserving_proto_field_name=True) for a in resp.assets]

def show_results_table(data):
    dialog = QtWidgets.QDialog(hou.ui.mainQtWindow())
    dialog.setWindowTitle("Assets")
    dialog.resize(600, 400)

    layout = QtWidgets.QVBoxLayout(dialog)
    table = QtWidgets.QTableWidget()
    layout.addWidget(table)

    if not data:
        label = QtWidgets.QLabel("No results found.")
        layout.addWidget(label)
        dialog.exec_()
        return

    # Column headers from keys
    headers = ["id", "name", "type", "version", "description", "created_at"]
    table.setColumnCount(len(headers))
    table.setRowCount(len(data))
    table.setHorizontalHeaderLabels(headers)

    for row, entry in enumerate(data):
        for col, key in enumerate(headers):
            val = str(entry.get(key, ""))
            table.setItem(row, col, QtWidgets.QTableWidgetItem(val))

    table.resizeColumnsToContents()
    table.setSortingEnabled(True)
    dialog.setLayout(layout)
    dialog.exec_()

show_results_table(data_dicts)
