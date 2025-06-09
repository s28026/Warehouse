"use client";

import { useGlobalState } from "../store/GlobalState";
import DashboardButton from "./Button";
import DashboardPanel from "./Panel";

const DashboardPage = () => {
  const { warehouse } = useGlobalState();

  return (
    <div>
      <div className="text-center">
        <p className="text-sm">
          <span className="font-bold">📌 Location:</span> {warehouse.location}
        </p>
        <h1 className="text-xl font-bold !pb-2">🎛️ Warehouse Dashboard</h1>
      </div>
      <div className="flex flex-col gap-y-2">
        <DashboardPanel label="Employee">
          <DashboardButton href="/employee/employ">Employ</DashboardButton>
          <DashboardButton href="/employee/shift">Shift</DashboardButton>
          <DashboardButton href="/employee/terminate">
            Terminate
          </DashboardButton>
          <DashboardButton href="/employee/roles">Change roles</DashboardButton>
        </DashboardPanel>
        <DashboardPanel label="Delivery">
          <DashboardButton href="/delivery/new/pickup">New</DashboardButton>
          <DashboardButton href="/delivery/unload">Unload</DashboardButton>
        </DashboardPanel>
      </div>
    </div>
  );
};

export default DashboardPage;
