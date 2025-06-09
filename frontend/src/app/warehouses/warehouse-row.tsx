import { Separator } from "@/components/ui/separator";
import { Checkbox } from "@/components/ui/checkbox";
import { Warehouse } from "./page";
import { Label } from "@/components/ui/label";

type Props = {
  data: Warehouse;
  selected: boolean;
};

export default function WarehouseRow({ data, selected }: Props) {
  const columns = [
    {
      label: "ID",
      value: data.id,
    },
    {
      label: "Location",
      value: data.location,
    },
    // {
    //   label: "Employee count",
    //   value: data.employees ? data.employees.length : 0,
    // },
  ];

  return (
    <div className="flex flex-row gap-x-4 rounded-md border !py-2 !px-4 items-center">
      <Checkbox checked={selected} />
      {columns.map((c) => (
        <div className="flex flex-col" key={c.label}>
          <Label className="text-sm font-bold">{c.label}</Label>
          <Separator />
          <div>{c.value}</div>
        </div>
      ))}
    </div>
  );
}
