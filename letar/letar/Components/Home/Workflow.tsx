import LandingPage from "@/Components/Home/LandingPage";
import {motion} from 'framer-motion';
import { useMediaQuery } from 'react-responsive';


export default function Workflow() {
    const isMobile = useMediaQuery({query : '(max-width: 768px)'})
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
        <div className="bg-neutral-900 mt-56 flex flex-col w-full max-w-[1240px]  items-center text-amber-50 ">
            <div className="flex  md:flex-row flex-col  w-full">
                <div className="md:w-2/5  items-baseline flex ">
                    {
                        !isMobile &&
                        <motion.div
                            className="sticky top-40 md:top-96 pb-32"
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            <h1 className="bg-clip-text  text-6xl text-transparent"
                                style={{backgroundImage: 'linear-gradient(to top, #404040, #fffbeb)'}}
                            >

                                Seamless process from
                                <br/>
                                Kickoff to shipping


                            </h1>
                        </motion.div>
                    }


                </div>
                <div className="md:w-3/5  flex flex-col  ">
                    {
                        isMobile &&
                        <motion.div
                            className="sticky text-3xl text-center  top-32 md:top-96 pb-32"
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            <h1 className="bg-clip-text  text-3xl text-transparent"
                                style={{backgroundImage: 'linear-gradient(to top, #404040, #fffbeb)'}}
                            >

                                Seamless process from
                                <br/>
                                Kickoff to shipping


                            </h1>
                        </motion.div>
                    }

                    <div
                        className="items-center animation-border-three h-80  align-middle justify-center border-neutral-500 border-r-0 sticky top-72 border p-4 flex flex-col ">
                        <code className=" text-2xl  ">
                            Kickoff
                        </code>
                        <motion.div

                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque, cumque deleniti dicta
                            dignissimos dolor
                            dolorem fugiat harum in nobis nulla obcaecati praesentium quam quas quis sint sunt veniam?
                            Veniam, voluptas.
                            dolorem fugiat harum in nobis nulla obcaecati praesentium quam quas quis sint sunt veniam?
                            Veniam, voluptas.

                        </motion.div>

                    </div>
                    <div
                        className="z-20 bg-neutral-900 sticky top-72 h-80 items-center animation-border align-middle justify-center border-neutral-500 border-l-0  border p-4 flex flex-col ">
                        <code className=" text-2xl  ">
                            Plan
                        </code>
                        <motion.div
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque, cumque deleniti dicta
                            dignissimos dolor
                            dolorem fugiat harum in nobis nulla obcaecati praesentium quam quas quis sint sunt veniam?
                            Veniam, voluptas.
                        </motion.div>
                    </div>
                    <div
                        className="z-30 bg-neutral-900 sticky top-72 h-80 items-center animation-border align-middle justify-center border-neutral-500 border-r-0  border p-4 flex flex-col ">
                        <code className=" text-2xl  ">
                            Execute
                        </code>
                        <motion.div
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque, cumque deleniti dicta
                            dignissimos dolor
                            dolorem fugiat harum in nobis nulla obcaecati praesentium quam quas quis sint sunt veniam?
                            Veniam, voluptas.
                        </motion.div>
                    </div>
                    <div
                        className="z-40 bg-neutral-900 sticky top-72 h-80 items-center animation-border align-middle justify-center border-neutral-500 border-l-0  border p-4 flex flex-col ">
                        <code className=" text-2xl  ">
                            Ship
                        </code>
                        <motion.div
                            initial="offscreen"
                            whileInView="onscreen"
                            viewport={{once: true, amount: 0.8}}
                            variants={cardVariants}
                            transition={{delay: 1}}
                        >
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque, cumque deleniti dicta
                            dignissimos dolor
                            dolorem fugiat harum in nobis nulla obcaecati praesentium quam quas quis sint sunt veniam?
                            Veniam, voluptas.
                        </motion.div>

                    </div>
                    <div
                        className="z-50  sticky top-72 h-80 items-center animation-border-two align-middle justify-center border-neutral-500 border-r-0 border-t-0 border-b-0 border-l-neutral-900 border p-4 flex flex-col ">
                        <div className="p-2 bg-neutral-900">
                            <code className=" text-2xl  ">
                                Closure & support
                            </code>
                            <motion.div
                                initial="offscreen"
                                whileInView="onscreen"
                                viewport={{once: true, amount: 0.8}}
                                variants={cardVariants}
                                transition={{delay: 1}}
                            >
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque, cumque deleniti dicta
                                dignissimos dolor
                                dolorem fugiat harum in nobis nulla obcaecati praesentium quam quas quis sint sunt
                                veniam?
                                Veniam, voluptas.
                            </motion.div>
                        </div>


                    </div>

                </div>

            </div>
        </div>
    );
}