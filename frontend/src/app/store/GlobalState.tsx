"use client";

import React, { createContext, useContext, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Employee } from "../dashboard/employee/employ/types";
import { Warehouse } from "../warehouses/page";
import { usePathname } from "next/navigation";
import { getWarehouseById } from "../warehouses/api";

type GlobalStateType = {
  warehouse: Warehouse; // Replace 'any' with a more specific type if available
  setWarehouse: React.Dispatch<React.SetStateAction<Warehouse | null>>;
  employee: Employee; // Replace 'any' with a more specific type if available
  setEmployee: React.Dispatch<React.SetStateAction<Employee | null>>;
};

const GlobalStateContext = createContext<GlobalStateType | undefined>(
  undefined,
);

export function GlobalStateProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [warehouse, setWarehouse] = useState<Warehouse | null>(null);
  const [employee, setEmployee] = useState<Employee | null>(null);
  const router = useRouter();
  const pathname = usePathname();

  const value = {
    warehouse,
    setWarehouse,
    employee,
    setEmployee,
  };

  useEffect(() => {
    const warehouseId = null;

    // if (warehouse === null && pathname !== "/warehouses")
    //   router.push("/warehouses");
    if (warehouseId) {
      getWarehouseById(+warehouseId).then((data) => {
        setWarehouse(data);
        console.log("Warehouse data:", data);
        localStorage.setItem("warehouseId", data!.id.toString());
      });
    } else if (pathname !== "/warehouses") {
      router.push("/warehouses");
    }
  }, []);

  if (warehouse === null && pathname !== "/warehouses") return null;

  return (
    <GlobalStateContext.Provider value={value}>
      {children}
    </GlobalStateContext.Provider>
  );
}

export function useGlobalState() {
  const context = useContext(GlobalStateContext);
  if (!context)
    throw new Error("useGlobalState must be used within GlobalStateProvider");
  return context;
}
