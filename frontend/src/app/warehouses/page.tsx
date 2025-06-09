"use client";

import { useEffect, useState } from "react";
import WarehouseRow from "./warehouse-row";
import { Button } from "@/components/ui/button";
import { useGlobalState } from "../store/GlobalState";
import { useRouter } from "next/navigation";
import { WarehouseEmployee } from "../dashboard/employee/employ/types";
import { getWarehouseById, getWarehouses } from "./api";

export type Warehouse = {
  id: number;
  location: string;
  employees?: WarehouseEmployee[];
};

const WarehousePage = () => {
  const { setWarehouse } = useGlobalState();
  const [label, setLabel] = useState("Loading...");
  const [warehouses, setWarehouses] = useState<Warehouse[] | null>(null);
  const [selectedWarehouse, setSelectedWarehouse] = useState<Warehouse | null>(
    null,
  );
  const router = useRouter();

  useEffect(() => {
    getWarehouses().then((res) => {
      console.log("Warehouse: ", res);
      setWarehouses(res);
      setLabel(`${res.length} Warehouses Found`);
    });
  }, []);

  return (
    <div className="flex-row !mt-12">
      <h1 className="text-xl font-bold text-center !pb-4">📦 {label}</h1>
      <div className="container mx-auto py-10 flex flex-col gap-y-2">
        {warehouses &&
          warehouses.map((w) => (
            <div
              key={w.id}
              onClick={() =>
                setSelectedWarehouse(w === selectedWarehouse ? null : w)
              }
            >
              <WarehouseRow
                data={w}
                selected={selectedWarehouse?.id === w.id}
              />
            </div>
          ))}
      </div>
      <Button
        onClick={() => {
          if (selectedWarehouse) {
            getWarehouseById(selectedWarehouse.id).then((warehouse) => {
              if (warehouse) {
                setSelectedWarehouse(warehouse);
                setWarehouse(warehouse);
                localStorage.setItem("warehouseId", warehouse.id.toString());
              } else {
                console.error("Warehouse not found");
              }
            });

            router.push(`dashboard`);
          }
        }}
        disabled={!selectedWarehouse}
        className="w-full !mt-2 cursor-pointer"
      >
        Select
      </Button>
    </div>
  );
};

export default WarehousePage;
