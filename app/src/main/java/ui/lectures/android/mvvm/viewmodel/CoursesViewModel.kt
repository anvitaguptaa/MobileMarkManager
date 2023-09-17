package ui.lectures.android.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ui.lectures.android.mvvm.Course
import ui.lectures.android.mvvm.model.CoursesModel

/**
 * AccountViewModel facilitates data exchange between the [Model][ui.lectures.android.mvvm.model.CoursesModel] and the View.
 */
class CoursesViewModel : ViewModel() {

    private val coursesModel = CoursesModel()
    private val _courses = MutableLiveData<List<Course>>()
    private val _filteredCourseList = MutableLiveData<List<Course>>(emptyList())

    val filteredCourseList: LiveData<List<Course>>
        get() = _filteredCourseList

    val courses : LiveData<List<Course>>
        get() = _courses


    init {
        coursesModel.courseList.observeForever { courses ->
            _courses.postValue(courses)
        }
    }

    fun initFilteredCourseListObserver() {
        coursesModel.filteredCourseList.observeForever { filteredCourses ->
            _filteredCourseList.postValue(filteredCourses)
        }
    }

    fun addCourse(courseCode: String, description: String, term: String, grade: String) {
        coursesModel.addCourse(Course(courseCode, description, term, grade))
    }


    fun updateCourse(code: String, description: String, mark: String, term: String) {
        coursesModel.updateCourse(code, description, mark, term)
    }

    fun deleteCourse(course: Course) {
        coursesModel.deleteCourse(course)
    }

    fun getCourse(courseName: String): Course {
        return coursesModel.getCourse(courseName)
    }

    fun sortCourses(selectedSortingOption: String) {
        coursesModel.sortCourses(selectedSortingOption)
    }

    fun setFilterOption(filterOption: String) {
        coursesModel.setFilterOption(filterOption)
    }

    fun updateFilteredCourseList() {
        coursesModel.updateFilteredCourseList()
    }


}