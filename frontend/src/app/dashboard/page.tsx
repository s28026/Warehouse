"use client";

import { Separator } from "@/components/ui/separator";
import { useGlobalState } from "../store/GlobalState";
import DashboardButton from "./Button";
import DashboardPanel from "./Panel";

const DashboardPage = () => {
  const { warehouse } = useGlobalState();

  return (
    <div>
      <div className="text-center">
        <h1 className="text-xl font-bold !pb-2">🎛️ Warehouse Dashboard</h1>
        <p className="text-sm">
          <span className="font-bold">📌 Location:</span> {warehouse.location}
        </p>
        <Separator className="!my-4" />
      </div>
      <div className="flex flex-col gap-y-2">
        <DashboardPanel label="Employee">
          <DashboardButton href="/employee/employ">Employ</DashboardButton>
          <DashboardButton href="/employee/roles">Change roles</DashboardButton>
          <DashboardButton href="/employee/shift" disabled={true}>
            Shift
          </DashboardButton>
          <DashboardButton href="/employee/terminate" disabled={true}>
            Terminate
          </DashboardButton>
          <DashboardButton href="/employee/deliveries">
            Deliveries
          </DashboardButton>
        </DashboardPanel>
        <DashboardPanel label="Delivery">
          <DashboardButton href="/delivery/new/pickup">New</DashboardButton>
          <DashboardButton href="/delivery/unload" disabled={true}>
            Unload
          </DashboardButton>
          <DashboardButton href="/delivery/unload" disabled={true}>
            Mark as unloaded
          </DashboardButton>
        </DashboardPanel>
      </div>
    </div>
  );
};

export default DashboardPage;
