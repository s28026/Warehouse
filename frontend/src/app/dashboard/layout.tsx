"use client";

import { Button } from "@/components/ui/button";
import { Menu } from "lucide-react";
import { useRouter } from "next/navigation";

const DashboardLayout = ({ children }: { children: React.ReactNode }) => {
  const router = useRouter();

  return (
    <div>
      <div className="flex gap-2 items-center !my-6">
        <Menu />
        <Button
          className="!px-2"
          variant={"ghost"}
          onClick={() => router.push("/warehouses")}
        >
          Warehouses
        </Button>
        <Button
          className="!px-2"
          variant={"ghost"}
          onClick={() => router.push("/dashboard")}
        >
          Dashboard
        </Button>
      </div>
      {children}
    </div>
  );
};

export default DashboardLayout;
