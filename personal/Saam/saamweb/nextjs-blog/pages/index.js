import Head from 'next/head'
import styles from '../styles/Home.module.css';
import Link from 'next/link';
import Navbar from "../Components/Navbar";
import MainBody from "../Components/MainBody"


export default function Home() {
  return (

      <div>
          <Navbar></Navbar>
          <MainBody></MainBody>
      </div>


  )

}