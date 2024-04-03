// pages/index.js
import { motion } from 'framer-motion';
import Navbar from "@/Components/Home/Navbar";
import LandingPage from "@/Components/Home/LandingPage";
import Rskeleton from "@/Components/Home/Rskeleton";
import React from "react";
import Lskeleton from "@/Components/Home/Lskeleton";
import Footer from "@/Components/Home/Footer"
import {FaChevronDown} from "react-icons/fa";
import CTA from "@/Components/Home/CTA";
// import handleChevronClick from "@/Utils/HandleChevronClick";


const handleChevronClick = () => {
    window.scroll({
        top: window.innerHeight + window.pageYOffset,
        behavior: "smooth"
    });
}

export default function Home() {


    return (
        <div className="bg-neutral-900   w-screen">
                <div className="nav">
                    <Navbar/>

                    <LandingPage/>
                    {/*portfolio*/}
                    {/*<Rskeleton imageSrc={"https://images.pexels.com/photos/8422724/pexels-photo-8422724.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"} mainTag={"Retail POS"} description={"Our retail POS solution as a service is the perfect tool for\n" +*/}
                    {/*    "                        small and medium-sized enterprises. It is modern,\n" +*/}
                    {/*    "                        easy to use, and can help you manage your business effectively."} buttonTxt={"request Demo"} bgColor={"bg-amber-50"} subText={"reeliable | convenient"} headColor={'text-gray-500'}/>*/}
                    {/*<Lskeleton imageSrc={"https://images.pexels.com/photos/35969/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"} mainTag={"Geo Location"} description={*/}
                    {/*    "Our geo-location clocking solution provides an efficient and reliable method for tracking employee time based on their geographic location. Ideal for remote and on-the-go employees, this solution ensures accuracy and accountability in your workforce."*/}
                    {/*} buttonTxt={"request Demo"} bgColor={"bg-emerald-50"} subText={'Acccurate | Reliable | scalable '} headColor={'text-neutral-500'}/>*/}
                    
                    {/*<Rskeleton imageSrc={"https://images.pexels.com/photos/6177648/pexels-photo-6177648.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"} mainTag={"Mobile Apps"} description={*/}
                    {/*    "Our cross platform apps are designed to provide seamless performance and user experience on any device or platform. They are developed with flexibility in mind, covering diverse needs and scenarios while promising robust functionality and reliable performance."*/}
                    {/*} buttonTxt={"request Demo"} bgColor={"bg-violet-100"z} subText={'lorem | ipsum | pregtis'} headColor={'text-gray-500'} />*/}


                    {/*<Lskeleton imageSrc={"https://img.freepik.com/free-photo/black-businessman-using-computer-laptop_53876-24736.jpg?w=2000&t=st=1707399863~exp=1707400463~hmac=667ad030335eee5ae5f98303f34eb01c27e9e367e4ec0ba6fcb334251218a3cc"} mainTag={"Marketing"} description={*/}
                    {/*    "Our tailor-made enterprise solutions are designed to address the unique challenges and needs of your business. Leveraging the latest technologies and methodologies, we deliver solutions that improve efficiency, enhance productivity, and drive business performance."*/}
                    {/*} buttonTxt={"request Demo"} bgColor={"bg-pink-100"} subText={'ipsum | lorem | tyros'} headColor={''}/>*/}

                {/*    call To Action*/}
                {/*    <CTA/>*/}

                {/*footer*/}
                    <Footer/>
                </div>
        </div>
    );
}