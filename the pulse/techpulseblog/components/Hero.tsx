'use client'
import React, {useEffect, useState} from 'react';

import {motion, useAnimation} from 'framer-motion';
import {useInView} from 'react-intersection-observer';
import {fetchBlogs} from "@/Services/blogService";
import {Blog} from "@/Types/blog";
import Link from 'next/link'

const Hero = () => {
    const [blogs, setBlogs] = useState<Blog[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);


    useEffect(() => {
        const loadBlogs = async () => {
            try {
                setLoading(true);
                const data = await fetchBlogs();
                setBlogs(data);
                setLoading(false);
            } catch (errors) {
                setLoading(false);
                setError('we hit a snug, it\'s on us, please try again later ')
                throw errors;

            }
        };

        loadBlogs();
    }, []);

    const variants = {
        hidden: {opacity: 0, y: 40},
        visible: {opacity: 1, y: 0}
    };
    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;


    return (

        <div className="max-w-screen-xl justify-between mx-auto ">
            <div className="flex flex-wrap">
                <div className="w-full lg:w-1/2 bg-gray-100 p-8">
                    <motion.h1
                        initial={{opacity: 0}}
                        animate={{opacity: 1}}
                        transition={{duration: 4}}
                        className="text-7xl top-0 lg:text-9xl font-bold sticky lg:top-10 bg-gray-100 mt-10"
                    >
                            Dive Deep into Tech
                    </motion.h1>

                    <motion.h1
                        initial={{opacity: 0, y: -100}}
                        animate={{opacity: 1, y: 0}}
                        transition={{duration: 0.7, delay:0.4}}
                        className='text-3xl font-light mt-10 lg:text-6xl'
                    >
                            A Journey <br/>
                            into the Ever-Evolving Realm of Technology

                    </motion.h1>


                </div>

                {/* Right section with cards of latest blogs */}

                <div className="cursor-pointer w-full lg:w-1/2 p-8 ">
                    {blogs.map(blog=>(

                        <div key={blog.id}>
                            <Link href={"/"}>
                                <motion.div
                                    initial="hidden"
                                    animate="visible"
                                    variants={variants}
                                    transition={{duration: 0.5}}
                                >
                                    <div className="rounded overflow-hidden mb-4 flex flex-col sm:flex-row">
                                        <img
                                            className="h-80 max-w-xl sm:w-full  hover:scale-105 transition transform duration-500"
                                            src={blog.imageUrl}
                                            alt="Blog 1"
                                            style={{cursor: 'pointer'}}
                                        />
                                        <div className="px-6 py-4">
                                            <div className="font-bold text-xl mb-2">{blog.title}</div>
                                            <p className="text-gray-700 text-base sm:text-left">
                                                {blog.content}
                                            </p>
                                            <div className="mt-4 sm:mt-8 flex justify-between items-center w-full">
                                                <p className="text-gray-500 text-sm">{blog.publishedDate}</p>
                                                <button
                                                    className="bg-amber-500 hover:bg-blue-700 text-white font-light w-full sm:w-auto px-4 rounded">
                                                    More
                                                    <svg className="w-3.5 h-3.5 ml-2" aria-hidden="true"
                                                         xmlns="http://www.w3.org/2000/svg" fill="none"
                                                         viewBox="0 0 14 10">
                                                        <path stroke="currentColor" stroke-linecap="round"
                                                              stroke-linejoin="round"
                                                              stroke-width="2" d="M1 5h12m0 0L9 1m4 4L9 9"/>
                                                    </svg>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </motion.div>
                            </Link>

                        </div>

                    ))}
                    {/* Card 1 */}


                    {/* Additional cards */}
                    {/* Add more cards as needed */}
                </div>
            </div>
        </div>
    );
};

export default Hero;