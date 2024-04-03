import React, {useState} from 'react';
import {AiOutlineClose, AiOutlineMenu} from 'react-icons/ai';
import Link from "next/link";

const Navbar = () => {
    const [nav, setNav] = useState(false);
    const handleNav = () => setNav(!nav);

    return (
        <nav className='flex items-center justify-between h-24 max-w-[1240px] mx-auto px-4 text-white'
             style={{zIndex: 10}}>
            <div className="flex items-center">
                <h1 className='text-3xl tracking-widest font-bold bg-[radial-gradient(ellipse_at_right,_var(--tw-gradient-stops))] from-white to-amber-600 bg-clip-text text-transparent'>letar.</h1>
            </div>

            <div onClick={handleNav}>

                {nav ? ""
                    : <AiOutlineMenu size={20}/>}
            </div>

            <div
                className={`absolute inset-0 flex justify-center items-center p-6 transition-transform duration-300 ease-in-out ${nav ? "translate-x-0" : "-translate-x-full"} bg-gradient-radial from-neutral-600 to-neutral-900 dark:bg-gray-900 md:bg-transparent md:dark:bg-transparent md:flex md:items-start md:mt-10 md:space-x-6 md:p-0`}
                style={{zIndex: 10}}>
                <ul className={`flex flex-col md:flex-row items-center space-x-0 md:space-x-10`}>
                    <li className="md:order-last md:ml-20">
                        {nav && <AiOutlineClose size={20} className="cursor-pointer" onClick={handleNav}/>}
                    </li>
                    <li className="hover-link"><Link href="/">Home</Link></li>
                    <li className="hover-link"><Link href="/404">Company</Link></li>
                    <li className="hover-link"><Link href="/404">Resources</Link></li>
                    <li className="hover-link"><Link href="/About">About</Link></li>
                    <li className="hover-link"><Link href="/404">Contact</Link></li>

                </ul>
            </div>
        </nav>
    );
};

export default Navbar;