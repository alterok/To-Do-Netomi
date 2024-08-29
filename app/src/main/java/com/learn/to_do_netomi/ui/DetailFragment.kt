package com.learn.to_do_netomi.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.learn.to_do_netomi.R
import com.learn.to_do_netomi.databinding.FragmentDetailBinding
import com.learn.to_do_netomi.presentation.TaskDetailViewModel
import com.learn.to_do_netomi.ui.model.Task
import com.learn.to_do_netomi.ui.util.TaskStatusCalculator
import com.learn.to_do_netomi.ui.util.TimeInputValidatorHelper
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

private const val TAG = "DetailFragment"

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val taskDetailViewModel: TaskDetailViewModel by viewModels()
    private val timeValidator = TimeInputValidatorHelper()
    private val taskStatusCalculator = TaskStatusCalculator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.detailTaskDeadlineAmPmSpinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.time_am_pm,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun setupListeners() {
        binding.detailTaskDeadlineEdittext.addTextChangedListener(
            afterTextChanged = { s ->
                Log.i(TAG, "after textWatcher $s")
                if (s == null) {
                    return@addTextChangedListener
                }

                val text = s.toString()

                if (text.length == 3 && !text.contains(":")) {
                    s.insert(2, ":")
                } else if (text.length <= 2) {
                    s.removeSpan(":")
                } else {
                    val validation = timeValidator.isValid12HrTimeInput(text)
                    if (text.length == 5) {
                        if (validation != TimeInputValidatorHelper.ValidationResult.VALID) {
                            binding.detailTaskDeadlineInputlayout.error = validation.msg
                        } else
                            binding.detailTaskDeadlineInputlayout.error = null
                    }
                }
            }
        )

        binding.detailTaskDeadlineAmPmSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    binding.detailTaskDeadlineAmPmSpinner.setSelection(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.detailTaskDeadlineAmPmSpinner.setSelection(0)
                }
            }

        binding.detailTaskCancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.detailTaskAddBtn.setOnClickListener {
            val title = binding.detailTaskTitleEdittext.text.toString().trim()
            val description = binding.detailTaskDescriptionEdittext.text.toString()
            val deadline = timeValidator.convertTimeInputToMillis(
                binding.detailTaskDeadlineEdittext.text.toString(),
                resources.getStringArray(R.array.time_am_pm)[binding.detailTaskDeadlineAmPmSpinner.selectedItemPosition]
                    .toString()
            )

            validateAndCreateTask(title, description, deadline)?.let {
                taskDetailViewModel.addTask(it)
                findNavController().navigateUp()
            }
        }
    }

    private fun validateAndCreateTask(
        title: String,
        description: String,
        deadline: Long,
    ): Task? {
        var isValid = true

        if (title.isEmpty()) {
            binding.detailTaskTitleEdittext.error = getString(R.string.cannot_be_empty)
            isValid = false
        }

        if (deadline == -1L) {
            binding.detailTaskDeadlineEdittext.error = getString(R.string.invalid_input)
            isValid = false
        }

        return if (isValid)
            Task(
                taskId = System.currentTimeMillis(),
                title = title,
                description = description,
                deadlineTimestamp = deadline,
                createdAt = System.currentTimeMillis(),
                taskStatus = taskStatusCalculator.getTaskStatusFromDeadlineTimestamp(
                    deadline,
                    System.currentTimeMillis()
                )
            )
        else
            null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}