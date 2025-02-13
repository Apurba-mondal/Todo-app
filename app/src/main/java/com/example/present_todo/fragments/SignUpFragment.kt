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
import com.example.present_todo.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
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
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.nextBtn.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val verifypass = binding.repassEt.text.toString().trim()

            if(email.isNotEmpty() && pass.isNotEmpty() && verifypass.isNotEmpty()){
                if(pass == verifypass){

                    binding.progressBar3.visibility = View.VISIBLE

                    auth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context,"Ragistered Successfully",Toast.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_signUpFragment_to_homeFragment2)
                            }
                            else{
                                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()

                            }
                            binding.progressBar3.visibility = View.GONE
                        })
                }
                else
                {
                    Toast.makeText(context, "Password and Re enter password are not same", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}