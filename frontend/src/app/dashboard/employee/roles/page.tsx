"use client";

import { useState } from "react";
import { SelectEmployee } from "../Select";
import { Employee, EmployeePayload } from "../employ/types";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import DashboardInput from "../../Input";
import DashboardForm from "../../Form";
import { ChevronsUpDown } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import { changeToOperator, changeToWorker } from "./api";

// type RoleChangePayload = {
// 	worker?: EmployeePayload['warehouse']['worker'];
// 	operator?: EmployeePayload['warehouse']['']
// };
export type RoleChangePayload = {} & EmployeePayload["warehouse"];

const DashboardEmployeeRolesPage = () => {
  const { register, handleSubmit } = useForm<RoleChangePayload>();
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [isWorkerOpen, setIsWorkerOpen] = useState(false);
  const [isOperatorOpen, setIsOperatorOpen] = useState(false);

  console.log(employee);

  return (
    <DashboardForm
      header="Change Employee Roles"
      onSubmit={handleSubmit((d) => {
        if (!employee) return;

        if (isWorkerOpen) changeToWorker(employee.person.pesel, d.worker);
        else if (isOperatorOpen)
          changeToOperator(employee.person.pesel, d.operator);

        setEmployee(null);
        setIsWorkerOpen(false);
        setIsOperatorOpen(false);
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
