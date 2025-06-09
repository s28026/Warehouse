"use client";

import { Label } from "@/components/ui/label";
import { useNewDelivery } from "../context";

const NewDeliveryAwaitingLayout = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const { driver, address } = useNewDelivery();

  return (
    // <div className="flex flex-col gap-4 w-full">
    // 	<div className="flex flex-col gap-2 w-full">{children}</div>
    // </div>
    <>
      <div>
        <Label className="font-bold">Pickup Address:</Label>
        <p className="text-sm">{address}</p>
      </div>
      <div>
        <Label className="font-bold">Driver:</Label>
        <p className="text-sm">{driver!.employee.person.fullName}</p>
      </div>
      {children}
    </>
  );
};

export default NewDeliveryAwaitingLayout;
