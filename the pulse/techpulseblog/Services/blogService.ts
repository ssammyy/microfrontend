// services/blogService.ts
import { Blog } from '@/Types/blog';

const BASE_URL = 'http://localhost:3300/api/blogs';

export const fetchBlogs = async (): Promise<Blog[]> => {
    const response = await fetch(BASE_URL);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    // console.log('response '+ await response.json())
    return response.json();
};
