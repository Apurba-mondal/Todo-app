package com.example.present_todo.fragments

import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.present_todo.databinding.FragmentAddTodoBinding
import com.example.present_todo.utils.TodoData


class AddTodoFragment : DialogFragment() {
    private lateinit var binding : FragmentAddTodoBinding
    private lateinit var listener : DilogNextBtnClickListener
    private var todoData : TodoData? = null

    fun setListener (listener : DilogNextBtnClickListener){
        this.listener = listener

    }

    companion object
    {
        const val Tag = "AddTodoFragment"

        @JvmStatic
        fun newInstance(taskId : String, task : String) = AddTodoFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null)
        {
            todoData = TodoData(
                arguments?.getString("tasskId").toString(),
                arguments?.getString("task").toString())

            binding.todoEt.setText(todoData?.task)
        }

        registerEvents()
    }

    private fun registerEvents(){

        binding.todoNextBtn.setOnClickListener{
            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()){

                if (todoData == null)
                {
                    listener.onSaveTask(todoTask, binding.todoEt)
                }
                else
                {
                    todoData?.task = todoTask
                    listener.onUpdateTask(todoData!!, binding.todoEt)
                }



            }
            else{

                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
            }


        }

        binding.todoClose.setOnClickListener{

            dismiss()
        }


    }

    interface DilogNextBtnClickListener{
        fun onSaveTask(todo: String, todoEt: EditText)


        fun onDeleteTaskClicked(todoData: TodoData)
        fun onUpdateTask(todoData: TodoData, todoEt: EditText)
    }



}