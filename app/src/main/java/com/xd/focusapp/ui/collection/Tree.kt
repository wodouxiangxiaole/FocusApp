package com.xd.focusapp.ui.collection

class Tree {
    var treeName:String ?= null
    var image:Int ?= null
    var shortIntro:String ?= null

    // rank 0 ~ 3
    // 0 - legendary
    // 1 - rare
    // 2 - uncommon
    // 3 - common
    private var rank:Int = 3


    // control user lock or unlock
    var status:Boolean = false

    constructor(name:String, image:Int){
        this.image = image
        this.treeName = name
        this.rank = rank
    }

    fun unLock(){
        if(!this.status){
            this.status = true
        }
    }

    fun setRank(rank: Int){
        this.rank = rank
    }

    fun getRank(): Int{
        return this.rank
    }






}