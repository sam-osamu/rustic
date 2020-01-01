package com.osm.gradle.plugins.wrapper.builder.helpers.cargo

import com.osm.gradle.plugins.types.interfaces.IConfigBase
import com.osm.gradle.plugins.wrapper.builder.OptionBuilder
import com.osm.gradle.plugins.wrapper.builder.helpers.BuilderHelper
import com.osm.gradle.plugins.wrapper.builder.options.cargo.TargetSelection
import com.osm.gradle.plugins.wrapper.builder.options.cargo.TestOptions

class TestOptionsHelper : BuilderHelper {
    override fun put(opt: IConfigBase?, builder: OptionBuilder) {
        opt?.testOptions?.apply {
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
            doc?.also {
                if (it) {
                    builder.put(TargetSelection.Doc())
                } else {
                    builder.removeIfMatchType(TargetSelection.Doc::class.java)
                }
            }
        }
    }
}