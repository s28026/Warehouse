import { fetchApi } from "@/app/api";
import { RoleChangePayload } from "./page";

export const changeToWorker = (
  employeePesel: string,
  dto: RoleChangePayload["worker"],
) =>
  fetchApi<void>(`/employees/${employeePesel}/worker`, {
    method: "POST",
    body: JSON.stringify(dto),
  });

export const changeToOperator = (
  employeePesel: string,
  dto: RoleChangePayload["operator"],
) =>
  fetchApi<void>(`/employees/${employeePesel}/operator`, {
    method: "POST",
    body: JSON.stringify(dto),
  });
