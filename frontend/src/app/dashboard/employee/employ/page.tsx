"use client";

import { Button } from "@/components/ui/button";
import DashboardForm from "../../Form";
import DashboardInput from "../../Input";
import { useForm } from "react-hook-form";
import { Employee, EmployeePayload, validateEmployeePayload } from "./types";
import { useState } from "react";
// import { useRouter } from "next/navigation";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import { ChevronsUpDown, CircleAlert } from "lucide-react";
import { fetchApi } from "@/app/api";
import { useRouter } from "next/navigation";

const submitData = (body: EmployeePayload) =>
  fetchApi<Employee>("/employees/", {
    method: "POST",
    body: JSON.stringify({
      person: {
        pesel: body.person.pesel,
        firstName: body.person.firstName,
        lastName: body.person.lastName,
        dateOfBirth: new Date(body.person.date).toISOString(),
      },
      phoneNumber: body.phone,
      salary: body.salary,
      warehouse: body.warehouse && {
        warehouseId: body.warehouse.id,
        worker: body.warehouse.worker && {
          capacity: body.warehouse.worker.capacity,
        },
        operator: body.warehouse.operator && {
          driverLicense: body.warehouse.operator.driverLicense,
          driverLicenseValidUntil: new Date(
            body.warehouse.operator.driverLicenseValidUntil,
          ).toISOString(),
          vehicleType: body.warehouse.operator.vehicleType,
        },
      },
      deliveryDriver: body.driver && {
        driverLicense: body.driver.driverLicense,
        driverLicenseValidUntil: new Date(
          body.driver.driverLicenseValidUntil,
        ).toISOString(),
      },
    }),
  });

const DashboardEmployeeEmployPage = () => {
  const { register, handleSubmit } = useForm<EmployeePayload>();
  const [errors, setErrors] = useState<string[]>([]);
  const [isWarehouseOpen, setIsWarehouseOpen] = useState(false);
  const [isWorkerOpen, setIsWorkerOpen] = useState(false);
  const [isOperatorOpen, setIsOperatorOpen] = useState(false);
  const [isDriverOpen, setIsDriverOpen] = useState(false);
  const router = useRouter();

  const onSubmit = (data: EmployeePayload) => {
    setErrors([]);

    if (!isWarehouseOpen) {
      data.warehouse = undefined;
    } else {
      if (!isWorkerOpen && !isOperatorOpen) {
        setErrors(["You must provide either Worker or Operator information."]);
        return;
      }

      if (!isWorkerOpen) data.warehouse!.worker = undefined;
      if (!isOperatorOpen) data.warehouse!.operator = undefined;
    }
    if (!isDriverOpen) data.driver = undefined;

    const validationError = validateEmployeePayload(data);
    if (validationError) {
      setErrors([validationError]);
      return;
    }

    submitData(data).then((result) => {
      if (result.errors) {
        setErrors(result.errors);
      } else router.push("/dashboard/");
    });
  };

  return (
    <DashboardForm header="Employ Employee" onSubmit={handleSubmit(onSubmit)}>
      <DashboardInput label="PESEL" {...register("person.pesel")} />
      <DashboardInput label="First Name" {...register("person.firstName")} />
      <DashboardInput label="Last Name" {...register("person.lastName")} />
      <DashboardInput label="Date Of Birth" {...register("person.date")} />
      <DashboardInput label="Phone Number" {...register("phone")} />
      <DashboardInput label="Salary" {...register("salary")} />
      <Collapsible
        open={isDriverOpen}
        onOpenChange={setIsDriverOpen}
        className="flex flex-col gap-2"
      >
        <div className="flex items-center justify-between gap-4 px-4">
          <h4 className="text-sm font-semibold">Add Driver Information</h4>
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
            {...register("driver.driverLicense")}
          />
          <DashboardInput
            label="Driver License Valid Until"
            {...register("driver.driverLicenseValidUntil")}
          />
        </CollapsibleContent>
      </Collapsible>
      <Collapsible
        open={isWarehouseOpen}
        onOpenChange={setIsWarehouseOpen}
        className="flex flex-col gap-2"
      >
        <div className="flex items-center justify-between gap-4 px-4">
          <h4 className="text-sm font-semibold">Add Warehouse Information</h4>
          <CollapsibleTrigger asChild>
            <Button variant="ghost" size="icon" className="size-8">
              <ChevronsUpDown />
              <span className="sr-only">Toggle</span>
            </Button>
          </CollapsibleTrigger>
        </div>
        <CollapsibleContent className="flex flex-col gap-2">
          <DashboardInput label="Warehouse ID" {...register("warehouse.id")} />
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
              <h4 className="text-xs font-semibold">Add Worker Information</h4>
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
                {...register("warehouse.worker.capacity")}
              />
            </CollapsibleContent>
          </Collapsible>
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
              <h4 className="text-xs font-semibold">
                Add Operator Information
              </h4>
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
                {...register("warehouse.operator.driverLicense")}
              />
              <DashboardInput
                label="Driver License Valid Until"
                {...register("warehouse.operator.driverLicenseValidUntil")}
              />
              <DashboardInput
                label="Vehicle Type"
                {...register("warehouse.operator.vehicleType")}
              />
            </CollapsibleContent>
          </Collapsible>
        </CollapsibleContent>
      </Collapsible>
      {errors.length > 0 && (
        <div className="text-red-500 text-xs flex flex-col gap-1">
          {errors.map((err, index) => (
            <div key={index} className="flex items-center gap-2">
              <CircleAlert size={16} />
              {err}
            </div>
          ))}
        </div>
      )}
      <Button>Submit</Button>
    </DashboardForm>
  );
};

export default DashboardEmployeeEmployPage;
