'use client'
import {useInView} from "react-intersection-observer";

type BlogPost = {
    id: number;
    title: string;
    excerpt: string;
    imageUrl: string;
};

type LatestBlogsProps = {
    blogs: BlogPost[];
};
import React, {useEffect, useRef, useState} from 'react';
import BlogCard from './BlogCard';
import {inView, motion, useAnimation} from "framer-motion";

const Latest: React.FC<LatestBlogsProps> = ({ blogs }) => {
    const fadeInVariants = {
        hidden: { opacity: 0 },
        visible: { opacity: 1 }
    };


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
        <div>
            <div className='max-w-screen-xl lg:flex  mx-auto text-black pt-16 pb-5'>
                <h1 className='text-3xl'>Discover more curated content</h1>

            </div>
            <div className=' flex  justify-center items-center '>


                <div  className="w-screen flex flex-wrap  max-w-screen-xl justify-between mx-auto">

                    {blogs.map(blog => (
                        <motion.div
                            ref={ref}
                            key={blog.id}
                            variants={fadeInVariants}
                            initial="hidden"
                            whileInView="visible"
                            transition={{duration:.5}}


                            viewport={{ once: true, amount: 0.5 }}
                        >

                            <BlogCard key={blog.id} title={blog.title} excerpt={blog.excerpt} imageUrl={blog.imageUrl} />
                            <button className="read-more-button px-4 mb-16 text-white bg-amber-600 ml-2.5 rounded">
                                read more
                                <svg className="arrow-icon w-3.5 h-3.5 ml-2" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 10">
                                    <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M1 5h12m0 0L9 1m4 4L9 9"/>
                                </svg>
                            </button>
                        </motion.div>

                    ))}
                </div>

            </div>

        </div>


    );
};

export default Latest;
