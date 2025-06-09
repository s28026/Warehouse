"use client";

import * as React from "react";
import { Check, ChevronsUpDown } from "lucide-react";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { useGlobalState } from "@/app/store/GlobalState";

type Props = {
  setEmployee: any;
  width?: number;
};

export function SelectEmployee({ setEmployee, width = 300 }: Props) {
  const [open, setOpen] = React.useState(false);
  const [value, setValue] = React.useState("");
  const { warehouse } = useGlobalState();

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className={`w-[${width}px] justify-between`}
        >
          {value
            ? warehouse.employees!.find(
                (e) => e.employee.person.fullName === value,
              )?.employee.person.fullName
            : "Select employee..."}
          <ChevronsUpDown className="opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className={`w-[${width}px] !p-0`}>
        <Command>
          <CommandInput placeholder="Search employees..." className="h-9" />
          <CommandList>
            <CommandEmpty>No employees found.</CommandEmpty>
            <CommandGroup>
              {warehouse.employees!.map((we) => (
                <CommandItem
                  key={we.employee.person.pesel}
                  value={we.employee.person.fullName}
                  onSelect={(currentValue) => {
                    setValue(currentValue === value ? "" : currentValue);
                    setOpen(false);
                    setEmployee(currentValue === value ? null : we);
                  }}
                  className="!p-2"
                >
                  {we.employee.person.fullName}
                  <Check
                    className={cn(
                      "ml-auto",
                      value === we.employee.person.fullName
                        ? "opacity-100"
                        : "opacity-0",
                    )}
                  />
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}
