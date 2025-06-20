"use client";

import { useEffect, useState } from "react";
import { getEmployeeDeliveries } from "../api";
import DeliveryRow from "../delivery";
import { useParams } from "next/navigation";
import { Label } from "@/components/ui/label";

const DeliveriesPage = () => {
  const [deliveries, setDeliveries] = useState<[] | null>(null);
  const [activeDelivery, setActiveDelivery] = useState(null);
  const params = useParams();
  const { pesel } = params;

  useEffect(() => {
    getEmployeeDeliveries(pesel).then((d) => {
      setDeliveries(d.previousDeliveries || []);
      setActiveDelivery(d.current || null);
    });
  }, []);

  return (
    <div>
      <Label className="text-xl !pb-1">Deliveries</Label>
      {activeDelivery && (
        <div>
          <div>
            <Label>Active delivery</Label>
          </div>
          <DeliveryRow delivery={activeDelivery} />
        </div>
      )}
      <div className="flex flex-col gap-2">
        {deliveries?.map((delivery, index) => (
          <DeliveryRow key={index} delivery={delivery} />
        ))}
      </div>
    </div>
  );
};

export default DeliveriesPage;
