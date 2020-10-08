package com.example.otherhalf.model

data class ChatMessage(var id : String = "", var message : String = "", var fromId: String = "", var toId : String = "", var timestamp : Long = -1){
    override fun toString(): String {
        return "ChatMessage(id=$id, message=$message, fromId=$fromId, toId=$toId, timeStamp=$timestamp)"
    }
}