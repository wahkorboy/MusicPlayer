package com.wahkor.player.model

class TrackFile {
    var title:String?=null
    var singer:String?=null
    var thumbnail:Int?=null

    constructor()
    constructor(title:String,singer:String,thumbnail:Int){
        this.title=title
        this.singer=singer
        this.thumbnail=thumbnail
    }

}