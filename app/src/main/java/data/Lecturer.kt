package data

data class Lecturer(
    var first : String = "" , var middle : String = "",
    var last : String= "",var birth_date : String = "",
    var address : String = "", var email : String = "",var mobile_no : String = "${(0..9999999).random()}"
    ,var password : String = "" ,var active : Boolean = false
){
    fun LecturertoMap(): Map<String, Any?> {
        return mapOf(
            "first" to first,
            "middle" to middle,
            "last" to last,
            "birth_date" to birth_date,
            "address" to address,
            "email" to email,
            "mobile_no" to mobile_no,
            "password" to password,
            "active" to active

        )
    }
}
