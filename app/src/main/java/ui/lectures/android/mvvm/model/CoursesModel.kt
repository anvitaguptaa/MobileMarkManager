package ui.lectures.android.mvvm.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ui.lectures.android.mvvm.Course

/**
 * AccountModel models a bank account.
 */

class CoursesModel {
    private val _courseList = MutableLiveData<List<Course>>(emptyList())
    private val termOrder = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23", "S23", "F23")
    private val _selectedFilterOption = MutableLiveData<String>()
    private val _filteredCourseList = MutableLiveData<List<Course>>(emptyList())

    // Observe changes to the selected filter option and update the filtered course list accordingly
    val courseList: LiveData<List<Course>>
        get() = _courseList

   val filteredCourseList: LiveData<List<Course>>
        get() = _filteredCourseList

    init {
        _filteredCourseList.value = _courseList.value
    }


    fun addCourse(course: Course) {
        val currentList = _courseList.value?.toMutableList() ?: mutableListOf()
        currentList.add(course)
        _courseList.postValue(currentList)
        updateFilteredCourseList()
    }

    fun updateCourse(code: String, description: String, mark: String, term: String) {
        courseList.value?.find { it.courseCode == code }?.apply {
            this.description = description
            this.grade = mark
            this.term = term
        }
        updateFilteredCourseList()
    }

    fun getCourse(courseName: String): Course {
        for (course in courseList.value!!) {
            if (course.courseCode == courseName) {
                return course
            }
        }
        throw NoSuchElementException("Course $courseName not found.")
    }

    fun deleteCourse(course: Course) {
        val currentList = _courseList.value?.toMutableList() ?: mutableListOf()
        currentList.remove(course)
        _courseList.postValue(currentList)
        updateFilteredCourseList()
    }

    fun sortCourses(selectedSortingOption: String) {
        val unsortedList = _courseList.value ?: emptyList()
        val sortedList = when (selectedSortingOption) {
            "By Course Code" -> unsortedList.sortedBy { it.courseCode }
            "By Term" -> unsortedList.sortedBy { termOrder.indexOf(it.term) }
            "By Mark" -> unsortedList.sortedWith(compareByDescending<Course> { it.grade.toDoubleOrNull() ?: Double.MIN_VALUE }
                .thenBy { it.grade == "WD" })
            else -> unsortedList
        }
        _courseList.postValue(sortedList)
        updateFilteredCourseList()
    }


    fun setFilterOption(filterOption: String) {
        _selectedFilterOption.value = filterOption
        updateFilteredCourseList()
    }

    fun updateFilteredCourseList() {
        val unfilteredList = _courseList.value ?: emptyList()
        val selectedFilterOption = _selectedFilterOption.value

        _filteredCourseList.value = when (selectedFilterOption) {
            "All Courses" -> unfilteredList
            "CS Only" -> unfilteredList.filter { it.courseCode.startsWith("cs") }
            "Math Only" -> unfilteredList.filter {
                it.courseCode.startsWith("math") || it.courseCode.startsWith("stat") || it.courseCode.startsWith("co")
            }
            "Other Only" -> unfilteredList.filter {
                !(it.courseCode.startsWith("cs") || it.courseCode.startsWith("math") || it.courseCode.startsWith("stat") || it.courseCode.startsWith("co"))
            }
            else -> unfilteredList
        }
    }
}