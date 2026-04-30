import React from "react";
import { Table, Button, Card } from "antd";
import "antd/dist/reset.css";

const columns = [
    {
        title: "Order ID",
        dataIndex: "orderId",
    },
    {
        title: "Items",
        dataIndex: "items",
    },
    {
        title: "Pickup Time",
        dataIndex: "pickupTime",
    },
    {
        title: "Status",
        dataIndex: "status",
    },
    {
        title: "Action",
        dataIndex: "action",
    },
];

const data = [
    {
        key: "1",
        orderId: "101",
        items: "Latte x2",
        pickupTime: "08:30",
        status: "Accepted",
        action: "Start",
    },
    {
        key: "2",
        orderId: "102",
        items: "Mocha x1",
        pickupTime: "08:35",
        status: "Preparing",
        action: "Ready",
    },
    {
        key: "3",
        orderId: "103",
        items: "Tea x1",
        pickupTime: "08:40",
        status: "Ready",
        action: "Collected",
    },
    {
        key: "4",
        orderId: "104",
        items: "Cappuccino x1",
        pickupTime: "08:45",
        status: "Cancelled",
        action: "-",
    },
];

function App() {
    return (
        <div
            style={{
                padding: "40px",
                background: "linear-gradient(to bottom, #ffffff, #dbeafe)",
                minHeight: "100vh",
            }}
        >
            <h1 style={{ textAlign: "center" }}>Coffee Kiosk Staff Dashboard</h1>

            <Card style={{ marginTop: 30 }}>
                <Button type="primary" style={{ float: "right", marginBottom: 20 }}>
                    View Archive
                </Button>

                <Table columns={columns} dataSource={data} />
            </Card>
        </div>
    );
}

export default App;