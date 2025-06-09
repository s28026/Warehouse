"use client";

import DashboardForm from "@/app/dashboard/Form";
import { useNewDelivery } from "../context";
import { Button } from "@/components/ui/button";
import { CircleAlert } from "lucide-react";
import DashboardInput from "@/app/dashboard/Input";
import { useEffect, useState } from "react";
import { setDeliveryAddress } from "../api";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";

type InputAddressPayload = {
  address: string;
};

const NewDeliveryPickupPage = () => {
  const [errors, setErrors] = useState<string[] | null>(null);
  const { register, handleSubmit } = useForm<InputAddressPayload>();
  const { setAddress, deliveryId, setDriver, hasCreatedRef, setDeliveryId } =
    useNewDelivery();
  const router = useRouter();

  useEffect(() => {
    hasCreatedRef.current = false;
    setDriver(null);
    setAddress(null);
    setDeliveryId(null);
  }, []);

  console.log(errors);

  return (
    <form
      onSubmit={handleSubmit((data) => {
        setErrors(null);

        setDeliveryAddress(deliveryId, data.address).then((res) => {
          console.log("RESPONSE", res);
          if (res.message) {
            router.push(`/dashboard/delivery/new/select-driver`);
            setAddress(data.address);
          } else {
            setErrors(res.errors);
          }
        });
      })}
    >
      <DashboardInput label="Pickup address" {...register("address")} />
      {errors?.map((err) => (
        <div
          key={err}
          className="text-red-500 text-xs flex items-center gap-1 mt-2"
        >
          <CircleAlert size={14} />
          {err}
        </div>
      ))}
      <Button className="w-full !mt-2">Check distance</Button>
    </form>
  );
};

export default NewDeliveryPickupPage;
