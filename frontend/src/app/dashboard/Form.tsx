import { Label } from "@/components/ui/label";

type Props = {
  children?: React.ReactNode;
  header?: string;
  onSubmit?: (e: React.FormEvent<HTMLFormElement>) => void;
};

const DashboardForm = ({ children, header, onSubmit }: Props) => {
  return (
    <form
      className="flex flex-col gap-2 !p-4 bg-white rounded-lg shadow-md w-full !mx-auto"
      onSubmit={onSubmit}
    >
      {header && (
        <Label className="text-xl justify-center font-semibold mb-4">
          {header}
        </Label>
      )}
      {children}
    </form>
  );
};

export default DashboardForm;
