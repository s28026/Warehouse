import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";

type Props = {
  children: React.ReactNode;
  href: string;
};

const DashboardButton = ({ children, href, ...rest }: Props) => {
  const router = useRouter();

  return (
    <Button
      onClick={() => router.push(`/dashboard/${href}`)}
      className="w-full"
      {...rest}
    >
      {children}
    </Button>
  );
};

export default DashboardButton;
