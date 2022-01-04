package com.survivalcoding.todolist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.survivalcoding.todolist.R
import com.survivalcoding.todolist.databinding.ActivityMainBinding
import com.survivalcoding.todolist.databinding.FragmentMainBinding
import com.survivalcoding.todolist.ui.main.adapter.TodoListAdapter


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoListAdapter = TodoListAdapter(
            onItemClicked = { item ->
                mainViewModel.toggleIsDone(item)
            },
            onLongClicked = { item ->
                mainViewModel.todoNeedChanged.value = item
                mainViewModel.isUpdate.value = true
                parentFragmentManager.commit {
                    replace<EditFragment>(R.id.fragment_container_view)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        )

        // enroll listAdapter
        binding.recyclerview.adapter = todoListAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.todos.observe(this, { todos ->
            todoListAdapter.submitList(todos)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
