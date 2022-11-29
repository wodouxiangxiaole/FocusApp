package com.xd.focusapp.ui.collection

class Tree {
    var treeName:String ?= null
    var image:Int ?= null
    var shortIntro:String ?= null
    var id:Int ?= null

    var toPoints:Boolean = false

    // rank 0 ~ 3
    // 0 - legendary
    // 1 - rare
    // 2 - uncommon
    // 3 - common
    private var rank:Int = 3

    // control user lock or unlock
    var status: Boolean

    constructor(name:String, image:Int, id:Int){
        this.image = image
        this.treeName = name
        this.rank = 3
        this.status = false
        this.id = id
    }

    fun unLock(): Boolean{
        return if(this.status){
            this.toPoints = true
            false
        } else{
            this.status = true
            true
        }
    }

    fun setRank(rank: Int){
        this.rank = rank
    }

    fun getRank(): Int{
        return this.rank
    }






}