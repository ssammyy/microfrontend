import { useState, useEffect, RefObject } from 'react';

const useOnScreen = (ref: RefObject<HTMLElement>) => {
    const [isInView, setIsInView] = useState(false);
    const observer = new IntersectionObserver(
        ([entry]) => setIsInView(entry.isIntersecting)
    );

    useEffect(() => {
        if(ref.current) {
            observer.observe(ref.current);
        }

        return () => observer.disconnect();
    }, [ref]);

    return isInView;
};

export default useOnScreen;