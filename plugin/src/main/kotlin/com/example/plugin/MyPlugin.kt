package com.example.plugin

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.example.plugin.asm.ScanClassVisitorFactory
import com.example.plugin.bean.ScanBean
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.Opcodes

class MyPlugin : Plugin<Project> {

    //替换Build.MODEL
    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                ScanClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                //配置忽略路径
                it.ignoreOwner.set("com/example/lib/BuildUtils")
                //配置目标信息和替换信息
                it.listOfScans.set(
                    listOf(
                        ScanBean(
                            "android/os/Build",
                            "MODEL",
                            "Ljava/lang/String;",
                            Opcodes.INVOKESTATIC,
                            "com/example/lib/BuildUtils",
                            "getModel",
                            "()Ljava/lang/String;"
                        )
                    )
                )
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }

}