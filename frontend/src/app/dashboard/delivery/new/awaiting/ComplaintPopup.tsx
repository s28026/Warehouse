"use client";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { DialogDescription, DialogTitle } from "@radix-ui/react-dialog";
import { fileComplaint } from "../api";
import { useState } from "react";

type Props = {
  employeePesel: string;
};

const ComplaintPopup = ({ employeePesel }: Props) => {
  const [errors, setErrors] = useState<string[] | null>(null);
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant={"destructive"} className="w-full">
          File a complaint
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px] !p-6">
        <DialogHeader>
          <DialogTitle className="font-bold">File a Complaint</DialogTitle>
          <DialogDescription className="text-xs text-muted-foreground">
            Please provide details about your complaint below.
          </DialogDescription>
        </DialogHeader>
        <form
          className="grid gap-4"
          onSubmit={(d) => {
            d.preventDefault();
            setErrors(null);

            const description = (
              d.currentTarget.elements.namedItem(
                "description",
              ) as HTMLTextAreaElement
            ).value;

            fileComplaint(employeePesel, description).then((res) => {
              if (res.errors) return setErrors(res.errors);

              setOpen(false);
            });
          }}
        >
          <div className="grid gap-4">
            <Textarea name="description" placeholder="Description" rows={4} />
            {errors?.map((err) => (
              <div key={err} className="text-red-500 text-xs">
                {err}
              </div>
            ))}
            <Button type="submit">Submit Complaint</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ComplaintPopup;
