package com.osm.gradle.plugins

import javafx.concurrent.Task
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class RusticPluginTest {
    @Rule
    @JvmField
    val testProjectDir = TemporaryFolder()
    lateinit var buildFile: File

    @Before
    fun setUp() {
        testProjectDir.create()

        val srcJarPath = Paths.get("build/libs/rustic-0.2.6.jar")
        val dstJarPath = Paths.get(testProjectDir.root.toPath().toString(), srcJarPath.fileName?.toString())
        Files.copy(srcJarPath, dstJarPath)

        buildFile = testProjectDir.newFile("build.gradle")
        buildFile.writeText(
            """
            buildscript {
                dependencies {
                    classpath files('$dstJarPath')
                }
            }

            apply plugin: 'com.osm.gradle.plugins.rustic'
            
            """.trimIndent()
        )

        val manifest = testProjectDir.newFile("Cargo.toml")
        manifest.writeText(
            """
            [package]
            name = "rustic_test"
            version = "0.1.0"
            
            [lib]
            name = "rustic_test"
            path = "src/lib.rs"
            """.trimIndent()
        )

        testProjectDir.newFolder("src")
        testProjectDir.newFile("src/lib.rs").writeText(
            """
            pub fn test() {
                println!("test")
            }
            """.trimIndent()
        )
    }

    @After
    fun tearDown() {
    }

    private fun run(script: String): List<String> {
        buildFile.appendText(
            """
            rustic {
                projectSettings {
                    projectLocation = "${testProjectDir.root}"
                }
                
                $script
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .build()

        println(result.tasks.joinToString(", ") { it.toString() })

        return result.output.lines()
    }

    @Test
    fun apply001() {
        val ret = run("")
        println(ret.joinToString("\n"))
    }

    @Test
    fun apply002() {
        val ret = run(
            """
            defaultConfig.defaultOptions {
                target "defaultConfig"
            }
            
            buildTypes {
                niku.defaultOptions {
                    target "nikuBuild"
                }
            }
            
            flavors {
                test001.defaultOptions {
                    target "test001"
                }
                
                test002 {
                    
                }
                
                test003 {
                    
                }
                
                test004 {
                    
                }
                
                test005 {
                    environments = [
                            AR: "/usr/bin/x86_64-w64-mingw32-gcc-ar",
                            CC: "/usr/bin/x86_64-w64-mingw32-gcc"
                    ]
                    defaultOptions.cargoConfig {
                        targetTriple {
                            linker environments["CC"]
                        }
                    }
                }
            }
            
            variants.all {
                //println("variant : " + it.name + ", target : " + it.defaultOptions.target)
            }
            
            tasks.all {
                //println("task : " + it.name)
            }
        """.trimIndent()
        )

        println(ret.joinToString("\n"))
    }

//    @Test
//    fun applyDynamic() {
//        val project = ProjectBuilder.builder().build()
//        project.apply {
//            it.plugin("com.osm.gradle.plugins.rustic")
//        }
//    }
}
