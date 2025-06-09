"use client";

import { createContext, useContext, useState, useRef, useEffect } from "react";
import { DeliveryDriver } from "../types";
import { createDelivery } from "./api";
import { useRouter } from "next/navigation";
import { useGlobalState } from "@/app/store/GlobalState";

export enum NewDeliveryPageStatus {
  InputAddress = 0,
  SelectDriver = 1,
  AwaitingConfirmation = 2,
}

type DeliveryDriverShift = any; // Replace with your actual type

type DeliveryContextType = {
  address: string | null;
  setAddress: (a: string | null) => void;
  driver: DeliveryDriver | null;
  setDriver: (d: DeliveryDriver | null) => void;
  drivers: DeliveryDriverShift[] | null;
  setDrivers: (d: DeliveryDriverShift[] | null) => void;
  status: NewDeliveryPageStatus;
  setStatus: (s: NewDeliveryPageStatus) => void;
  deliveryId: number;
  setDeliveryId: (id: number | null) => void;
  hasCreatedRef: React.MutableRefObject<boolean>;
  selectedDriver: number | null;
  setSelectedDriver: (id: number | null) => void;
};

const DeliveryContext = createContext<DeliveryContextType | null>(null);

export const useNewDelivery = () => {
  const ctx = useContext(DeliveryContext);
  if (!ctx) throw new Error("useDelivery must be used within DeliveryProvider");
  return ctx;
};

export const NewDeliveryProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [address, setAddress] = useState<string | null>(null);
  const [driver, setDriver] = useState<DeliveryDriver | null>(null);
  const [selectedDriver, setSelectedDriver] = useState<DeliveryDriver | null>(
    null,
  );
  const [drivers, setDrivers] = useState<DeliveryDriverShift[] | null>(null);
  const [deliveryId, setDeliveryId] = useState<number | null>(null);
  const hasCreatedRef = useRef(false);
  const router = useRouter();
  const { warehouse } = useGlobalState();

  useEffect(() => {
    if (!deliveryId) router.push(`/dashboard/delivery/new/pickup`);
    if (hasCreatedRef.current) return;

    console.log("123");

    createDelivery(warehouse.id).then((d) => {
      hasCreatedRef.current = true;
      console.log("Created delivery:", d);
      setDeliveryId(d.id);
    });
  }, [deliveryId, hasCreatedRef]);

  return (
    <DeliveryContext.Provider
      value={{
        address,
        setAddress,
        driver,
        setDriver,
        drivers,
        setDrivers,
        deliveryId,
        setDeliveryId,
        hasCreatedRef,
        selectedDriver,
        setSelectedDriver,
      }}
    >
      {children}
    </DeliveryContext.Provider>
  );
};
