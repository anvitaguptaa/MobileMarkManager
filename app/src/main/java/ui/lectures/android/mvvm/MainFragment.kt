package ui.lectures.android.mvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import ui.lectures.android.mvvm.viewmodel.CoursesViewModel

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val viewModel = ViewModelProvider(requireActivity())[CoursesViewModel::class.java]
        val root = inflater.inflate(R.layout.main_fragment, container, false)
        val navController = findNavController()
        val recyclerView: RecyclerView = root.findViewById(R.id.course_list)
        val courseAdapter = CourseAdapter(navController,viewModel)
        recyclerView.adapter = courseAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)


        val filterOptions = resources.getStringArray(R.array.filter_spinner)
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            filterOptions
        )
        root.findViewById<Spinner>(R.id.filter_spinner).apply {
            adapter = filterAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val filterOption = parent?.getItemAtPosition(position).toString()
                    viewModel.setFilterOption(filterOption)
                    viewModel.updateFilteredCourseList()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }


        val sortingOptions = resources.getStringArray(R.array.sorting_spinner)
        val sortingAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            sortingOptions
        )
        root.findViewById<Spinner>(R.id.sorting_spinner).apply {
            adapter = sortingAdapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedSortingOption = parent?.getItemAtPosition(position) as? String
                    if (selectedSortingOption != null) {
                        viewModel.sortCourses(selectedSortingOption)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        root.findViewById<Button>(R.id.add_button).apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_homeActivity_to_addActivity)
            }
        }


        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            courseAdapter.setData(courses)
            courseAdapter.notifyDataSetChanged()
        }

        viewModel.initFilteredCourseListObserver()
        viewModel.filteredCourseList.observe(viewLifecycleOwner) { filteredCourses ->
            courseAdapter.filterList(filteredCourses)
            courseAdapter.notifyDataSetChanged()
        }


        return root
    }
}
