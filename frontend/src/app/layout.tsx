"use client";

import "./globals.css";
import { GlobalStateProvider } from "./store/GlobalState";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <div className="!m-auto w-128">
          <GlobalStateProvider>{children}</GlobalStateProvider>
        </div>
      </body>
    </html>
  );
}
