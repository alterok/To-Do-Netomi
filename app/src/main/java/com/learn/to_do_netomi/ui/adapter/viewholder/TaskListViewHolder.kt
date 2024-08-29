package com.learn.to_do_netomi.ui.adapter.viewholder

import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.learn.to_do_netomi.R
import com.learn.to_do_netomi.base.domain.TaskStatus
import com.learn.to_do_netomi.databinding.ItemviewTaskListBinding
import com.learn.to_do_netomi.ui.adapter.TaskItemClickListener
import com.learn.to_do_netomi.ui.model.Task
import com.learn.to_do_netomi.ui.util.TaskStatusCalculator
import com.learn.to_do_netomi.ui.util.TimeInputValidatorHelper


private const val TAG = "TaskListViewHolder"

class TaskListViewHolder(
    private val binding: ItemviewTaskListBinding,
    private val taskItemClickListener: TaskItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private val taskStatusCalculator = TaskStatusCalculator()
    private val timeInputValidatorHelper = TimeInputValidatorHelper()
    private var data: Task? = null

    init {
        setupListeners()
    }

    private fun setupListeners() {
        binding.itemviewTaskListIscompletedCheckbox.addOnCheckedStateChangedListener { materialCheckBox, i ->
            data?.let {
                val isChecked = i == 1

                val newData = it.copy(
                    taskStatus = if (isChecked) TaskStatus.COMPLETED
                    else taskStatusCalculator.getTaskStatusFromDeadlineTimestamp(
                        it.deadlineTimestamp, System.currentTimeMillis()
                    )
                )

                taskItemClickListener.onTaskUpdated(it, newData)
                data = newData
                bindData(newData)
            }
        }

        binding.itemviewTaskListRemoveBtn.setOnClickListener {
            data?.let { it1 -> taskItemClickListener.onRemoveClicked(it1) }
        }

        binding.root.setOnClickListener {
            data?.let { it1 -> taskItemClickListener.onTaskClicked(it1) }
        }
    }

    fun bindData(task: Task) {
        data = task
        binding.apply {
            itemviewTaskListStatus.isInvisible = task.taskStatus != TaskStatus.PENDING

            itemviewTaskListIscompletedCheckbox.isChecked = task.taskStatus == TaskStatus.COMPLETED
            itemviewTaskListTitle.text = task.title
            itemviewTaskListStatus.text = task.taskStatus.name.lowercase()

            if (task.taskStatus == TaskStatus.PENDING) {
                itemviewTaskListTitle.setTextColor(
                    ContextCompat.getColor(
                        root.context, R.color.alert_red
                    )
                )
            } else {
                itemviewTaskListTitle.setTextColor(
                    MaterialColors.getColor(root, com.google.android.material.R.attr.colorPrimary)
                )
            }

            if(task.taskStatus == TaskStatus.COMPLETED){
                itemviewTaskListTitle.paintFlags =
                    itemviewTaskListTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                itemviewTaskListTitle.paintFlags =
                    itemviewTaskListTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            }

            itemviewTaskListDescription.text = task.description
            itemviewTaskListDescription.isVisible = task.description.isNotBlank()

            itemviewTaskListDeadline.text =
                timeInputValidatorHelper.convertMillisToTodayTimeString(task.deadlineTimestamp)
        }
    }
}