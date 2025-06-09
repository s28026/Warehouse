import { Loader2 } from "lucide-react";
import { Label } from "./ui/label";

type Props = {
  text: string;
};

const Loader = ({ text }: Props) => {
  return (
    <div className="flex flex-row gap-2 items-center justify-center h-full">
      <Loader2 className="animate-spin" />
      <Label>{text}...</Label>
    </div>
  );
};

export default Loader;
