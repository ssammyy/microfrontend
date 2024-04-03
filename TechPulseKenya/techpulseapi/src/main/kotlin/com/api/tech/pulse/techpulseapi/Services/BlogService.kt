package com.api.tech.pulse.techpulseapi.Services

import com.api.tech.pulse.techpulseapi.DataTransfer.BlogsDtos
import com.api.tech.pulse.techpulseapi.Store.Entities.Blogs
import com.api.tech.pulse.techpulseapi.Store.Repositories.BlogsRepository
import org.springframework.stereotype.Service

@Service
class BlogService(
        private val blogsRepository: BlogsRepository
) {
    fun getAllBlogs(): List<Blogs> =
            blogsRepository.findAll()


    fun getBlogsById(id: Long): Blogs = blogsRepository.findById(id).orElseThrow()


    fun createBlog(blog: Blogs): Blogs = blogsRepository.save(blog)


    fun updateBlog(id:Long, updatedBlog:Blogs): Blogs{
        return blogsRepository.findById(id).map{blog ->
            blog.title = updatedBlog.title
            blog.content = updatedBlog.content
            blog.author = updatedBlog.author
            blog.category = updatedBlog.category
            blog.imageUrl = updatedBlog.imageUrl
            blog.additionalImage = updatedBlog.additionalImage
            blog.publishedDate = updatedBlog.publishedDate
            blogsRepository.save(blog)

        }.orElseThrow()
    }


    fun deleteBlog(id: Long){
        blogsRepository.deleteById(id)
    }



    fun convertToDTO(blog: Blogs): BlogsDtos.BlogDto {
        return BlogsDtos.BlogDto(
                id = blog.id,
                title = blog.title,
                content = blog.content,
                author = blog.author,
                category = blog.category,
                imageUrl = blog.imageUrl,
                additionalImage = blog.additionalImage,
                publishedDate = blog.publishedDate
        )

    }

}