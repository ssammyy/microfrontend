import Navbar from "@/components/Navbar";
import Hero from "@/components/Hero";
import blogs from '../public/Assets/Blogs.json'

import {Inter} from 'next/font/google'
import './globals.css'
import CTA from "@/components/CTA";
import Latest from "@/components/Latest";
import TopHorizontal from "@/components/TopHorizontal";
import Footer from "@/components/Footer";

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
                <TopHorizontal/>
                <CTA/>
                <Latest blogs={blogs}/>
                <Footer/>
            {children}
            </body>
        </html>
    );
}