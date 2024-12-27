package com.abdullah.lostfound.ui.dataclasses

class Lost{
    var isFound: Boolean=true
    var contact: String=""
    var address: String=""
    var id:String=""
    var title:String=""
    var description:String=""
    var image:String=""
    var category: String = ""
    var status: String = "" // Use "LOST" or "FOUND"
    var date: Long = 0L
    var imageUrl: String = ""
    var postedBy: String = ""
    var name: String = ""
    var email: String = ""
    var userId: String=""
    var userName: String=""
    var item: Lost? = null
    var postDate: String=""
    var isLost: Boolean=false
    var isLostorFound: Boolean=false // False for Lost, true for Found



}