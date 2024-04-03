import React from 'react';
import { motion } from 'framer-motion';

const Hero = () => {
    return (

        <div className="max-w-screen-xl justify-between mx-auto ">
            <div className="flex flex-wrap">
                {/* Left section with the sticky big text */}


                <div className="w-full lg:w-1/2 bg-gray-100 p-8">
                    <h1 className="text-7xl top-0 lg:text-9xl font-bold sticky lg:top-10 bg-gray-100 mt-10">
                        Dive Deep into Tech
                    </h1>

                    <h1 className='text-3xl font-light mt-10 lg:text-6xl'>
                        A Journey <br/>
                        into the Ever-Evolving Realm of Technology

                    </h1>

                </div>

                {/* Right section with cards of latest blogs */}
                <div className="w-full lg:w-1/2 p-8 ">
                    {/* Card 1 */}
                    <div className="rounded overflow-hidden mb-4 flex flex-col sm:flex-row">
                        <img
                            className="h-80 w-full sm:w-80 hover:scale-105 transition transform duration-500"
                            src="https://images.pexels.com/photos/2007647/pexels-photo-2007647.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt="Blog 1"
                            style={{ cursor: 'pointer' }}
                        />
                        <div className="px-6 py-4">
                            <div className="font-bold text-xl mb-2">The future is AR?</div>
                            <p className="text-gray-700 text-base sm:text-left">
                                A brief description of the latest blog post.
                            </p>
                            <div className="mt-4 sm:mt-8 flex justify-between items-center w-full">
                                <p className="text-gray-500 text-sm">October 25, 2023</p>
                                <button className="bg-amber-500 hover:bg-blue-700 text-white font-light w-full sm:w-auto px-4 rounded">
                                   More
                                    <svg className="w-3.5 h-3.5 ml-2" aria-hidden="true"
                                         xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 10">
                                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                              stroke-width="2" d="M1 5h12m0 0L9 1m4 4L9 9"/>
                                    </svg>
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Card 2 */}
                    <div className="rounded overflow-hidden mb-4 flex flex-col sm:flex-row">
                        <img
                            className="h-80 w-full sm:w-80 hover:scale-105 transition transform duration-500"
                            src="https://images.pexels.com/photos/2708981/pexels-photo-2708981.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt="Blog 2"
                            style={{ cursor: 'pointer' }}
                        />
                        <div className="px-6 py-4">
                            <div className="font-bold text-xl mb-2">This week on Retro!</div>
                            <p className="text-gray-700 text-base sm:text-left">
                                Dive back to the early tech
                            </p>
                            <div className="mt-4 sm:mt-8 flex justify-between items-center w-full">
                                <p className="text-gray-500 text-sm">Date Published: October 25, 2023</p>
                                <button className="bg-amber-500 hover:bg-blue-700 text-white font-light w-full sm:w-auto px-4 rounded">
                                    Read More
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className="rounded overflow-hidden mb-4 flex flex-col sm:flex-row">
                        <img
                            className="h-80 w-full sm:w-80 hover:scale-105 transition transform duration-500"
                            src="https://images.pexels.com/photos/4226122/pexels-photo-4226122.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt="Blog 2"
                            style={{ cursor: 'pointer' }}
                        />
                        <div className="px-6 py-4">
                            <div className="font-bold text-xl mb-2">Letar's new technology</div>
                            <p className="text-gray-700 text-base sm:text-left">
                                Letar, a booming tech start up.
                            </p>
                            <div className="mt-4 sm:mt-8 flex justify-between items-center w-full">
                                <p className="text-gray-500 text-sm">Date Published: October 25, 2023</p>
                                <button className="bg-amber-500 hover:bg-blue-700 text-white font-light w-full sm:w-auto px-4 rounded">
                                    Read More
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className="rounded overflow-hidden mb-4 flex flex-col sm:flex-row">
                        <img
                            className="h-80 w-full sm:w-80 hover:scale-105 transition transform duration-500"
                            src="https://images.pexels.com/photos/8386363/pexels-photo-8386363.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
                            alt="Blog 2"
                            style={{ cursor: 'pointer' }}
                        />
                        <div className="px-6 py-4">
                            <div className="font-bold text-xl mb-2">Ai and Tech</div>
                            <p className="text-gray-700 text-base sm:text-left">
                                why Ai wont solve most of your problems.
                            </p>
                            <div className="mt-4 sm:mt-8 flex justify-between items-center w-full">
                                <p className="text-gray-500 text-sm">Date Published: October 25, 2023</p>
                                <button className="bg-amber-500 hover:bg-blue-700 text-white font-light w-full sm:w-auto px-4 rounded">
                                    Read
                                </button>
                            </div>
                        </div>
                    </div>
                    {/* Additional cards */}
                    {/* Add more cards as needed */}
                </div>
            </div>
        </div>
    );
};

export default Hero;