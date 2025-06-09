import { fetchApi } from "../api";
import { Warehouse } from "./page";

export const getWarehouses = () =>
  fetchApi<Warehouse[]>("/warehouses/?withEmployees=true");

export const getWarehouseById = (id: number) =>
  fetchApi<Warehouse>(`/warehouses/${id}?withEmployees=true`);
