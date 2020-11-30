package com.brins.networklib.model.event

/**
 * Created by lipeilin
 * on 2020/11/28
 */
class EventsResult {

    var code = 0

    var lasttime: Long = 0

    var size = 0

    var more = false

    var events: ArrayList<EventData>? = null


}