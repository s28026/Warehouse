"use client";

import { useEffect, useState } from "react";
import { SelectEmployee } from "../Select";
import { useRouter } from "next/navigation";

const DeliveriesPage = () => {
  const [employee, setEmployee] = useState(null);
  const router = useRouter();

  useEffect(() => {
    if (employee)
      router.push(
        `/dashboard/employee/deliveries/${employee.employee.person.pesel}`,
      );
  }, [employee]);

  return (
    <div className="flex flex-col gap-4">
      <SelectEmployee setEmployee={setEmployee} />
    </div>
  );
};

export default DeliveriesPage;
