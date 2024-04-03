import React, {useEffect, useState} from "react";
import styles from '../styles/MainBody.module.css'
import {motion, useAnimation} from "framer-motion";

import {useInView} from "react-intersection-observer";


const MainBody = () => {
    const squareVariants = {
        visible: { opacity: 1, scale: 1, transition: { duration: 1 } },
        hidden: { opacity: 0, scale: 0 }
    };
    const textVariants = {
        visible: { opacity: 1, scale: .5, transition: { duration: 3 } },
        hidden: { opacity: 0, scale: 0 }
    };
    const [navColor, setNavColor] = useState('#007bff'); // Initial primary color

    const changePrimaryColor = () => {
        const newColor = '#ff5722'; // New primary color
        setNavColor(newColor);

        // Update the CSS variable value dynamically
        document.documentElement.style.setProperty('--primary-color', newColor);
    };
    const controls = useAnimation();
    const controls2 = useAnimation();
    const [ref, inView] = useInView();
    useEffect(() => {
        if (inView) {
            controls.start("visible");
        }
    }, [controls, inView]);
    const [ref2, inView2] = useInView();
    useEffect(() => {
        if (inView2) {
            controls2.start("visible");
        }
    }, [controls2, inView2]);

    return (
        <div>


            <div className={styles.wrapper}>
                <header className={styles.header}>
                    <img src="/images/city.jpg" alt="" className={styles.background}/>
                    <img src="/images/s a a m.svg" alt="" className={styles.foreground}/>
                    {/*<video autoPlay loop muted className={styles.foreground}>*/}
                    {/*    <source src='/images/sam.mp4' type="video/mp4" />*/}
                    {/*    Your browser does not support the video tag.*/}
                    {/*</video>*/}


                </header>
                <motion.h1
                    ref={ref}
                    animate={controls}
                    initial="hidden"
                    variants={squareVariants}
                    // className="square"
                >
                <section
                    className={styles.section}>



                        {/*<div className={styles.leftSection}>*/}
                        {/*    <h1> <strong> We are : </strong> </h1>*/}
                        {/*    <br/>*/}

                        {/*    <br/>*/}
                        {/*    /!*<button onClick={changePrimaryColor}>Change Primary Color</button>*!/*/}

                        {/*</div>*/}
                        <div  className={styles.rightSection}>
                            <h1> + </h1>
                            From Earth
                            <br/>

                            <br/>



                            {/*<button onClick={changePrimaryColor}>Change Primary Color</button>*/}

                        </div>
                    <motion.h1
                        ref={ref2}
                        animate={controls2}
                        initial="hidden"
                        variants={textVariants}
                        // className="square"
                    >
                    <div  className={styles.leftSection}>
                            <h1> + </h1>
                            From Earth
                            <br/>

                            <br/>



                            {/*<button onClick={changePrimaryColor}>Change Primary Color</button>*/}

                        </div>
                    </motion.h1>


                </section>
                </motion.h1>


                <section className={styles.blackSection}>
                    <div className={styles.blackOverlay}></div>
                    <p>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium autem, dolorum
                        ducimus eaque ex explicabo ipsum laudantium pariatur perspiciatis rem sit
                        unde?
                    </p>

                    <br/>  <p>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium autem, dolorum
                        ducimus eaque ex explicabo ipsum laudantium pariatur perspiciatis rem sit
                        unde?
                    </p>

                    <br/>  <p>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium autem, dolorum
                        ducimus eaque ex explicabo ipsum laudantium pariatur perspiciatis rem sit
                        unde?
                    </p>

                    <br/>  <p>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium autem, dolorum
                        ducimus eaque ex explicabo ipsum laudantium pariatur perspiciatis rem sit
                        unde?
                    </p>

                    <br/>  <p>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium autem, dolorum
                        ducimus eaque ex explicabo ipsum laudantium pariatur perspiciatis rem sit
                        unde?
                    </p>

                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad culpa minima quo sapiente sed! Adipisci alias aliquid animi deleniti enim error fuga, itaque minus necessitatibus possimus sed totam, vel veritatis!</p>
                </section>

            </div>

        </div>

    )
}
export default MainBody;