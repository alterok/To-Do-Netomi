package com.learn.to_do_netomi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.learn.to_do_netomi.R
import com.learn.to_do_netomi.base.domain.TaskStatus
import com.learn.to_do_netomi.databinding.FragmentDashboardBinding
import com.learn.to_do_netomi.domain.model.TaskDomainModel
import com.learn.to_do_netomi.domain.model.TaskListPrefs
import com.learn.to_do_netomi.presentation.TaskDetailViewModel
import com.learn.to_do_netomi.presentation.TaskListViewModel
import com.learn.to_do_netomi.presentation.mapper.toUIModel
import com.learn.to_do_netomi.presentation.misc.TaskSort
import com.learn.to_do_netomi.ui.adapter.TaskItemClickListener
import com.learn.to_do_netomi.ui.adapter.TaskListAdapter
import com.learn.to_do_netomi.ui.model.Task
import com.learn.to_do_netomi.ui.util.TaskStatusCalculator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

private const val TAG = "FirstFragment"
@AndroidEntryPoint
class DashboardFragment : Fragment(), MenuProvider, TaskItemClickListener {

    private var tasks: List<Task> = emptyList()
    private var taskListAdapter: TaskListAdapter? = null
    private var currentTaskListPrefs: TaskListPrefs = TaskListPrefs.DEFAULT
    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    private val taskListViewModel: TaskListViewModel by viewModels()
    private val taskDetailViewModel: TaskDetailViewModel by viewModels()

    private val taskStatusCalculator = TaskStatusCalculator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupViews() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.dashboardTaskListRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = TaskListAdapter(this@DashboardFragment).apply {
                taskListAdapter = this
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                taskListViewModel.sortedAllTasksFlow.collect {
                    tasks = it
                    taskListAdapter?.submitList(it)

                    if (it.isEmpty()) {
                        showEmptyUI()
                    } else {
                        hideEmptyUI()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                taskListViewModel.taskListPrefsFlow.collect {
                    val oldPrefs = currentTaskListPrefs
                    onTaskListPrefChanged(oldPrefs, it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(1000)
                    checkDeadlinesAndUpdateStatus()
                }
            }
        }
    }

    private fun hideEmptyUI() {
        binding.dashboardTaskListRecyclerview.isVisible = true
        binding.dashboardEmptyViewContainer.isGone = true
    }

    private fun showEmptyUI() {
        binding.dashboardTaskListRecyclerview.isInvisible = true
        binding.dashboardEmptyViewContainer.isVisible = true
    }

    private suspend fun checkDeadlinesAndUpdateStatus() {
        withContext(Dispatchers.IO) {
            tasks.forEachIndexed { index, it ->
                if (it.taskStatus != TaskStatus.COMPLETED) {
                    taskStatusCalculator.getTaskStatusFromDeadlineTimestamp(
                        it.deadlineTimestamp,
                        System.currentTimeMillis()
                    ).apply {
                        if (it.taskStatus != this) {
                            val updated = it.copy(taskStatus = this)
                            onTaskUpdated(it, updated)
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.dashboardAddTaskFab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.dashboardEmptyViewDummyTaskBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                TaskDomainModel.createDummyTasks(3).forEach {
                    taskDetailViewModel.addTask(it.toUIModel())
                }
            }
        }
    }

    private fun onTaskListPrefChanged(oldPrefs: TaskListPrefs, newPrefs: TaskListPrefs) {
        currentTaskListPrefs = newPrefs

        updateSortOrderUI(newPrefs.taskListSort)
    }

    private fun updateSortOrderUI(taskSort: TaskSort) {
        binding.dashboardTaskListOrderLabel.text = when (taskSort) {
            TaskSort.DEFAULT -> "Sorted by recently created"
            TaskSort.ASCENDING -> "Sorted by deadline (ASC)"
            TaskSort.DESCENDING -> "Sorted by deadline (DESC)"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTaskUpdated(oldTask: Task, newTask: Task) {
        taskDetailViewModel.addTask(newTask)
    }

    override fun onRemoveClicked(task: Task) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.warning))
            .setMessage("Do you want to delete \"${task.title}\", this action can't be undone.")
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                taskDetailViewModel.removeTask(task)
            }
            .show()
    }

    override fun onTaskClicked(task: Task) {

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.action_sort).apply {
            icon = if (currentTaskListPrefs.taskListSort == TaskSort.DEFAULT) {
                isChecked = false
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.baseline_align_horizontal_left_24
                )
            } else {
                isChecked = true
                ContextCompat.getDrawable(requireContext(), R.drawable.baseline_dehaze_24)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_sort -> {
                val isChecked = menuItem.isChecked
                menuItem.isChecked = !isChecked
                menuItem.icon = if (isChecked) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_align_horizontal_left_24
                    )
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.baseline_dehaze_24)
                }
                onFilterToggle(menuItem.isChecked)
                true
            }

            else -> false
        }
    }

    private fun onFilterToggle(enable: Boolean) {
        val taskSort = if (enable) TaskSort.ASCENDING else TaskSort.DEFAULT
        taskListViewModel.onSortChanged(taskSort)
    }
}