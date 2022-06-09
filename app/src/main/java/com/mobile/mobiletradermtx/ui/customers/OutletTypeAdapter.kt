package com.mobile.mobiletradermtx.ui.customers

import java.util.ArrayList

class OutletTypeAdapter {

    var nodes: ArrayList<Caches> = ArrayList()

    fun add(id: Int, name: String) {
        nodes.add(Caches(id, name))
    }

    fun clear() {
        nodes.clear()
    }

    fun size(): Int {
        return nodes.size
    }

    fun getIndexById(id: Int): Int {
        var index = 0
        for (nd in nodes) {
            if (nd.getId() == id) {
                break
            }
            index++
        }
        return index
    }

    fun getValueId(value: String): Int {
        var id = 0
        for (nd in nodes) {
            if (nd.getName() == value) {
                id = nd.getId()
                break
            }
        }
        return id
    }

    class Caches constructor(internal var id: Int, internal var name: String) {

        fun getId(): Int {
            return id
        }

        fun setId(id: Int) {
            this.id = id
        }

        fun getName(): String {
            return name
        }

        fun setName(name: String) {
            this.name = name
        }
    }

}