/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.ports.provided.storage


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.required.storage.IStorageService
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.StorageException
import org.kebs.app.kotlin.apollo.common.exceptions.StorageFileNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.storage.StorageProperties
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream


//@Service
class FileSystemStorageService(
        storageProperties: StorageProperties
) : IStorageService {
    private var rootLocation: Path? = null

    init {
        storageProperties.uploadDirectory
                ?.let {
                    rootLocation = Paths.get(it)
                }

    }
    //    final var uploadDirectory = System.getProperty("user.dir") + "/uploads"


    //    val rootLocation: Path = Paths.get(storageProperties.uploadDirectory)
    override fun init() {
        try {
            rootLocation
                    ?.let { location ->
                        KotlinLogging.logger { }.info("would have created directory here at $location")
//                        Files.createDirectories(location)

                    }
                    ?: throw NullValueNotAllowedException("Not a valid path")

        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }

    }

    override fun store(file: MultipartFile) {
        file.originalFilename
                ?.let { fileName ->
                    val filename: String = StringUtils.cleanPath(fileName)
                    try {
                        when {
                            file.isEmpty -> {
                                throw StorageException("Failed to store empty file $filename")
                            }
                            filename.contains("..") -> {
                                // This is a security check
                                throw StorageException(
                                        "Cannot store file with relative path outside current directory "
                                                + filename
                                )
                            }
                            else -> file.inputStream.use { inputStream ->

                                rootLocation
                                        ?.let { location ->
                                            Files.copy(inputStream, location.resolve(filename), StandardCopyOption.REPLACE_EXISTING)
                                        }
                                        ?: throw NullValueNotAllowedException("Not a valid path")

                            }
                        }
                    } catch (e: IOException) {
                        throw StorageException("Failed to store file $filename", e)
                    }

                }
                ?: throw StorageException("Invalid File Name")

    }

    override fun loadAll(): Stream<Path>? {
        return try {
            Files.walk(rootLocation, 1)
                    .filter { path: Path -> path != rootLocation }
                    .map { other: Path? ->
                        other?.let { o -> rootLocation?.relativize(o) } ?: throw StorageException("Invalid File Name")
                    }
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }
    }

    override fun load(filename: String?): Path? {
        return filename
                ?.let { rootLocation?.resolve(it) }
                ?: throw StorageException("Invalid File Name")
    }

    override fun loadAsResource(filename: String?): Resource? {
        return try {
            val file = load(filename)
            file
                    ?.let {
                        val resource: Resource = UrlResource(it.toUri())
                        if (resource.exists() || resource.isReadable) {
                            resource
                        } else {
                            throw StorageFileNotFoundException(
                                    "Could not read file: $filename"
                            )
                        }

                    }
                    ?: throw StorageException("Invalid File Name")

        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $filename", e)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation?.toFile())
    }
}