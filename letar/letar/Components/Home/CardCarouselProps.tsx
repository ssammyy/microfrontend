import React, {useState, useRef, useEffect} from 'react';
import {FaArrowDown, FaArrowLeft, FaArrowRight, FaGlobe} from 'react-icons/fa';
import {FaMobileAlt, FaCode, FaLaptopCode, FaWordpressSimple, FaHandsHelping} from 'react-icons/fa';

import {Accordion, AccordionItem, Card, CardFooter} from "@nextui-org/react";
import {className} from "postcss-selector-parser";
import {CardBody, CardHeader} from "@nextui-org/card";
import {motion} from 'framer-motion';

import {Divider} from "@nextui-org/react";
import Link from "next/link"; // Importing icons from Font Awesome

interface Service {
    name: string;
    iconClass: React.ElementType;
    description: string;
}

const services: Service[] = [
    {name: 'Mobile Development', iconClass: FaMobileAlt, description: 'We offer mobile app development services.'},
    {name: 'API Development', iconClass: FaCode, description: 'We provide API development solutions.'},
    {
        name: 'Web Development UI/UX',
        iconClass: FaLaptopCode,
        description: 'Our team specializes in web development and UI/UX design.'
    },
    {name: 'WordPress', iconClass: FaWordpressSimple, description: 'We offer WordPress development services.'},
    {
        name: 'Consultancy',
        iconClass: FaHandsHelping,
        description: 'We provide consultancy services to help you achieve your goals.'
    },
    {
        name: 'Pros cms',
        iconClass: FaHandsHelping,
        description: 'We provide consultancy services to help you achieve your goals.'
    },
];

const ServiceCarousel: React.FC = () => {
    const [activeIndex, setActiveIndex] = useState(0);
    const carouselRef = useRef<HTMLDivElement>(null);
    const [showBackwardButton, setShowBackwardButton] = useState(false);
    const [showForwardButton, setShowForwardButton] = useState(true);

    useEffect(() => {
        if (carouselRef.current) {
            const handleScroll = () => {
                const scrollLeft = carouselRef.current.scrollLeft;
                const containerWidth = carouselRef.current.offsetWidth;
                const totalWidth = carouselRef.current.scrollWidth;

                setShowBackwardButton(scrollLeft > 0);
                setShowForwardButton(scrollLeft < totalWidth - containerWidth);
            };

            carouselRef.current.addEventListener('scroll', handleScroll);

            return () => {
                if (carouselRef.current) {
                    carouselRef.current.removeEventListener('scroll', handleScroll);
                }
            };
        }
    }, []);

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

    const handleForwardClick = () => {
        if (carouselRef.current) {
            const cardWidth = carouselRef.current.offsetWidth / services.length;
            const newIndex = Math.min(activeIndex + 1, services.length + 5 );
            carouselRef.current.scrollTo({left: cardWidth * newIndex, behavior: 'smooth'});
            setActiveIndex(newIndex);
        }
    };

    const handleBackwardClick = () => {
        if (carouselRef.current) {
            const cardWidth = carouselRef.current.offsetWidth / services.length;
            const newIndex = Math.max(activeIndex - 1, 0);
            carouselRef.current.scrollTo({left: cardWidth * newIndex, behavior: 'smooth'});
            setActiveIndex(newIndex);
        }
    };

    return (
        <div className="max-w-[1240px] w-full relative text-amber-50 py-20 ">

            <div className="w-full flex-row  py-8">
                <div
                    className=" flex-row  "
                >
                    <motion.div
                        initial="offscreen"
                        whileInView="onscreen"
                        viewport={{once: true, amount: 0.8}}
                        variants={cardVariants}
                        transition={{delay: 1}}
                    >
                        <h1 className="md:text-start text-center text-3xl md:text-6xl w-full bg-clip-text text-transparent"
                            style={{backgroundImage: 'linear-gradient(to top, #404040, #fffbeb)'}}>
                            Our areas of expertise

                        </h1>
                        <h1 className="md:text-start text-center text-neutral-400 font-light w-full md:w-3/5">
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium ad
                            Lorem ipsum , consectetur adipisicing elit. Accusantium ad
                        </h1>
                    </motion.div>

                </div>

                <motion.div
                    initial="offscreen"
                    whileInView="onscreen"
                    viewport={{once: true, amount: 0.8}}
                    variants={cardVariantsTwo}
                    transition={{delay: 1}}
                >
                    <div className="flex space-x-10 mt-6 justify-end">
                        <button className="button-backward " onClick={handleBackwardClick}
                                style={{  color: 'gray', border: 'none', borderRadius: '4px', padding: '8px 16px' }}

                                disabled={!showBackwardButton}>
                            <FaArrowLeft/>
                        </button>
                        <button className="button-forward" onClick={handleForwardClick} disabled={!showForwardButton}
                                style={{  color: 'gray', border: 'none', borderRadius: '4px', padding: '8px 16px' }}
                        >
                            <FaArrowRight/>
                        </button>
                    </div>
                </motion.div>
            </div>
            <motion.div
                initial="offscreen"
                whileInView="onscreen"
                viewport={{once: true, amount: 0.8}}
                variants={cardVariants}
                transition={{delay: 1}}
            >

                <div className=" flex overflow-x-auto md:overflow-x-hidden overflow-hidden  space-x-1 md:space-x-4 w-full" ref={carouselRef}
                     >
                    {services.map((service, index) => (
                        <div
                            key={index}
                            className=" flex w-full  card "
                        >

                            <Card className="md:w-[400px] w-[300px]">
                                <CardHeader className="flex gap-3 ">
                                    <h1 className="text-3xl">
                                        {<service.iconClass/>}
                                    </h1>
                                    <div className="flex flex-col">
                                        <code className="text-md">{service.name}</code>
                                    </div>
                                </CardHeader>
                                <br/>
                                <Divider/>
                                <br/>
                                <CardBody>


                                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur consequatur
                                        consequuntur culpa
                                        eligendi enim facere id impedit ipsum, magni maiores modi necessitatibus numquam
                                        odio omnis praesentium quod quos ratione veritatis.</p>


                                </CardBody>
                                <CardFooter>
                                    {/*<Link*/}
                                    {/*    isExternal*/}
                                    {/*    showAnchorIcon*/}
                                    {/*    href="https://github.com/nextui-org/nextui"*/}
                                    {/*>*/}
                                    {/*    Visit source code on GitHub.*/}
                                    {/*</Link>*/}
                                </CardFooter>
                            </Card>


                        </div>
                    ))}
                </div>
            </motion.div>
        </div>
);
};

export default ServiceCarousel;
