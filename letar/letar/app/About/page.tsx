"use client"
import Navbar from "@/Components/Home/Navbar";
import React from "react";
import {Card, CardFooter, Image, Button} from "@nextui-org/react";
import {CardBody, CardHeader} from "@nextui-org/card";
import {motion} from 'framer-motion';
import {FaGithub, FaLinkedinIn, FaTwitter} from "react-icons/fa";
import Footer from "@/Components/Home/Footer";


export default function Page() {
    const cardVariants = {
        offscreen: {
            y: 50,
            opacity: 0
        },
        onscreen: {
            y: 0,
            opacity: 1,
            transition: {
                type: "spring",
                bounce: 0.4,
                duration: 2
            }
        }
    };
    const cardVariantsTwo = {
        offscreen: {
            y: 50,
            opacity: 0
        },
        onscreen: {
            y: 0,
            opacity: 1,
            transition: {
                type: "spring",
                bounce: 0.4,
                duration: 4
            }
        }
    };
    return (
        <div className="bg-neutral-900 min-h-screen   text-amber-50">
            <Navbar/>
            <div className="max-w-[1240px] mx-auto p-5">

                    <div className="flex flex-col md:flex-row items-center pt-16 space-y-4 md:space-y-0">
                        <h1 className="text-center md:text-start lg:text-start text-3xl md:text-3xl lg:text-4xl bg-clip-text bg-gradient-to-br from-pink-400 to-red-600 w-full md:w-2/5">

                            <motion.div
                                style={{backgroundImage: 'linear-gradient(to bottom, #404040, #fffbeb)'}}

                                className=" bg-clip-text text-transparent"
                                initial="offscreen"
                                whileInView="onscreen"
                                viewport={{once: true, amount: 0.8}}
                                variants={cardVariants}
                                transition={{delay: 10}}
                            >
                                About Us
                            </motion.div>
                        </h1>

                        <h3 className="text-base md:text-lg lg:text-xl text-center mt-4 w-full md:w-3/5">
                            <motion.div
                                className="py-4"
                                initial="offscreen"
                                whileInView="onscreen"
                                viewport={{once: true, amount: 0.8}}
                                variants={cardVariantsTwo}
                                transition={{delay: 5}}
                            >
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium aliquam at,
                                commodi
                                dignissimos distinctio doloremque dolorum et
                                iste iusto labore molestiae nesciunt odio omnis, optio, quia saepe tenetur! Dolores,
                                possimus!
                            </motion.div>
                        </h3>

                    </div>

                <div className="text-center pt-24 text-2xl">
                    <motion.div
                        style={{backgroundImage: 'linear-gradient(to bottom, #404040, #fffbeb)'}}

                        className="py-4 text-transparent bg-clip-text"
                        initial="offscreen"
                        whileInView="onscreen"
                        viewport={{once: true, amount: 0.8}}
                        variants={cardVariants}
                        transition={{delay: 5}}
                    >
                        <h1 className="text-2xl">Meet the team!</h1>
                    </motion.div>
                    <h2 className='justify-center text-sm md:text-base lg:text-lg'>
                        <motion.div
                            className="py-4"
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariantsTwo}
                            transition={{delay: 5}}
                        >
                            Lorem ipsum dolor sit amet,
                            consectetur adipisicing elit.Assumenda
                            autem blanditiis consectetur corporis culpa eius.
                        </motion.div>
                    </h2>
                </div>
                <div className="flex flex-col  md:flex-row md:justify-between space-y-2 md:space-y-0">
                    <motion.div
                        className="py-4"
                        initial="offscreen"
                        whileInView="onscreen"
                        viewport={{once: true, amount: 0.8}}
                        variants={cardVariants}
                        transition={{delay: 1}}
                    >
                        <Card className="py-4">
                            <CardHeader className="pb-0 pt-2 px-4 flex-col items-start">
                                <p className="text-tiny uppercase font-bold">Samuel Boniface</p>
                                <small className="text-default-500">C.E.O</small>
                                <h4 className="cursor-pointer font-bold flex justify-between space-x-6">
                                    <FaLinkedinIn/>
                                    <FaTwitter/> <FaGithub/>

                                </h4>
                            </CardHeader>
                            <CardBody className=" flex items-center justify-center overflow-visible py-2">
                                <div className=" rounded-xl overflow-hidden h-[300px] w-[270px]">
                                    <Image
                                        alt="Card background"
                                        className="object-cover rounded-xl w-full h-full"
                                        src="/images/prof.jpeg"
                                    />
                                </div>
                            </CardBody>
                        </Card>
                    </motion.div>

                    <motion.div
                        className="py-4"
                        initial="offscreen"
                        whileInView="onscreen"
                        viewport={{once: true, amount: 0.8}}
                        variants={cardVariants}
                        transition={{delay: 5}}
                    >

                        <Card className="py-4">
                            <CardHeader className="pb-0 pt-2 px-4 flex-col items-start">
                                <p className="text-tiny uppercase font-bold">George Kabiru</p>
                                <small className="text-default-500">C.E.O</small>
                                <h4 className="cursor-pointer font-bold flex justify-between space-x-6">
                                    <FaLinkedinIn/>
                                    <FaTwitter/> <FaGithub/>

                                </h4></CardHeader>
                            <CardBody className="flex items-center justify-center overflow-visible py-2 relative">
                                <div className="relative rounded-xl overflow-hidden h-[300px] w-[270px]">
                                    <Image
                                        alt="Card background"
                                        className="object-cover rounded-xl"
                                        src="/images/tim.jpeg"

                                    />
                                </div>
                            </CardBody>
                        </Card>
                    </motion.div>

                    <motion.div
                        className="py-4"
                        initial="offscreen"
                        whileInView="onscreen"
                        viewport={{once: true, amount: 0.8}}
                        variants={cardVariants}
                        transition={{delay: 10}}
                    >
                        <Card className="py-4">
                            <CardHeader className="pb-0 pt-2 px-4 flex-col items-start">
                                <p className="text-tiny uppercase font-bold">Derrick Mwomore</p>
                                <small className="text-default-500">C.E.O</small>
                                <h4 className="cursor-pointer font-bold flex justify-between space-x-6">
                                    <FaLinkedinIn/>
                                    <FaTwitter/> <FaGithub/>

                                </h4>
                            </CardHeader>
                            <CardBody className="flex items-center justify-center overflow-visible py-2 relative">
                                <div className="relative rounded-xl overflow-hidden h-[300px] w-[270px]">
                                    <Image
                                        alt="Card background"
                                        className="object-cover rounded-xl w-full h-full"
                                        src="/images/musk.jpeg"
                                    />
                                </div>
                            </CardBody>
                        </Card>
                    </motion.div>


                </div>


                    <div className="flex flex-col md:flex-row items-center pt-16 pb-16 space-y-4 md:space-y-0 ">
                        <h1 className="text-center md:text-start lg:text-start text-3xl md:text-3xl lg:text-4xl bg-clip-text bg-gradient-to-br from-pink-400 to-red-600 w-full md:w-2/5">
                            <motion.div
                                style={{backgroundImage: 'linear-gradient(to bottom, #404040, #fffbeb)'}}

                                className=" bg-clip-text text-transparent"
                                initial="offscreen"
                                whileInView="onscreen"
                                viewport={{once: true, amount: 0.8}}
                                variants={cardVariants}
                                transition={{delay: 10}}
                            >
                                Simplifying Workflows
                            </motion.div>
                        </h1>


                        <h3 className="text-base md:text-lg lg:text-xl text-center mt-4 w-full md:w-3/5">
                            <motion.div

                                className="py-4"
                                initial="offscreen"
                                whileInView="onscreen"
                                viewport={{once: true, amount: 0.8}}
                                variants={cardVariantsTwo}
                                transition={{delay: 10}}
                            >
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium aliquam at,
                                commodi
                                dignissimos distinctio doloremque dolorum et
                                iste iusto labore molestiae nesciunt odio omnis, optio, quia saepe tenetur! Dolores,
                                possimus!
                                <br/>

                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias architecto consectetur
                                doloremque, dolores doloribus esse est exercitationem facilis
                                in itaque necessitatibus non omnis praesentium rem sapiente, suscipit tenetur ullam
                                voluptates.
                            </motion.div>
                        </h3>

                    </div>

                    <Footer />

            </div>

        </div>
)

}