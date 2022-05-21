package com.example.study_room.Activities.Activities.Adapters

data class upload_file(var file : String = "", var std_id : String = "" ,
                       var lec_id : String = ""){
    fun FiletoMap(): Map<String, Any?> {
        return mapOf(
            "file" to file,
            "std_id" to std_id,
            "lec_id" to lec_id,

        )
    }

}
