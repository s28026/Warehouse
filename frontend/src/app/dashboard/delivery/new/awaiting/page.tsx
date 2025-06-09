"use client";

import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { AlarmClock, CircleAlert, CircleCheck } from "lucide-react";
import { useEffect, useState } from "react";
import { getDelivery, getDeliveryDrivers } from "../api";
import Loader from "@/components/Loader";
import { useRouter } from "next/navigation";
import { useNewDelivery } from "../context";
import ComplaintPopup from "./ComplaintPopup";

enum AwaitingStatus {
  PENDING = 0,
  CONFIRMED = 1,
  REJECTED = 2,
  TIMEOUT = 3,
}

const TIMEOUT_SECONDS = 300; // 5 minutes

const NewDeliveryAwaitingPage = () => {
  const [status, setStatus] = useState<AwaitingStatus>(AwaitingStatus.PENDING);
  const [timeLeft, setTimeLeft] = useState(TIMEOUT_SECONDS);
  const [errors, setErrors] = useState<string[] | null>(null);
  const { deliveryId, driver } = useNewDelivery();
  const router = useRouter();

  useEffect(() => {
    if (!deliveryId || status !== AwaitingStatus.PENDING) return;

    const intervalId = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 0) {
          clearInterval(intervalId);
          return 0;
        }
        return prev - 1;
      });

      getDelivery(deliveryId).then((data) => {
        if (status !== AwaitingStatus.PENDING) return clearInterval(intervalId);

        if (data.errors) {
          setErrors(data.errors);
          clearInterval(intervalId);
          return;
        }

        switch (data.status) {
          case "IN_TRANSIT":
            setStatus(AwaitingStatus.CONFIRMED);
            break;
          case "DRIVER_REJECTED":
            setStatus(AwaitingStatus.REJECTED);
            break;
          case "DRIVER_TIMEOUT":
            setStatus(AwaitingStatus.TIMEOUT);
            break;
          default:
            setStatus(AwaitingStatus.PENDING);
        }
      });
    }, 1000);

    return () => clearInterval(intervalId);
  }, [deliveryId, status]);

  const minutes = Math.floor(timeLeft / 60);
  const seconds = timeLeft % 60;
  const timeStr = `${minutes}:${seconds.toString().padStart(2, "0")} minutes left`;

  if (status === AwaitingStatus.PENDING) {
    return (
      <div className="flex flex-col items-center">
        <Label className="font-bold text-base">Awaiting Confirmation</Label>
        <Loader text={timeStr} />
      </div>
    );
  }

  const anotherDriverButton = (
    <Button
      className="w-full !mt-2"
      onClick={() => {
        router.push(`/dashboard/delivery/new/select-driver`);
      }}
    >
      Choose another driver
    </Button>
  );

  if (errors) {
    return errors.map((err) => (
      <div key={err}>
        <div className="text-red-500 text-sm flex items-center gap-1">
          <CircleAlert color="red" size={16} />
          Error: {err}
        </div>
        {anotherDriverButton}
      </div>
    ));
  }

  if (status === AwaitingStatus.CONFIRMED) {
    return (
      <div>
        <div className="flex items-center gap-2">
          <Label className="font-bold !mb-2 text-base">
            <CircleCheck color="green" size={18} />
            <p className="">The delivery has been confirmed.</p>
          </Label>
        </div>

        <Button
          onClick={() => {
            router.push(`/dashboard/delivery/new/pickup`);
          }}
          className="w-full !mt-2"
        >
          Add another delivery
        </Button>
      </div>
    );
  }

  if (status === AwaitingStatus.REJECTED) {
    return (
      <div>
        <Label className="font-bold !mb-2 text-base flex">
          <CircleAlert color="red" size={18} />
          Delivery Rejected
        </Label>
        {anotherDriverButton}
      </div>
    );
  }

  if (status === AwaitingStatus.TIMEOUT) {
    return (
      <div>
        <Label className="font-bold !mb-2 text-base">
          <AlarmClock />
          Confirmation Timeout
        </Label>
        <div className="flex flex-col items-center gap-2">
          {anotherDriverButton}
          <ComplaintPopup employeePesel={driver!.employee.person.pesel} />
        </div>
      </div>
    );
  }

  return null;
};

export default NewDeliveryAwaitingPage;
