// import { Providers } from "./providers";
import Navbar from "@/components/Navbar";
import Hero from "@/components/Hero";

import { Inter } from 'next/font/google'
import './globals.css'

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
        <body>
            <Navbar/>
            <Hero/>
            {children}
        </body>
        </html>
    );
}