'use client'
import React from 'react'
import {inView, motion, useAnimation} from "framer-motion";


function TopHorizontal() {
    return (
        <div className='max-w-screen-xl lg:flex lg:justify-around  mx-auto text-white pt-16 pb-16'>
            <div className="container flex-row mx-auto px-4 py-8">
                <h2 className="text-2xl text-black font-bold mb-6">Editor's pick</h2>
                <div className="flex flex-row gap-4 overflow-x-auto no-scrollbar ">
                    {/* Each card */}
                    <div className=" rounded-lg overflow-hidden">
                        <img className="w-full h-60"
                             src='https://images.pexels.com/photos/11545222/pexels-photo-11545222.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load'
                             alt="SpaceX Starship"/>
                        <div className="p-4">
                            <h3 className="text-lg  text-black">The best PS5 console deals for Cyber Monday 2023</h3>
                        </div>
                    </div>
                    <div className=" rounded-lg overflow-hidden">
                        <img className="w-full h-60"
                             src='https://images.pexels.com/photos/11545222/pexels-photo-11545222.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load'
                             alt="SpaceX Starship"/>
                        <div className="p-4">
                            <h3 className="text-lg  text-black">The best PS5 console deals for Cyber Monday 2023</h3>
                        </div>
                    </div>
                    <div className=" rounded-lg overflow-hidden">
                        <img className="w-full h-60"
                             src='https://images.pexels.com/photos/11545222/pexels-photo-11545222.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load'
                             alt="SpaceX Starship"/>
                        <div className="p-4">
                            <h3 className="text-lg  text-black">The best PS5 console deals for Cyber Monday 2023</h3>
                        </div>
                    </div>
                    <div className=" rounded-lg overflow-hidden">
                        <img className="w-full h-60"
                             src='https://images.pexels.com/photos/11545222/pexels-photo-11545222.jpeg?auto=compress&cs=tinysrgb&w=800&lazy=load'
                             alt="SpaceX Starship"/>
                        <div className="p-4">
                            <h3 className="text-lg  text-black">The best PS5 console deals for Cyber Monday 2023</h3>
                        </div>
                    </div>
                    {/* Repeat for each card */}
                </div>
            </div>
        </div>
    )
}

export default TopHorizontal
