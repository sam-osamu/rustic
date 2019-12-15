package com.osm.gradle.plugins.wrapper.builder.helpers

import com.osm.gradle.plugins.params.project.OptionsBase
import com.osm.gradle.plugins.wrapper.builder.OptionBuilder

interface BuilderHelper {
    fun put(opt: OptionsBase?, builder: OptionBuilder)
}