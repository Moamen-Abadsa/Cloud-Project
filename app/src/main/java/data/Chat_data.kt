package data

data class Chat_data(var sender_id : String = "", var receiver_id : String = "" , var message : String = "" , var time : Double = System.currentTimeMillis().toDouble()){

    fun ChatToMap() :  Map<String, Any?>{
        return mapOf(
            "sender_id" to sender_id,
            "receiver_id" to receiver_id,
            "message" to message,
            "time" to time
        )


    }
}
