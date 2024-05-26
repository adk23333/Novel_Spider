package sanliy.spider.novel

import sanliy.spider.novel.net.sfacg.model.SysTag


fun tripleSwitch(input: Int): Int {
    return when (input) {
        0 -> 1
        1 -> -1
        -1 -> 0
        else -> throw IllegalArgumentException("Invalid input")
    }
}

fun List<SysTag>.toIdListString(): String {
    var idList = ""
    this.forEach {
        if (idList != "") idList += ","
        idList += it.sysTagId.toString()
    }
    return idList
}

