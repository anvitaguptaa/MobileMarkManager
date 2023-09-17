package ui.lectures.android.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ui.lectures.android.mvvm.viewmodel.CoursesViewModel

class AddFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.add_fragment, container, false)
        val viewModel = ViewModelProvider(requireActivity())[CoursesViewModel::class.java]

        val filterOptions = resources.getStringArray(R.array.term_spinner)
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            filterOptions
        )
        val termSpinner = root.findViewById<Spinner>(R.id.term_spinner)
        termSpinner.adapter = filterAdapter

        val cancelButton = root.findViewById<Button>(R.id.cancel_button)
        cancelButton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_addFragment_pop)
            }
        }

        val courseCode = root.findViewById<EditText>(R.id.course_cell)
        val description = root.findViewById<EditText>(R.id.description_cell)
        val mark = root.findViewById<EditText>(R.id.mark_cell)

        val wd = root.findViewById<Switch>(R.id.wd_switch)
        wd.apply {
            setOnClickListener {
                mark.isEnabled = !isChecked
            }
        }


        val createButton = root.findViewById<Button>(R.id.create_button)
        createButton.apply {
            setOnClickListener {
                val code = courseCode.text.toString()
                val desc = description.text.toString()
                val grade = if (wd.isChecked) {
                    "WD"
                } else {
                    mark.text.toString()
                }
                val term = termSpinner.selectedItem.toString()

                if (code.isNotEmpty() && grade.isNotEmpty()) {
                    viewModel.addCourse(code, desc, term, grade)
                    findNavController().navigateUp()
                } else {
                    // Show an error message
                    Toast.makeText(requireContext(), "Please fill in all * fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
    }
}