"use client";

import { Label } from "@/components/ui/label";

type Props = {
  children: React.ReactNode;
};

const NewDeliveryWrapper = ({ children }: Props) => {
  return (
    <div>
      <Label className="justify-center !mb-8 text-2xl font-bold">
        New Delivery
      </Label>
      <div className="flex flex-col gap-4 !mb-8 w-full !p-8 border shadow-md bg-white rounded-lg p-4">
        {children}
      </div>
    </div>
  );
};

export default NewDeliveryWrapper;
