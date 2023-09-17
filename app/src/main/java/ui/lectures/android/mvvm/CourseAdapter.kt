package ui.lectures.android.mvvm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ui.lectures.android.mvvm.viewmodel.CoursesViewModel

class CourseAdapter(private val navController: NavController, private val viewModel: CoursesViewModel) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private var courses: List<Course> = emptyList()
    private var filteredCourses: List<Course> = emptyList()

    fun setData(courses: List<Course>) {
        this.courses = courses
        this.filteredCourses = courses
        notifyDataSetChanged()
        Log.d("CoursesViewModel", "Data is set to ${this.courses}")
    }

    fun filterList(filteredCourses: List<Course>) {
        this.filteredCourses = filteredCourses
        notifyDataSetChanged()
        Log.d("CoursesViewModel", "Filter Data is set to ${this.filteredCourses}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_card, parent, false)
        return CourseViewHolder(view, navController)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = filteredCourses[position]
        holder.bind(course, navController)
    }

    override fun getItemCount(): Int {
        return filteredCourses.size
    }

    fun getColor(context: Context, grade: String): Int {
        return when {
            grade == "WD" -> ContextCompat.getColor(context, R.color.wd_color)
            grade.toIntOrNull() != null -> when {
                grade.toInt() < 50 -> ContextCompat.getColor(context, R.color.fail_color)
                grade.toInt() in 50..59 -> ContextCompat.getColor(context, R.color.pass_color)
                grade.toInt() in 60..90 -> ContextCompat.getColor(context, R.color.good_color)
                grade.toInt() in 91..95 -> ContextCompat.getColor(context, R.color.excellent_color)
                else -> ContextCompat.getColor(context, R.color.outstanding_color)
            }
            else -> ContextCompat.getColor(context, R.color.outstanding_color)
        }
    }


    inner class CourseViewHolder(itemView: View, private val navController: NavController) : RecyclerView.ViewHolder(itemView) {
        fun bind(course: Course, navController: NavController) {
            itemView.findViewById<TextView>(R.id.course_code).text = course.courseCode.uppercase()
            itemView.findViewById<TextView>(R.id.description).text = course.description
            itemView.findViewById<TextView>(R.id.term).text = course.term
            itemView.findViewById<TextView>(R.id.grade).text = course.grade
            itemView.setBackgroundColor(getColor(itemView.context, course.grade))

            itemView.findViewById<ImageView>(R.id.edit).setOnClickListener {
                navController.navigate(R.id.action_homeActivity_to_editFragment,
                    Bundle().apply {
                        putString("courseName", course.courseCode)
                })
            }

            itemView.findViewById<ImageView>(R.id.delete).setOnClickListener {
                viewModel.deleteCourse(course)
            }
        }
    }
}