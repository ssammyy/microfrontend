'use client'
import React from 'react'
import Image from "next/image";
import emailIcon from '../public/email.svg'
import { useInView } from 'react-intersection-observer';
import {useEffect} from "react";
import {useAnimation} from "framer-motion";
import {motion} from "framer-motion";


function Cta() {

    const { ref, inView } = useInView({
        threshold: 0.1,
    });
    const controls = useAnimation();

    useEffect(() => {
        if (inView) {
            console.log('element in view')
            controls.start("visible");
        } else {
            controls.start("hidden");
            console.log('element hidden')
        }
    }, [controls, inView]);
    return (
        <div className='bg-gradient-to-r from-rose-100 to-teal-100'>
            <div className='max-w-screen-xl lg:flex lg:justify-around  mx-auto text-white pt-16 pb-16'>
                <motion.div
                    ref={ref}
                    animate={controls}
                    initial="hidden"
                    variants={{
                        hidden: { opacity: 0 },
                        visible: { opacity: 1 },
                    }}
                    transition={{duration: .5}}
                    className='text-5xl lg:flex '

                >
                        <Image
                            src={emailIcon}
                            alt='email icon'
                            width={150}
                            height={150}
                            className='mb-3 '

                        />
                        <h1 className='lg:ml-7 mt-10 bg-[radial-gradient(ellipse_at_right,_var(--tw-gradient-stops))] from-sky-400 to-indigo-900 bg-clip-text text-transparent'>Signup for a
                            <br/>
                            Weekly news letter
                        </h1>

                </motion.div>


                {/*email input section*/}
                <motion.div
                    ref={ref}
                    animate={controls}
                    initial="hidden"
                    variants={{
                        hidden: { opacity: 0 },
                        visible: { opacity: 1 },
                    }}
                    transition={{duration: 2, delay:1}}
                >
                    <div className=" p-4 flex justify-center lg:mt-10">
                        <div className="flex  rounded">
                            <input type="email" className="px-4 py-2 w-auto rounded lg:w-80 lg:ml-10 text-black decoration-0" placeholder="Enter email here"/>
                            <button className="px-4 text-white bg-amber-600 ml-2.5 rounded">
                                Subscribe
                            </button>
                        </div>
                    </div>
                </motion.div>


            </div>
        </div>
    )
}

export default Cta
