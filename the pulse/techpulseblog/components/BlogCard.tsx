
type BlogCardProps={
    title: string,
    excerpt: string,
    imageUrl: string
}

const BlogCard: React.FC<BlogCardProps> = ({ title, excerpt, imageUrl }) => {
    return (
        <div className="flex-none w-1/1 rounded overflow-hidden  m-4 cursor-pointer transition transform hover:-translate-y-1 motion-reduce:transition-none motion-reduce:hover:transform-none ...">
            <img className="w-full blog-image " src={imageUrl} alt="Blog cover" />
            <div className="px-6 py-4">
                <div className="font-bold text-xl mb-2">{title}</div>
                <p className="text-gray-700 text-base ">
                    {excerpt}
                </p>
            </div>
        </div>
    );
};

export default BlogCard;
