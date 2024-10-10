package com.example.present_todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.present_todo.databinding.FragmentHomeBinding
import com.example.present_todo.utils.ToDoAdapter
import com.example.present_todo.utils.TodoData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), AddTodoFragment.DilogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private  var popUpFragment: AddTodoFragment?= null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList : MutableList<TodoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        ragisterEvents()
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Task").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()

                for (taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let {
                        TodoData(it, taskSnapshot.value.toString())
                    }

                    if (todoTask != null)
                    {
                        mList.add(todoTask)
                    }

                }

                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun ragisterEvents(){

        binding.addbtnhome.setOnClickListener{

            if (popUpFragment != null)
            {
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }
            popUpFragment = AddTodoFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(childFragmentManager, AddTodoFragment.Tag)

        }
    }

    override fun onSaveTask(todo: String, todoEt: EditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {

            if(it.isSuccessful){

                Toast.makeText(context, "Saved Successfully !!", Toast.LENGTH_SHORT).show()
                todoEt.text = null
            }
            else{

                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskClicked(todoData: TodoData) {

        this.databaseRef.child(todoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful)
            {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onUpdateTask(todoData: TodoData, todoEt: EditText) {

        val map = HashMap<String, Any>()
        map[todoData.taskId] = todoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful)
            {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            todoEt.text = null
            popUpFragment!!.dismiss()
        }

    }

    override fun onDeleteTaskBtnClicked(todoData: TodoData) {

        this.databaseRef.child(todoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful)
            {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onEditTaskBtnClicked(todoData: TodoData) {
        // Remove existing popup fragment if it's showing
        popUpFragment?.let {
            childFragmentManager.beginTransaction().remove(it).commit()
        }

        // Create a new instance of AddTodoFragment with the task data
        popUpFragment = AddTodoFragment.newInstance(todoData.taskId, todoData.task)
        popUpFragment?.setListener(this)
        popUpFragment?.show(childFragmentManager, AddTodoFragment.Tag)
    }






}