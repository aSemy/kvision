package io.kvision.gradle

import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.property


/**
 * Configuration for the KVision plugin.
 *
 * The default values are set in [KVisionPlugin.createKVisionExtension].
 */
abstract class KVisionExtension @Inject constructor(
    private val objects: ObjectFactory,
    private val providers: ProviderFactory,
) {

    val enableGradleTasks: Property<Boolean> =
        kvisionSystemProperty("enableGradleTasks")

    val enableWebpackVersions: Property<Boolean> =
        kvisionSystemProperty("enableWebpackVersions")

    val enableHiddenKotlinJsStore: Property<Boolean> =
        kvisionSystemProperty("enableHiddenKotlinJsStore")

    val enableSecureResolutions: Property<Boolean> =
        kvisionSystemProperty("enableSecureResolutions")

    val enableBackendTasks: Property<Boolean> =
        kvisionSystemProperty("enableBackendTasks")

    val enableWorkerTasks: Property<Boolean> =
        kvisionSystemProperty("enableWorkerTasks")

    private fun kvisionSystemProperty(
        property: String,
        default: Boolean = true,
    ): Property<Boolean> {
        val convention = providers.systemProperty("io.kvision.plugin.$property")
            .map { it.toBoolean() }
            .orElse(default)
        return objects.property<Boolean>().convention(convention)
    }

    abstract val kotlinJsStoreDirectory: DirectoryProperty

    /**
     * Set the Node binary used to execute KVision tasks. The default uses the Node binary
     * installed the Kotlin JS/MPP Gradle plugin.
     */
    abstract val nodeBinaryPath: Property<String>

    @get:Nested
    abstract val versions: Versions

    fun versions(configure: Action<Versions>): Unit = configure.execute(versions)

    abstract class Versions @Inject constructor(
        objects: ObjectFactory,
    ) {
        /** Requires [KVisionExtension.enableWebpackVersions] to be true */
        val webpackDevServer: Property<String> = objects.property<String>().convention("4.9.0")

        /** Requires [KVisionExtension.enableWebpackVersions] to be true */
        val webpack: Property<String> = objects.property<String>().convention("5.72.0")

        /** Requires [KVisionExtension.enableWebpackVersions] to be true */
        val webpackCli: Property<String> = objects.property<String>().convention("4.9.2")

        /** Requires [KVisionExtension.enableWebpackVersions] to be true */
        val karma: Property<String> = objects.property<String>().convention("6.3.19")

        /** Requires [KVisionExtension.enableWebpackVersions] to be true */
        val mocha: Property<String> = objects.property<String>().convention("10.0.0")

        /** `async` version. Requires [KVisionExtension.enableSecureResolutions] to be true */
        val async: Property<String> = objects.property<String>().convention("^2.6.4")
    }

}
