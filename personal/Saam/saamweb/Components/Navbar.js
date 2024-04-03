import React, { useState, useEffect } from 'react';
import Image from 'next/image'

const Navbar = () => {
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [showNavbarLogo, setShowNavbarLogo] = useState(false);

    const toggleMobileMenu = () => {
        setIsMobileMenuOpen(!isMobileMenuOpen);
    };

    useEffect(() => {
        const handleScroll = () => {
            // Calculate the scroll position
            const scrollY = window.scrollY*10 || window.pageYOffset;
            console.log('scroll Y '+ scrollY + ' and inner height is '+ window.innerHeight /1000)

            // Set the visibility of the navbar logo based on scroll position
            setShowNavbarLogo(scrollY >= window.innerHeight / 1000);
        };

        // Add a scroll event listener
        window.addEventListener('scroll', handleScroll);

        // Clean up the event listener when the component unmounts
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    return (
        <div className='navMain'>
            <di className={`navbar ${isMobileMenuOpen ? 'mobile-menu-open' : ''}`}>
                {/*<div >*/}
                {/*    <Image*/}
                {/*        src='/images/saamWhite.svg'*/}
                {/*        width={300}*/}
                {/*        */}
                {/*        height={300}*/}
                {/*        alt="logo"*/}
                {/*        style={{color: "white"}}*/}
                {/*    />*/}
                {/*</div>*/}

                <img className='navLogo' src="/images/saamWhite.svg" alt="logo"/>
                <ul className={`navbar-links ${isMobileMenuOpen ? 'mobile-open' : ''}`}>
                    <li><a href="#"><i className="fas fa-home"></i> Home</a></li>
                    <li><a href="#"><i className="fas fa-user"></i> About</a></li>
                    <li><a href="#"><i className="fas fa-cogs"></i> Services</a></li>
                    <li><a href="#"><i className="fas fa-envelope"></i> Contact</a></li>
                </ul>
            </di>
        </div>
    );
};

export default Navbar;
