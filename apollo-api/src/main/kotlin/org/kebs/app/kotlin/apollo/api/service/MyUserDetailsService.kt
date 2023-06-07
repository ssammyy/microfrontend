//package org.kebs.app.kotlin.apollo.api.service
//
//import org.kebs.app.kotlin.apollo.store.model.UsersEntity
//import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
//import org.springframework.security.core.userdetails.UserDetails
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//
//import org.springframework.security.core.GrantedAuthority
//
//import org.springframework.security.core.userdetails.UsernameNotFoundException
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.security.core.userdetails.User
//
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.stereotype.Service
//
//
//@Service
//class MyUserDetailsService : UserDetailsService {
//    @Autowired
//    private val userRepository: IUserRepository? = null
//
//    @Throws(UsernameNotFoundException::class)
//    override fun loadUserByUsername(username: String): UserDetails {
//        val user: UsersEntity? = userRepository?.findByUserName(username)
//        val authority: GrantedAuthority = SimpleGrantedAuthority(user.getRole())
//        return User(
//            user.username,
//            user.password, Arrays.asList(authority)
//        )
//    }
//}