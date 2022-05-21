package data

data class Student(var first : String , var middle : String ,
                   var last : String,var birth_date : String ,
                   var address : String , var email : String ,var mobile_no : String
                   ,var password : String,var active : Boolean, var selected_Courses : ArrayList<String>? = null){
    fun StudenttoMap(): Map<String, Any?> {
        return mapOf(
            "first" to first,
            "middle" to middle,
            "last" to last,
            "birth_date" to birth_date,
            "address" to address,
            "email" to email,
            "mobile_no" to mobile_no,
            "password" to password,
            "active" to active,
            "selected_courses" to selected_Courses

        )
    }
}
