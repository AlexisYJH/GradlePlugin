package com.example.plugin.bean

import java.io.Serializable

/**
 * @author AlexisYin
 */
class ScanBean(
    var owner: String = "",
    var name: String = "",
    var desc: String = "",
    var replaceOpcode: Int = 0,
    var replaceOwner: String = "",
    var replaceName: String = "",
    var replaceDesc: String = "",
) : Cloneable, Serializable {

    public override fun clone(): ScanBean {
        return try {
            super.clone() as ScanBean
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
            ScanBean()
        }
    }
}