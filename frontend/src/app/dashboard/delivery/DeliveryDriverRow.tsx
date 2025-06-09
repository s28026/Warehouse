"use client";

import { CircleAlert } from "lucide-react";
import { DeliveryDriverShift } from "./types";
import { Checkbox } from "@/components/ui/checkbox";

type Props = {
  driverShift: DeliveryDriverShift;
  selected?: boolean;
};

const DeliveryDriverRow = ({ driverShift: ds, selected }: Props) => {
  const minutes = ds.hoursWorkedToday;
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  const worked = `${h}:${m.toString().padStart(2, "0")}`;

  return (
    <div className="flex gap-1 !p-2 items-center hover:bg-gray-50 cursor-pointer">
      <Checkbox checked={selected} />
      <div className="!ml-2 text-xs">
        <div className="font-bold">{ds.driver.employee.person.fullName}</div>
        {ds.numberOfDeliveriesToday === 0 ? (
          <div className="text-green-700 text-xs flex items-center gap-1">
            <CircleAlert size={12} />
            No deliveries
          </div>
        ) : (
          <div>
            <div>
              Deliveries:{" "}
              <span className="font-semibold">
                {ds.numberOfDeliveriesToday}
              </span>
            </div>
            <div>
              Hours worked: <span className="font-semibold">{worked}</span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default DeliveryDriverRow;
