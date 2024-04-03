import React, { useRef } from 'react';
import TypingEffect from "@/Utils/TypingAnimation";
import useOnScreen from "@/Utils/UseOnScreen";
import { motion } from 'framer-motion';
import { FaChevronDown } from "react-icons/fa";
import handleChevronClick from "@/Utils/HandleChevronClick";

interface PosProps {
    imageSrc: string,
    mainTag: string,
    description: string,
    buttonTxt: string,
    bgColor: string,
    subText: string,
    headColor: string,
}
const paragraphVariant = {
    hidden: { opacity: 0, x: '100vw' },
    visible: { opacity: 1, x: 0,
        transition: { type: 'spring', stiffness: 30, delay: .3, duration:1}
    }
};

const Lskeleton: React.FC<PosProps> = ({ imageSrc, mainTag, description, buttonTxt, bgColor, subText, headColor }) => {
    const ref = useRef<HTMLDivElement>(null);
    const ref2 = useRef<HTMLDivElement>(null);
    const ref3 = useRef<HTMLDivElement>(null);
    const ref4 = useRef<HTMLDivElement>(null);

    const isVisible = useOnScreen(ref);
    const isVisible2 = useOnScreen(ref2);
    const isVisible3 = useOnScreen(ref3);
    const isVisible4 = useOnScreen(ref4);


    return (
        <div className={`${bgColor} min-h-screen flex flex-col md:flex-row justify-center items-center text-black `}>
            <div className="order-last md:order-first w-full h-screen md:w-1/2 flex items-center justify-center">
                <img src={imageSrc} alt="image" className="h-screen object-cover rounded"/>
            </div>

            <div className="flex flex-col w-full md:w-1/2 items-center justify-center p-8">
                <div ref={ref}>
                    <motion.h1
                        className="text-4xl md:text-4xl lg:text-4xl mb-6 bg-clip-text text-transparent"
                        style={{backgroundImage: `linear-gradient(to right, #e66465, #9198e5)`}}
                        variants={{
                            hidden: {opacity: 0, y: -50},
                            visible: {
                                opacity: 1,
                                y: 0,
                                transition: {
                                    duration: 1,
                                    delay: 0.2
                                }
                            }
                        }}
                        initial='hidden'
                        animate={isVisible ? 'visible' : 'hidden'}
                    >
                        {mainTag}
                    </motion.h1>
                </div>

                <div ref={ref4}>
                    <motion.p
                        className="text-center text-xl md:text-2xl mb-4"
                        variants={paragraphVariant}
                        initial="hidden"
                        animate={isVisible4 ? 'visible' : 'hidden'}
                    >
                        {description}
                    </motion.p>
                </div>
                <div ref={ref2}>
                    {isVisible2 && <TypingEffect text={subText} className="text-center md:text-left text-xl md:text-2xl mt-4 mb-4"
                                  delay={80} styles={{opacity: isVisible2 ? 1 : 0}}/>}
                </div>
                <div ref={ref3} className="relative inline-flex group">
                    <motion.button
                        variants={{
                            hidden: {opacity: 0, y: -40},
                            visible: {
                                opacity: 1,
                                y: 0,
                                transition: {
                                    duration: 1,
                                    delay: 0.5
                                }
                            }
                        }}
                        initial='hidden'
                        animate={isVisible3 ? 'visible' : 'hidden'}
                        className="px-6 py-2 mt-4 md:mt-8 bg-gradient-to-r from-red-200 to-green-200 text-black font-bold rounded-xl hover:scale-110 transition-transform"
                    >
                        {buttonTxt}
                    </motion.button>
                </div>
            </div>

            <motion.div
                initial={{opacity: 0}}
                animate={{
                    opacity: 1,
                    y: ["0%", "10%", "0%"],
                }}
                transition={{
                    delay: 3,
                    duration: 2.5,
                    ease: 'easeInOut',
                    loop: Infinity,
                    repeatDelay: 1
                }}
                className="absolute text-2xl inset-x-0 bottom-4 flex items-center justify-center text-amber-600 cursor-pointer"
            >
                <FaChevronDown onClick={handleChevronClick}/>
            </motion.div>
        </div>
    );
};

export default Lskeleton;
