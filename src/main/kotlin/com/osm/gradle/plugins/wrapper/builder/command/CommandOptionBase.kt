package com.osm.gradle.plugins.wrapper.builder.command

/**
 * This class represents one option and its parameters.
 */
abstract class CommandOptionBase(protected val param: Iterable<String?>?) : ICommandOption {
    /**
     * Turns an option and its parameters into a list.
     */
    override fun toList(): List<String> {
        val param = param?.filter { !it.isNullOrBlank() }?.map { it!! }
        return if (!param.isNullOrEmpty()) {
            listOfNotNull(option).plus(param)
        } else {
            if (hasValue) {
                emptyList<String>()
            } else {
                listOfNotNull(option)
            }
        }
    }
}