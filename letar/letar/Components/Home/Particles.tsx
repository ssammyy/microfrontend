// components/ParticleBackground.js
import Particles from "react-tsparticles";
import { loadFull } from "tsparticles";
import {ISourceOptions} from "tsparticles-engine";

const particlesOptions: ISourceOptions = {
    fullScreen: {
        enable: true,
        zIndex: -1 // Use a negative z-index to place the particles behind other content
    },
    particles: {
        number: {
            value: 80,
            density: {
                enable: true,
                value_area: 800
            }
        },
        color: {
            value: "#ffffff"
        },
        shape: {
            type: "circle"
        },
        opacity: {
            value: 0.5,
            random: false,
            anim: {
                enable: false
            }
        },
        size: {
            value: 3,
            random: true,
            anim: {
                enable: false
            }
        },
        line_linked: {
            enable: true,
            distance: 150,
            color: "#ffffff",
            opacity: 0.4,
            width: 1
        },
        move: {
            enable: true,
            speed: 2,
            direction: "none",
            random: false,
            straight: false,
            out_mode: "out",
            attract: {
                enable: false
            }
        }
    },
    interactivity: {
        detect_on: "canvas",
        events: {
            onhover: {
                enable: true,
                mode: "repulse"
            },
            onclick: {
                enable: true,
                mode: "push"
            },
            resize: true
        },
        modes: {
            grab: {
                distance: 400,
                line_linked: {
                    opacity: 1
                }
            },
            bubble: {
                distance: 400,
                size: 40,
                duration: 2,
                opacity: 8
            },
            repulse: {
                distance: 200
            },
            push: {
                particles_nb: 4
            },
            remove: {
                particles_nb: 2
            }
        }
    },
    retina_detect: true
};

const ParticleBackground = () => {
        const particlesInit = async (main: any) => {
            await loadFull(main);
        };


    return (
        <Particles
            id="tsparticles"
            init={particlesInit}
            options={particlesOptions as ISourceOptions }
        />
    );
};

export default ParticleBackground;
