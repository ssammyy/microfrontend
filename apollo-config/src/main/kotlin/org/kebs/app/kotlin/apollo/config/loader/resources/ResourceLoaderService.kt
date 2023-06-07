package org.kebs.app.kotlin.apollo.config.loader.resources

import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class ResourceLoaderService(
    private var resourceLoader: ResourceLoader,
) : ResourceLoaderAware {


    fun getResource(path: String): Resource {
        return this.resourceLoader.getResource(path)
    }


    /**
     * Set the ResourceLoader that this object runs in.
     *
     * This might be a ResourcePatternResolver, which can be checked
     * through `instanceof ResourcePatternResolver`. See also the
     * `ResourcePatternUtils.getResourcePatternResolver` method.
     *
     * Invoked after population of normal bean properties but before an init callback
     * like InitializingBean's `afterPropertiesSet` or a custom init-method.
     * Invoked before ApplicationContextAware's `setApplicationContext`.
     *
     * @param resourceLoader ResourceLoader object to be used by this object
     *
     *
     *
     */
    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        this.resourceLoader = resourceLoader
    }
}