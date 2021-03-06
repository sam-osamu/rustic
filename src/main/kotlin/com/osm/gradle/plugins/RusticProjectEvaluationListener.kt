package com.osm.gradle.plugins

import com.osm.gradle.plugins.task.RusticTask
import com.osm.gradle.plugins.task.TaskGenerator
import com.osm.gradle.plugins.types.variants.BuildVariant
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState

class RusticProjectEvaluationListener(private val rustic: Rustic) : ProjectEvaluationListener {
    private val variantsInternal = ArrayList<BuildVariant>()

    override fun beforeEvaluate(project: Project) {
        if (project.name != rustic.project.name) {
            return
        }

        rustic.variants.clear()
        RusticTask.disableAll(project.tasks)
    }

    override fun afterEvaluate(project: Project, state: ProjectState) {
        if (project.name != rustic.project.name || state.failure != null) {
            return
        }

        evaluateLogLevel(project)
        generateVariants(project)
        generateTasks(project)

        rustic.variants.addAll(variantsInternal)
    }

    private fun evaluateLogLevel(project: Project) {
        project.logging.captureStandardOutput(rustic.projectSettings.captureStandardOutput)
        project.logging.captureStandardError(rustic.projectSettings.captureStandardError)
    }

    private fun generateVariants(project: Project) {
        rustic.apply {
            variantsInternal.addAll(buildTypes.map { BuildVariant(project, projectSettings, defaultConfig, it, null) })
            if (flavors.isNotEmpty()) {
                buildTypes.flatMap { buildType ->
                    flavors.map { flavor ->
                        variantsInternal.add(BuildVariant(project, projectSettings, defaultConfig, buildType, flavor))
                    }
                }
            }
        }
    }

    private fun generateTasks(project: Project) {
        rustic.apply {
            val taskGenerator = TaskGenerator(project, projectSettings, defaultConfig)
            taskGenerator.createCleanTasks()
            taskGenerator.createVariantTasks(variantsInternal)
        }
    }
}