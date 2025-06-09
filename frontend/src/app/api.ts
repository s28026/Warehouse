type ErrorResponse = {
  status: "error";
  errors: string[];
};

export const fetchApi = async <T>(
  endpoint: string,
  options?: RequestInit,
): Promise<T | ErrorResponse> => {
  try {
    const res = await fetch(`http://localhost:8080${endpoint}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      ...options,
    });

    if (!res.ok) {
      const errorData = (await res.json()) as ErrorResponse;
      return { status: "error", errors: errorData.errors || ["Unknown error"] };
    }

    const data = (await res.json()) as T;
    return data;
  } catch (err) {
    console.error(err);
    return {
      status: "error",
      errors: [err instanceof Error ? err.message : "Unexpected error"],
    };
  }
};
