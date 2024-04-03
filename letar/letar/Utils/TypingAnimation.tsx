import React, { useEffect, useState } from 'react';

interface TypingEffectProps {
    text: string;
    className?: string;
    delay?: number;
    styles?: {};
}

const TypingEffect: React.FC<TypingEffectProps> = ({text, className, delay, styles}) => {
    const [content, setContent] = useState('');
    const typingDelay = delay;
    useEffect(() => {
        let index = 0;
        const timeoutId = setInterval(() => {
            setContent((c) => c + text.charAt(index));
            index++;
            if (index === text.length) {
                clearInterval(timeoutId);
            }
        }, typingDelay);

        return () => clearInterval(timeoutId);
    }, [text]);

    return <h1 className={className} style={styles}>{content}</h1>;
};
export default TypingEffect;