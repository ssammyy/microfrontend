import React, { useState, useCallback, useRef, useEffect } from 'react'
import { useTransition, animated } from '@react-spring/web'
import { Parallax, ParallaxLayer, IParallax } from '@react-spring/parallax'
import { motion } from "framer-motion"
import Image from 'next/image'
import logo from 'public/images/saam (1200 × 600 px).png'
import { useInView } from 'react-intersection-observer';
import Description from "@/components/Desc";




// Little helpers ...
const url = (name: string, wrap = false) =>
    `${wrap ? 'url(' : ''}https://awv3node-homepage.surge.sh/build/assets/${name}.svg${wrap ? ')' : ''}`

export default function App() {
  const parallax = useRef<IParallax>(null!)

    const ref = useRef<ReturnType<typeof setTimeout>[]>([])
    const [items, set] = useState<string[]>([])
    const transitions = useTransition(items, {
        from: {
            opacity: 0,
            height: 0,
            innerHeight: 0,
            transform: 'perspective(600px) rotateX(0deg)',
            color: '#8fa5b6',
        },
        enter: [
            { opacity: 1, height: 80, innerHeight: 80 },
            { transform: 'perspective(600px) rotateX(180deg)', color: '#28d79f' },
            { transform: 'perspective(600px) rotateX(0deg)' },
        ],
        leave: [{ color: '#c23369' }, { innerHeight: 0 }, { opacity: 0, height: 0 }],
        update: { color: '#28b4d7' },
    })

    const reset = useCallback(() => {
        ref.current.forEach(clearTimeout)
        ref.current = []
        set([])
        ref.current.push(setTimeout(() => set(['I n n o v a t e', 'A u t o m a t e', 'E l e v a t e']), 2000))
        ref.current.push(setTimeout(() => set(['Grow', 'your', 'business']), 8000))
        ref.current.push(setTimeout(() => set(['A u t o m a t e', 'E l e v a t e']), 5000))

    }, [])

    useEffect(() => {
        reset();
        const intervalId = setInterval(reset, 20000);

        return () => clearInterval(intervalId);
    }, [reset]);

    useEffect(() => {
        reset()
        return () => ref.current.forEach(clearTimeout)
    }, [])


    const [containerRef, inView] = useInView({
        triggerOnce: true,
    });

  return (
      <div>
          <section>
              <div>
                  <div style={{ width: '100%', height: '100%', position: 'absolute'}}>
                      <Parallax ref={parallax} pages={2}>
                          <ParallaxLayer
                              offset={0}
                              speed={-.9}
                              // onClick={() => parallax.current.scrollTo(1)}
                              style={{
                                  zIndex: 14,
                                  display: 'flex',
                                  maxHeight: '10rem',
                                  // alignItems: 'center',
                                  justifyContent: 'center',
                              }}>
                              <div className="nav">

                                  <motion.div
                                      initial={{ x: -500 }} // Start off-screen to the left
                                      animate={{ x: 0 }} // Animate to its original position
                                      transition={{ duration: 1 }} // Adjust animation duration as needed
                                  >
                                      <Image
                                          src={logo}
                                          width={150}
                                          height={500}
                                          alt="logo"
                                          className="logo"
                                      />
                                  </motion.div>



                                  <ul className="nav-items">
                                      <li className="nav-link">home</li>
                                      <li className="nav-link">about</li>
                                      <li className="nav-link">youtube</li>
                                      <li className="nav-link">hire us</li>
                                  </ul>
                                  <li>

                                  </li>
                              </div>

                          </ParallaxLayer>


                          <ParallaxLayer  className="first-page"  offset={1} speed={1} style={{ backgroundImage: 'linear-gradient(to bottom, #000000, #131313, #1f1f1f, #2b2b2b, #373838)', opacity:'80%', zIndex:16}} >

                              <ParallaxLayer
                                  // offset={0}
                                  speed={-.5}
                                  onClick={() => parallax.current.scrollTo(1)}
                              >
                                  <div className='container'>

                                      <div className='main'>
                                          {transitions(({ innerHeight, ...rest }, item) => (
                                              <animated.div className='transitionsItem' style={rest} onClick={reset}>
                                                  <animated.div style={{ overflow: 'hidden', height: innerHeight }}>{item}</animated.div>
                                              </animated.div>
                                          ))}
                                      </div>
                                  </div>

                              </ParallaxLayer>




                              <h1 className="description"
                              >
                                  Here, we delve into the world of possibilities that technology offers.
                                  Our goal is to empower your business with cutting-edge solutions.
                                  From streamlined processes to enhanced customer experiences,
                                  we'll show you how technology can be your greatest ally on the path to success.
                                  Join us as we explore the potential that awaits when you embrace innovation.
                              </h1>

                              <div ref={containerRef} className='cards-container'>
                                  <motion.div
                                      initial={{ x: -500 }}
                                      animate={{ x: 0 }}
                                      transition={{ duration: 8}}
                                      className="cards"
                                  >
                                      hey there


                                  </motion.div>
                                  <div className="cards">
                                      hey there
                                  </div> <div className="cards">
                                  hey there
                              </div>

                              </div>




                          </ParallaxLayer>
                          <ParallaxLayer offset={2} speed={1} style={{ backgroundColor: '#87BCDE' }} />
                          {/*<ParallaxLayer offset={2} speed={1} style={{ backgroundColor: '#87BCDE' }} />*/}

                          <ParallaxLayer
                              offset={0}
                              speed={-.4}
                              factor={3}
                              style={{
                                  backgroundImage: url('stars', true),
                                  // backgroundImage: 'url("https://images.pexels.com/photos/1694000/pexels-photo-1694000.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")',
                                  backgroundSize: 'cover',
                              }}
                          />



                          <ParallaxLayer
                              offset={0}
                              speed={-.5}
                              onClick={() => parallax.current.scrollTo(1)}
                              style={{
                                  zIndex: -2,
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'center',
                              }}>

                              <motion.div
                                  initial={{ opacity:0 }}
                                  animate={{ opacity:1 }}
                                  transition={{ duration: 0.9, delay: 0.2 }}

                                  className="heading"
                              >
                                  We Help Small businesses grow.
                              </motion.div>

                          </ParallaxLayer>
                          <ParallaxLayer
                              offset={0}
                              speed={-.3}
                              // onClick={() => parallax.current.scrollTo(1)}
                              style={{
                                  zIndex: 11,
                                  paddingTop: '30rem',
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'center',
                              }}>
                              <motion.div
                                  initial={{ opacity:0 }}
                                  animate={{ opacity:1 }}
                                  transition={{ duration: 2, delay: 0.4 }}

                                  className="nav-link main-btn"
                              >
                                  Contact us.
                              </motion.div>

                          </ParallaxLayer>

                          <ParallaxLayer
                              offset={-1}
                              speed={0.8}
                              onClick={() => parallax.current.scrollTo(2)}
                              style={{
                                  display: 'flex',
                                  alignItems: 'center',
                                  justifyContent: 'center',
                              }}>
                              <div className="heading">
                                  <h1>Empower with business solutions</h1>
                              </div>
                          </ParallaxLayer>
                      </Parallax>



                  </div>
              </div>

          </section>

      </div>

  )
}
