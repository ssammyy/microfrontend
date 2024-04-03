'use client'
import React, {useRef} from 'react'
import Image from "next/image";
import emailIcon from '../../public/email.svg'
import useOnScreen from "@/Utils/UseOnScreen";
import {useEffect} from "react";
import {useAnimation} from "framer-motion";
import {motion} from "framer-motion";


function Cta() {


    const controls = useAnimation();

    const ref = useRef<HTMLDivElement>(null);
    const isVisible = useOnScreen(ref);
    return (
        <div className='bg-gradient-to-r from-rose-100 to-teal-100'>
            <div className='lg:flex text-white pt-16 md:ml-64 pb-16 max-w-[1240px]'>
                <div className='flex  '>
                    <div>
                        <h1 className='lg:ml-7 text-2xl mt-4 bg-[radial-gradient(ellipse_at_right,_var(--tw-gradient-stops))] from-sky-400 to-indigo-900 bg-clip-text text-transparent'>
                            stay in the loop with our weekly news letter
                        </h1>
                    </div>


                    <div className=" p-4 flex justify-center max-h-16">
                        <div className="flex  rounded">
                            <input type="email"
                                   className="px-4 py-2 w-auto rounded lg:w-80 lg:ml-10 text-black decoration-0"
                                   placeholder="Enter email here"/>
                            <button className="px-4 text-white bg-amber-600 ml-2.5 rounded">
                                Subscribe
                            </button>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    )
}

export default Cta
