package com.test.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author AlexisYin
 */
class TestPlugin : Plugin<Project> {

    //打印方法耗时
    override fun apply(project: Project) {
        println("-----------TestPlugin-----------");
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                TimeCostTransform::class.java,
                InstrumentationScope.PROJECT) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }

    }

}