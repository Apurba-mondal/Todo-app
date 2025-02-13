package com.example.present_todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.present_todo.R
import com.example.present_todo.databinding.FragmentSignInBinding
import com.example.present_todo.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents(){

        binding.authTextVew.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.nextBtn.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()


            if(email.isNotEmpty() && pass.isNotEmpty() ){

                binding.progressBar.visibility = View.VISIBLE
               loginUser(email, pass)

            }
            else{
                Toast.makeText(context, "Empty fields are not allowed",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginUser(email:String, pass:String){
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if(it.isSuccessful){
                navController.navigate(R.id.action_signInFragment_to_homeFragment)
            }

            else{
                Toast.makeText(context, "Something went Wrong Please check and try again" , Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility = View.GONE
        }
    }


}