package ui.lectures.android.mvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ui.lectures.android.mvvm.viewmodel.CoursesViewModel

class EditFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.edit_fragment, container, false)
        val viewModel = ViewModelProvider(requireActivity())[CoursesViewModel::class.java]
        val courseName = requireArguments().getString("courseName").toString()
        val course = viewModel.getCourse(courseName)

        val descriptionCell = root.findViewById<EditText>(R.id.description_cell)
        descriptionCell.setText(course.description)

        val markCell = root.findViewById<EditText>(R.id.mark_cell)
        markCell.setText(course.grade)

        val termArray = resources.getStringArray(R.array.term_spinner)
        val termIndex = termArray.indexOf(course.term)
        val filterOptions = resources.getStringArray(R.array.term_spinner)
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            filterOptions
        )

        val termSpinner = root.findViewById<Spinner>(R.id.term_spinner)
        termSpinner.adapter = filterAdapter
        termSpinner.setSelection(termIndex)


        val wd = root.findViewById<Switch>(R.id.wd_switch)
        wd.apply {
            setOnClickListener {
                markCell.isEnabled = !isChecked
            }
        }
        val code = root.findViewById<TextView>(R.id.course_code)
        code.text = course.courseCode.uppercase()

        val cancelButton = root.findViewById<Button>(R.id.cancel_button)
        cancelButton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_editFragment_pop)
            }
        }


        val submitButton = root.findViewById<Button>(R.id.submit_button)
        submitButton.apply {
            setOnClickListener {
                val description = descriptionCell.text.toString()
                val grade = if (wd.isChecked) "WD" else markCell.text.toString()
                val selectedTerm = termSpinner.selectedItem.toString()

                if (grade.isNotEmpty()) {
                    viewModel.updateCourse(courseName, description, grade, selectedTerm)
                    findNavController().navigate(R.id.action_editFragment_pop)
                } else {
                    // Show an error message
                    Toast.makeText(requireContext(), "Please fill in all * fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
    }
}