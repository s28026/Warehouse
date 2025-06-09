"use client";

import { useEffect, useState } from "react";
import { SelectEmployee } from "../Select";
import { Employee, EmployeePayload } from "../employ/types";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import DashboardInput from "../../Input";
import DashboardForm from "../../Form";
import { ChevronsUpDown, CircleAlert } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import { changeToOperator, changeToWorker } from "./api";
import { isIP } from "net";

// type RoleChangePayload = {
// 	worker?: EmployeePayload['warehouse']['worker'];
// 	operator?: EmployeePayload['warehouse']['']
// };
export type RoleChangePayload = {} & EmployeePayload["warehouse"];

const DashboardEmployeeRolesPage = () => {
  const { register, handleSubmit } = useForm<RoleChangePayload>();
  const [errors, setErrors] = useState<string[] | null>(null);
  const [employee, setEmployee] = useState<WarehouseEmployee | null>(null);
  const [isWorkerOpen, setIsWorkerOpen] = useState(false);
  const [isOperatorOpen, setIsOperatorOpen] = useState(false);

  useEffect(() => {
    setErrors(null);
  }, [isWorkerOpen, isOperatorOpen, employee]);

  console.log(employee);

  return (
    <DashboardForm
      header="Change Employee Roles"
      onSubmit={handleSubmit((d) => {
        setErrors(null);

        if (!employee) return;

        const handleRes = (d) => {
          if (d.errors) {
            setErrors(d.errors);
            return;
          }

          setEmployee(null);
          setIsWorkerOpen(false);
          setIsOperatorOpen(false);
        };
        const pesel = employee.employee.person.pesel;

        if (isWorkerOpen) changeToWorker(pesel, d.worker).then(handleRes);
        else if (isOperatorOpen)
          changeToOperator(pesel, d.operator).then(handleRes);
      })}
    >
      <div className="!mx-auto">
        <SelectEmployee setEmployee={setEmployee} />
      </div>
      {employee && (
        <div>
          {employee.isOperator && (
            <Collapsible
              open={isWorkerOpen}
              onOpenChange={() =>
                setIsWorkerOpen((x) => {
                  if (isOperatorOpen) setIsOperatorOpen(false);
                  return !x;
                })
              }
              className="flex flex-col gap-2"
            >
              <div className="flex items-center justify-between gap-4 px-4">
                <h4 className="text-xs font-semibold">Change to worker</h4>
                <CollapsibleTrigger asChild>
                  <Button variant="ghost" size="icon" className="size-8">
                    <ChevronsUpDown />
                    <span className="sr-only">Toggle</span>
                  </Button>
                </CollapsibleTrigger>
              </div>
              <CollapsibleContent className="flex flex-col gap-2">
                <DashboardInput
                  label="Capacity"
                  {...register("worker.capacity")}
                />
              </CollapsibleContent>
            </Collapsible>
          )}
          {employee.isWorker && (
            <Collapsible
              open={isOperatorOpen}
              onOpenChange={() =>
                setIsOperatorOpen((x) => {
                  if (isWorkerOpen) setIsWorkerOpen(false);
                  return !x;
                })
              }
              className="flex flex-col gap-2"
            >
              <div className="flex items-center justify-between gap-4 px-4">
                <h4 className="text-xs font-semibold">Change to operator</h4>
                <CollapsibleTrigger asChild>
                  <Button variant="ghost" size="icon" className="size-8">
                    <ChevronsUpDown />
                    <span className="sr-only">Toggle</span>
                  </Button>
                </CollapsibleTrigger>
              </div>
              <CollapsibleContent className="flex flex-col gap-2">
                <DashboardInput
                  label="Driver License"
                  {...register("operator.driverLicense")}
                />
                <DashboardInput
                  label="Driver License Valid Until"
                  {...register("operator.driverLicenseValidUntil")}
                />
                <DashboardInput
                  label="Vehicle Type"
                  {...register("operator.vehicleType")}
                />
              </CollapsibleContent>
            </Collapsible>
          )}
          {errors && (
            <div className="text-red-500 !py-1">
              {errors.map((error, index) => (
                <div key={error} className="flex items-center gap-1">
                  <CircleAlert size={16} />
                  <p className="text-xs">{error}</p>
                </div>
              ))}
            </div>
          )}
          <Button
            disabled={!(isWorkerOpen || isOperatorOpen)}
            type="submit"
            className="!mt-2 w-full"
          >
            Change Role
          </Button>
        </div>
      )}
    </DashboardForm>
  );
};

export default DashboardEmployeeRolesPage;
