package com.osm.gradle.plugins.wrapper.builder.helpers

import com.osm.gradle.plugins.params.project.OptionsBase
import com.osm.gradle.plugins.wrapper.builder.OptionBuilder
import com.osm.gradle.plugins.wrapper.builder.options.TestOptions

class BenchOptionsHelper : BuilderHelper {
    override fun put(opt: OptionsBase?, builder: OptionBuilder) {
        opt?.benchOptions?.apply {
            noRun?.also {
                if (it) {
                    builder.put(TestOptions.NoRun())
                } else {
                    builder.removeIfMatchType(TestOptions.NoRun::class.java)
                }
            }
            noFailFast?.also {
                if (it) {
                    builder.put(TestOptions.NoFailFast())
                } else {
                    builder.removeIfMatchType(TestOptions.NoFailFast::class.java)
                }
            }
        }
    }
}