import { fetchApi } from "@/app/api";

export const getEmployeeDeliveries = (employeePesel: string) =>
  fetchApi(`/employees/${employeePesel}/deliveries`);
