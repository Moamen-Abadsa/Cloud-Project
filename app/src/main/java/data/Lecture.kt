package data

data class Lecture(
  var name : String = "" , var description : String = ""
, var assignment : String = "" , var video : String = ""
  ,var Course_id : String = "",
  var Lecture_id : String = "${(0..99999999).random()}",
  var student_view: ArrayList<String>,var isHide : Boolean = false)
{

    fun LecturetoMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "desc" to description,
            "assignment" to assignment,
            "video" to video,
            "course_id" to Course_id,
            "lecture_id" to Lecture_id,
            "student_view" to student_view,
            "isHide" to isHide
        )
    }

}


