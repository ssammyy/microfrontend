import {motion} from 'framer-motion';
import {Spacer} from "@nextui-org/react";
import {Section} from "@react-stately/collections";

export default function CtaMain(){
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
        <div className="flex  justify-center   items-center align-middle max-w-[1024px] ">
            <div className="flex flex-col  text-center w-full">
                <motion.div
                    initial="offscreen"
                    whileInView="onscreen"
                    viewport={{once: true, amount: 0.8}}
                    variants={cardVariants}
                    transition={{delay: 1}}
                >
                    <h1 className="md:text-6xl text-3xl w-full bg-clip-text text-transparent "
                        style={{backgroundImage: 'linear-gradient(to top, #404040, #fffbeb)'}}
                    >
                        Modern approach
                    </h1>
                </motion.div>
                <motion.div
                    initial="offscreen"
                    whileInView="onscreen"
                    viewport={{once: true, amount: 0.8}}
                    variants={cardVariantsTwo}
                    transition={{delay: 1}}
                >
                    <h1 className="text-neutral-400 font-light">
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium ad
                    </h1>
                </motion.div>
                <br/>
                <motion.div
                    initial="offscreen"
                    whileInView="onscreen"
                    viewport={{once: true, amount: 0.8}}
                    variants={cardVariants}
                    transition={{delay: 1}}
                >
                    <h1>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Distinctio eligendi fugiat fugit harum
                        itaque magni, minima minus molestias mollitia nostrum quo repellendus, reprehenderit tempore,
                        unde ut! Explicabo fuga magnam quos!

                    </h1>
                </motion.div>

            </div>
        </div>
)
}