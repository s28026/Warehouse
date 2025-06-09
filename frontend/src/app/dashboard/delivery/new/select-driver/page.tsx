"use client";

import { Button } from "@/components/ui/button";
import DeliveryDriverRow from "../../DeliveryDriverRow";
import { getDeliveryDrivers, sendDeliveryConfirmationSMS } from "../api";
import { useNewDelivery } from "../context";
import { Label } from "@/components/ui/label";
import { useEffect, useState } from "react";
import { DeliveryDriverShift } from "../../types";
import Loader from "@/components/Loader";
import { useRouter } from "next/navigation";
import { CircleAlert } from "lucide-react";

const NewDeliverySelectDriverPage = () => {
  const [drivers, setDrivers] = useState<DeliveryDriverShift[] | null>(null);
  const { deliveryId, setDriver, setSelectedDriver, selectedDriver, address } =
    useNewDelivery();
  const router = useRouter();

  useEffect(() => {
    getDeliveryDrivers(deliveryId).then((data) => {
      setDrivers(data);
    });
  }, [deliveryId]);

  if (!drivers) return <Loader text="Loading available delivery drivers" />;

  if (drivers.length === 0)
    return (
      <div className="text-center text-sm text-red-500">
        <div className="flex items-center justify-center gap-1">
          <CircleAlert size={16} />
          No available drivers for this delivery.
        </div>
        <Button
          onClick={() => router.push(`/dashboard`)}
          className="w-full !mt-2"
        >
          Go back to dashboard page
        </Button>
      </div>
    );

  return (
    <>
      <div>
        <Label className="font-bold">Pickup Address:</Label>
        <p className="text-sm">{address}</p>
      </div>

      <div>
        <Label className="font-bold">Select Driver</Label>
        {drivers.map((d) => {
          return (
            <div
              key={d.driver.employee.person.pesel}
              onClick={() =>
                setSelectedDriver(
                  d.driver.id === selectedDriver ? null : d.driver.id,
                )
              }
              className="cursor-pointer"
            >
              <DeliveryDriverRow
                driverShift={d}
                selected={selectedDriver === d.driver.id}
              />
            </div>
          );
        })}
      </div>
      <Button
        onClick={() => {
          if (!selectedDriver) return;

          sendDeliveryConfirmationSMS(deliveryId, selectedDriver).then(
            (res) => {
              if (res.message) {
                setDriver(
                  drivers.find((d) => d.driver.id === selectedDriver)?.driver ||
                    null,
                );
                router.push(`/dashboard/delivery/new/awaiting`);
              } else {
                console.error("Error sending SMS:", res.error);
              }
            },
          );
        }}
        disabled={!selectedDriver}
        className="w-full"
      >
        Select
      </Button>
    </>
  );
};

export default NewDeliverySelectDriverPage;
