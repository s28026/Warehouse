import { NewDeliveryProvider } from "./context";
import NewDeliveryWrapper from "./wrapper";

const NewDeliveryLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <NewDeliveryProvider>
      <NewDeliveryWrapper>{children}</NewDeliveryWrapper>
    </NewDeliveryProvider>
  );
};

export default NewDeliveryLayout;
