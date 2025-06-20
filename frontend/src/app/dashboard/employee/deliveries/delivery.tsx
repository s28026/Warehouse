import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";

const DeliveryRow = ({ delivery }) => {
  const columns = [
    {
      label: "ID",
      value: delivery.id,
    },
    {
      label: "Pickup",
      value: delivery.pickupAddress,
    },
    {
      label: "STATUS",
      value: delivery.status,
    },
  ];

  return (
    <div className="flex flex-row gap-x-4 rounded-md border !py-2 !px-4 items-center">
      {columns.map((c) => (
        <div className="flex flex-col" key={c.label}>
          <Label className="text-sm font-bold">{c.label}</Label>
          <Separator />
          <div>{c.value}</div>
        </div>
      ))}
    </div>
  );
};

export default DeliveryRow;
