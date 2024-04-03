package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.Blogs
import org.springframework.data.jpa.repository.JpaRepository

interface BlogsRepository: JpaRepository<Blogs, Long> {


}