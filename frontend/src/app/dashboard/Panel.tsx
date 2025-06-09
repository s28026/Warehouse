"use client";

type Props = {
  label: string;
  children: React.ReactNode;
};

const DashboardPanel = ({ label, children }: Props) => {
  return (
    <div>
      <h1 className="text-sm !pb-2">⚙️ {label}</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">{children}</div>
    </div>
  );
};

export default DashboardPanel;
