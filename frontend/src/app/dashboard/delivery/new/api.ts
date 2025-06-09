import { WarehouseDelivery } from "../types";
import { fetchApi } from "@/app/api";

export const getDelivery = (deliveryId: number) =>
  fetchApi<WarehouseDelivery>(`/deliveries/${deliveryId}`);

export const markDeliveryAsAwaitingDriver = () =>
  fetchApi<BasicResponse>("/deliveries/awaiting-driver");

export const getDeliveryDrivers = (deliveryId: number) =>
  fetchApi<any>(`/deliveries/${deliveryId}/available-delivery-drivers`);

type BasicResponse = {
  message?: string;
  error?: string;
};

export const setDeliveryAddress = (deliveryId: number, address: string) =>
  fetchApi<BasicResponse>(`/deliveries/${deliveryId}/pickup-address/`, {
    method: "POST",
    body: JSON.stringify({ address }),
  });

export const createDelivery = (warehouseId: number) =>
  fetchApi<any>(`/deliveries/`, {
    method: "POST",
    body: JSON.stringify({ warehouseId }),
  });

export const assignDeliveryDriver = (deliveryId: number, driverId: number) =>
  fetchApi<any>(`/deliveries/${deliveryId}/assigned-driver/${driverId}`, {
    method: "POST",
  });

export const sendDeliveryConfirmationSMS = (
  deliveryId: number,
  driverId: number,
) =>
  fetchApi<BasicResponse>(
    `/deliveries/${deliveryId}/sms-confirmation/${driverId}`,
    { method: "POST" },
  );

export const fileComplaint = (employeePesel: string, description: string) =>
  fetchApi<BasicResponse>(`/employees/${employeePesel}/complaint`, {
    method: "POST",
    body: JSON.stringify({ description }),
  });
