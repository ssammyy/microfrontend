package com.api.tech.pulse.techpulseapi.Controllers

import com.api.tech.pulse.techpulseapi.DataTransfer.BlogsDtos
import com.api.tech.pulse.techpulseapi.Services.BlogService
import com.api.tech.pulse.techpulseapi.Store.Entities.Blogs
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blogs")
class BlogsCrud(
        private val service: BlogService
) {
    @GetMapping
    fun getAllBlogs(): List<BlogsDtos.BlogDto> = service.getAllBlogs().map{service.convertToDTO(it)}


    @GetMapping("/{id}")
    fun getBlogById(@PathVariable id: Long): Blogs = service.getBlogsById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBlog(@RequestBody blog: Blogs): Blogs = service.createBlog(blog)

    @PutMapping("/{id}")
    fun updateBlog(@PathVariable id: Long, @RequestBody blog: Blogs): Blogs = service.updateBlog(id, blog)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBlog(@PathVariable id: Long) {
        service.deleteBlog(id)
    }





}