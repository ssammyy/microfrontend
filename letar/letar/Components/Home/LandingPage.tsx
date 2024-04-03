import React, {useEffect, useState} from 'react';
import {motion} from 'framer-motion';

import {
    FaBalanceScale,
    FaBrain, FaBriefcase,
    FaChevronDown,
    FaCode, FaFire, FaGlobe,
    FaIntercom, FaLightbulb,
    FaLinkedinIn, FaMapMarkedAlt,
    FaMobileAlt,
    FaNetworkWired,
    FaNeuter,
    FaPhone
} from "react-icons/fa";
import Link from "next/link";
import TypingEffect from "@/Utils/TypingAnimation";
import Marquee from "react-fast-marquee";
import {FaMobileScreen} from "react-icons/fa6";
import {AiFillHeart} from "react-icons/ai";
import Workflow from "@/Components/Home/Workflow";
import CtaMain from "@/Components/Home/CTAM";
import {Card} from "@nextui-org/react";
import ServiceCarousel from "@/Components/Home/CardCarouselProps";

interface TypingEffectProps {
    text: string;
    className?: string;
    Delay?: number;
}

class LandingPage extends React.Component {
    state = {
        cursorX: 0,
        cursorY: 0,
    };

    handleChevronClick = () => {
        window.scroll({
            top: window.innerHeight + window.pageYOffset,
            behavior: "smooth"
        });
    }

    componentDidMount() {
        document.addEventListener('mousemove', this.handleMouseMove);
    }

    componentWillUnmount() {
        document.removeEventListener('mousemove', this.handleMouseMove);
    }

    handleMouseMove = (e: MouseEvent) => {
        const delay = 200;
        const newX = e.clientX;
        const newY = e.clientY;

        setTimeout(() => {
            this.setState({cursorX: newX, cursorY: newY});
        }, delay);
    };

    render() {
        const {cursorX, cursorY} = this.state;
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
        const cards = Array.from(Array(20).keys()).map((index) => (
            <div key={index} className="w-64 h-64 bg-gray-300">
                Card {index + 1}
            </div>
        ));
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
            <div className="bg-neutral-900  flex flex-col  items-center text-amber-50 px-4">


                <div
                    className=" flex flex-col w-full max-w-[1240px] overflow-hidden mt-8 items-center justify-center align-middle ">

                    <div
                        className="bg-gradient-to-br md:w-1/4 my-8 md:py-4 py-3 text-center from-neutral-900 to-neutral-600 hover:filter  hover:brightness-150 hover:shadow-lg">

                        <code>
                            Drop a line or book a demo.
                        </code>
                    </div>


                    <div
                    >
                        <motion.div
                            style={{backgroundImage: 'linear-gradient(to bottom, #404040, #fffbeb)'}}
                            className="font-bold text-3xl md:text-6xl px-30 text-center bg-clip-text text-transparent md:space-y-8"

                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            Delivering game Changing
                            <br/>
                            <div className=""
                            >
                                Enterprise Solutions.
                            </div>
                        </motion.div>

                        <motion.div
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            <h1 className="text-lg text-neutral-400 text-light pb-4 font-light">
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet commodi doloribus est?
                            </h1>
                        </motion.div>

                    </div>

                    <Marquee gradient={true} gradientColor="#171717"
                             className="space-y-20 flex max-w-[1240px] justify-between overflow-hidden">
    <span className="mr-20 flex flex-col items-center">
        <h1 className="text-7xl">
            <FaMobileAlt/>
        </h1>
        Mobile Apps
    </span>
                        <span className="mr-20 flex flex-col items-center">
        <h1 className="text-7xl">
            <FaCode/>
        </h1>
        Api Development
    </span>
                        <span className="mr-20 flex flex-col items-center">
        <h1 className="text-7xl">
            <FaGlobe/>
        </h1>
        Web Applications
    </span>
                        <span className="mr-20 flex flex-col items-center">
        <h1 className="text-7xl">
            <FaBrain/>
        </h1>
        Machine Learning
    </span>
                        <span className="mr-20 flex flex-col items-center">
        <h1 className="text-7xl">
            <FaMapMarkedAlt/>
        </h1>
        GIS
    </span>
                        <span className="mr-20 flex flex-col items-center">
        <h1 className="text-7xl">
            <FaBriefcase/>
        </h1>
        Consultancy
    </span>
                    </Marquee>


                    {/*OUR CORE VALUES */}
                    <motion.div
                        initial="offscreen"
                        whileInView="onscreen"
                        viewport={{once: true, amount: 0.8}}
                        variants={cardVariants}
                        transition={{delay: 1}}
                    >
                        <h1 className="bg-clip-text mt-32 text-3xl text-transparent"
                            style={{backgroundImage: 'linear-gradient(to top, #404040, #fffbeb)'}}
                        >

                            What moves us



                        </h1>
                    </motion.div>
                    <div
                        className="mt-10 flex  text-amber-50 w-full max-w-[1240px]  items-center justify-center align-middle">


                        <div className=" flex md:flex-row   flex-col  md:space-x-10">
                            <div
                                className=" items-center rounded-md  align-middle justify-center border-neutral-500 md:border mb-10 border p-4  flex flex-col   ">
                                <motion.div
                                    className="flex justify-center align-middle flex-col items-center "
                                    initial="offscreen"
                                    whileInView="onscreen"
                                    viewport={{once: true, amount: 0.8}}
                                    variants={cardVariants}
                                    transition={{delay: 1}}
                                >
                                    <h1 className="  text-7xl"><FaLightbulb/></h1>

                                    <code className="font-bold">
                                        Innovation
                                    </code>
                                </motion.div>

                                <motion.div
                                    className="flex justify-center align-middle flex-col items-center "
                                    initial="offscreen"
                                    whileInView="onscreen"
                                    viewport={{once: true, amount: 0.8}}
                                    variants={cardVariants}
                                    transition={{delay: 1}}
                                >

                                    <h1>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                                        Aspernatur atque corporis hic inventore magni
                                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti facilis id
                                        molestias mollitia necessitatibus numquam odit officiis,
                                        perspiciatis quaerat quidem tempora unde. Eligendi enim facilis ipsa non
                                        pariatur, quidem sit?
                                    </h1>
                                </motion.div>
                            </div>
                            <div
                                className=" items-center rounded-md  align-middle justify-center border-neutral-500 md:border mb-10 border p-4  flex flex-col   ">
                                <motion.div
                                    className="flex justify-center align-middle flex-col items-center "
                                    initial="offscreen"
                                    whileInView="onscreen"
                                    viewport={{once: true, amount: 0.8}}
                                    variants={cardVariants}
                                    transition={{delay: 1}}
                                >
                                    <h1 className="  text-7xl"><FaFire/></h1>

                                    <code className="font-bold">
                                        Passion
                                    </code>
                                </motion.div>

                                <motion.div
                                    className="flex justify-center align-middle flex-col items-center "
                                    initial="offscreen"
                                    whileInView="onscreen"
                                    viewport={{once: true, amount: 0.8}}
                                    variants={cardVariants}
                                    transition={{delay: 1}}
                                >

                                    <h1>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                                        Aspernatur atque corporis hic inventore magni
                                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti facilis id
                                        molestias mollitia necessitatibus numquam odit officiis,
                                        perspiciatis quaerat quidem tempora unde. Eligendi enim facilis ipsa non
                                        pariatur, quidem sit?
                                    </h1>
                                </motion.div>
                            </div>
                            <div
                                className=" items-center rounded-md  align-middle justify-center border-neutral-500 md:border mb-10 border p-4  flex flex-col   ">
                                <motion.div
                                    className="flex justify-center align-middle flex-col items-center "
                                    initial="offscreen"
                                    whileInView="onscreen"
                                    viewport={{once: true, amount: 0.8}}
                                    variants={cardVariants}
                                    transition={{delay: 1}}
                                >
                                    <h1 className="  text-7xl"><FaBalanceScale/></h1>

                                    <code className="font-bold">
                                        Integrity
                                    </code>
                                </motion.div>

                                <motion.div
                                    className="flex justify-center align-middle flex-col items-center "
                                    initial="offscreen"
                                    whileInView="onscreen"
                                    viewport={{once: true, amount: 0.8}}
                                    variants={cardVariants}
                                    transition={{delay: 1}}
                                >

                                    <h1>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                                        Aspernatur atque corporis hic inventore magni
                                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Deleniti facilis id
                                        molestias mollitia necessitatibus numquam odit officiis,
                                        perspiciatis quaerat quidem tempora unde. Eligendi enim facilis ipsa non
                                        pariatur, quidem sit?
                                    </h1>
                                </motion.div>
                            </div>


                        </div>
                    </div>

                </div>
                <Workflow/>
                <section className="h-screen"></section>
                <CtaMain/>
                <section className="h-screen"></section>
                <ServiceCarousel/>

            </div>

        );
    }
}

export default LandingPage;
