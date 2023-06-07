package org.kebs.app.kotlin.apollo.config.adaptor.akka.interfaces


import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
annotation class Actor 