import { Employee } from "../employee/employ/types";

export type DeliveryDriver = {
  id: number;
  employee: Employee;
  driverLicense: string;
  driverLicenseValidUntil: string;
  status: any;
};

export type DeliveryDriverShift = {
  driver: DeliveryDriver;
  numberOfDeliveriesToday: number;
  hoursWorkedToday: number;
};

export type WarehouseDelivery = {
  status: string;
};
