package com.test.plugin


import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.Method


/**
 * @author AlexisYin
 */
class TimeCostClassVisitor(nextVisitor: ClassVisitor): ClassVisitor(Opcodes.ASM9,nextVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val newMethodVisitor = object : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor){
            var start = 0;

            override fun onMethodEnter() {
                if (isNeedVisitMethod(name)) {
                    //long start = System.currentTimeMillis();
                    invokeStatic(
                        Type.getType("Ljava/lang/System;"),
                        Method("currentTimeMillis", "()J")
                    )
                    //索引
                    start = newLocal(Type.LONG_TYPE)
                    storeLocal(start)
                }
                super.onMethodEnter()
            }

            override fun onMethodExit(opcode: Int) {
                if (isNeedVisitMethod(name)) {
                    println("onMethodExit: $name")
                    //long end = System.currentTimeMillis();
                    //System.out.println("execute:" + (end-start)+" ms.");
                    invokeStatic(
                        Type.getType("Ljava/lang/System;"),
                        Method("currentTimeMillis", "()J")
                    )
                    //索引
                    val end = newLocal(Type.LONG_TYPE)
                    //用一个本地变量接收上一步的结果
                    storeLocal(end)
                    getStatic(
                        Type.getType("Ljava/lang/System;"),
                        "out",
                        Type.getType("Ljava/io/PrintStream;")
                    )
                    newInstance(Type.getType("Ljava/lang/StringBuilder;"))
                    dup()
                    invokeConstructor(
                        Type.getType("Ljava/lang/StringBuilder;"),
                        Method("<init>", "()V")
                    )
                    visitLdcInsn("$name execute:")
                    invokeVirtual(
                        Type.getType("Ljava/lang/StringBuilder;"),
                        Method("append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;")
                    )
                    loadLocal(end)
                    loadLocal(start)
                    math(SUB, Type.LONG_TYPE)
                    invokeVirtual(
                        Type.getType("Ljava/lang/StringBuilder;"),
                        Method("append", "(J)Ljava/lang/StringBuilder;")
                    )
                    visitLdcInsn(" ms.")
                    invokeVirtual(
                        Type.getType("Ljava/lang/StringBuilder;"),
                        Method("append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;")
                    )
                    invokeVirtual(
                        Type.getType("Ljava/lang/StringBuilder;"),
                        Method("toString", "()Ljava/lang/String;")
                    )
                    invokeVirtual(
                        Type.getType("Ljava/io/PrintStream;"),
                        Method("println", "(Ljava/lang/String;)V")
                    )
                }
                super.onMethodExit(opcode)
            }
        }
        return newMethodVisitor
    }

    private fun isNeedVisitMethod(name: String?): Boolean {
        return name != "<clinit>"
                && name != "<init>"
    }
}