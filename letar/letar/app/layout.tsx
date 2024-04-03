"use client"

import './globals.css'
import {NextUIProvider} from "@nextui-org/react";
import Home from "@/app/Home/Home";

export default function RootLayout({
                                     children,
                                   }: {
  children: React.ReactNode;
}) {
  return (
      <html lang="en">

      <body >
      {children}
      </body>
      </html>
  );
}