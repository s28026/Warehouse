export type Person = {
  pesel: string;
  firstName: string;
  lastName: string;
  fullName: string;
  dateOfBirth: Date;
};

export type WarehouseEmployee = {
  warehouseId: number;
  employee: Employee;
  isOperator: boolean;
  isWorker: boolean;
};

export type Employee = {
  person: Person;

  phone: string;
  salary: number;

  employedAt: Date;
  isEmployed: boolean;
  terminatedAt?: Date;

  shiftStart?: Date;
  shiftEnd?: Date;

  breaksTakenToday?: number[];
};

export const validateEmployeePayload = (
  payload: EmployeePayload,
): string | null => {
  if (!payload.person.pesel || payload.person.pesel.length !== 11) {
    return "Invalid PESEL number.";
  }
  if (!payload.person.firstName || !payload.person.lastName) {
    return "First name and last name are required.";
  }
  if (!payload.person.date || isNaN(new Date(payload.person.date).getTime())) {
    return "Invalid date of birth.";
  }
  if (new Date(payload.person.date) >= new Date()) {
    return "Date of birth must be in the past.";
  }
  if (!payload.phone || payload.phone.length != 9) {
    return "Phone number must be 9 digits.";
  }
  if (payload.salary <= 0) {
    return "Salary must be a positive number.";
  }
  if (payload.warehouse && !payload.warehouse.id) {
    return "Warehouse ID is required.";
  }
  if (payload.driver && !payload.driver.driverLicense) {
    return "Driver license is required for drivers.";
  }
  if (
    payload.driver &&
    !payload.driver.driverLicenseValidUntil &&
    new Date(payload.driver.driverLicenseValidUntil) <= new Date()
  ) {
    return "Driver license must be valid in the future.";
  }
  if (
    payload.warehouse?.operator &&
    !payload.warehouse.operator.driverLicense
  ) {
    return "Driver license is required for warehouse operators.";
  }

  return null;
};

export type EmployeePayload = {
  person: {
    pesel: string;
    firstName: string;
    lastName: string;
    date: string;
  };
  phone: string;
  salary: number;
  warehouse?: {
    id: number;
    worker?: {
      capacity: number;
    };
    operator?: {
      driverLicense: string;
      driverLicenseValidUntil: string;
      vehicleType: string;
    };
  };
  driver?: {
    driverLicense: string;
    driverLicenseValidUntil: string;
  };
};
