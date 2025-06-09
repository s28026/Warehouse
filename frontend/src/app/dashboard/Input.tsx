import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

type Props = {
  placeholder?: string;
  label?: string;
};

const DashboardInput = ({ placeholder, label, ...rest }: Props) => {
  return (
    <div className="">
      {label && <Label className="!mb-1 text-xs">{label}</Label>}
      <Input className="!text-xs" placeholder={placeholder} {...rest} />
    </div>
  );
};

export default DashboardInput;
