import React from "react";
import Image from "next/image";
import Link from "next/link";

export default function Page(){
    return(
        <div className="relative h-screen w-screen">
            <Image
                src='/notFnd.jpeg'
                alt='email icon'
                layout='fill'
                objectFit='cover'
                className='absolute inset-0 z-0 blur-sm'
            />
            <div className='absolute inset-0 bg-black opacity-50'></div>
            <div className='relative z-10 text-center flex flex-col items-center justify-center text-white h-screen text-5xl'>
                <h1 className='mb-6'><code className='text-orange-600'>Ooops</code>, we hit a snug and it's on us. </h1>
                <code className='text-2xl p-10 gap-14'>We're currently improving our website to serve you better. During this transition, this page is under maintenance. We appreciate your patience during this process and our engineers are working to restore. </code>
                <Link href="/" className='text-4xl border-2 border-solid border-gray-100 p-2 animate-pulse rounded-xl'> &lt;home/&gt; </Link>

            </div>
        </div>
    );
}