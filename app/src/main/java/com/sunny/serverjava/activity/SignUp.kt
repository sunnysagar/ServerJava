package com.sunny.serverjava.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sunny.serverjava.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var mauth : FirebaseAuth
    lateinit var email : EditText
    lateinit var mobile : EditText
    lateinit var name : EditText
    lateinit var pswd : EditText
    lateinit var btnSignup : Button
    lateinit var or : TextView
    lateinit var goog : ImageView
    lateinit var progressBar: ProgressBar
//    lateinit var username: EditText
    lateinit var database : DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mauth = FirebaseAuth.getInstance()

//        name = findViewById(R.id.etName)
        email = findViewById(R.id.etEmail)
//        mobile = findViewById(R.id.etMobile)
//        name = findViewById(R.id.etName)
        pswd = findViewById(R.id.etPassword)
//        cnPswd = findViewById(R.id.etcnfPassword)
        btnSignup = findViewById(R.id.btnSignup)
//        or = findViewById(R.id.or)
//        goog = findViewById(R.id.sga)
        val btnSignIn = findViewById<Button>(R.id.btnSignin)
        progressBar = findViewById(R.id.progress)

//        username = findViewById(R.id.etUsername)



        btnSignup.setOnClickListener {

//            val name = name.text.toString()
            val emailId = email.text.toString()
            val password = pswd.text.toString()
//            val userName = username.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                email.error = "Invalid Email";
                email.isFocusable = true;
            } else if (password.length < 6) {
                Toast.makeText(this, "Length Must be greater than 6 character", Toast.LENGTH_SHORT)
                    .show()
            }
            else{

                progressBar.visibility = View.VISIBLE
                lifecycleScope.launch(Dispatchers.IO) {
                    signUpWithEmailAndPassword(mauth,emailId,password)
                }

            }

            // adding Real Database

//            database = FirebaseDatabase.getInstance().getReference("Users")
//            val User = Users(name,emailId,userName)
//            database.child(userName).setValue(User)
//                .addOnSuccessListener {
//
////                    val checkUserDatabase: Query =
////                        reference.orderByChild("username").equalTo(userUsername)
//
//
//                    //Pass the data using intent
////                    val nameFromDB: String =
////                        snapshot.child(userUsername).child("name").getValue<String>(
////                            String::class.java
////                        )
//
//
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//
//            }.addOnFailureListener {
//                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
//            }
//


        }

        btnSignIn.setOnClickListener {

            progressBar.visibility = View.VISIBLE

          startActivity(Intent(this@SignUp,MainActivity::class.java))

            val email = email.text.toString()
            val password = pswd.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                signInWithEmailAndPassword(mauth,email,password)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun signUpWithEmailAndPassword(firebaseAuth: FirebaseAuth,
                                   emailId:String,
                                   password:String) {

//        val result = firebaseAuth.createUserWithEmailAndPassword(emailId,password)
//            .await()
//        Log.d("AuthResult","${result.user?.email}")
//        return result
        val result = firebaseAuth.createUserWithEmailAndPassword(emailId,password)
        result.addOnSuccessListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this@SignUp,"Success",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SignUp, MainActivity::class.java))
            finish()
        }.addOnFailureListener{

            Toast.makeText(this@SignUp,"${it.message}",Toast.LENGTH_SHORT).show()
            Log.d("AuthResult","${it.message}")
//                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

//        return try{
//            val result = firebaseAuth.createUserWithEmailAndPassword(emailId,password).await()
//
////            Toast.makeText(this,"${result.user?.email}",Toast.LENGTH_SHORT).show()
////            Log.d("AuthResult","${result.user?.email}")
//
//            updateUI(result.user)
//            result
//        }catch (e:Exception){
//            withContext(Dispatchers.Main){
//                Toast.makeText(this@SignupActivity,"${e.message}",Toast.LENGTH_SHORT).show()
//                Log.d("AuthResult","${e.message}")
//            }
//            null
//        }
    }

    suspend fun signInWithEmailAndPassword(
        firebaseAuth: FirebaseAuth,
        emailId:String,
        password:String) : AuthResult? {

//        val result = firebaseAuth.createUserWithEmailAndPassword(emailId,password)
//            .await()
//        Log.d("AuthResult","${result.user?.email}")
//        return result

        return try{
            val result = firebaseAuth.signInWithEmailAndPassword(emailId,password).await()
//            Toast.makeText(this,"${result.user?.email}",Toast.LENGTH_SHORT).show()
//            Log.d("AuthResult","${result.user?.email}")
            startActivity(Intent(this,MainActivity::class.java))
            result
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@SignUp,"${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("AuthResult","${e.message}")

            }
            null
        }
    }

//    private fun updateUI(firebaseUser: Task<AuthResult>){
//        Log.d("AuthResult","${firebaseUser?.email}")
//
//            progressBar.visibility = View.GONE
//            Toast.makeText(this@SignupActivity,"Success",Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this@SignupActivity, BasicDetails::class.java))
//            finish()
//
//
//    }

    override fun onStart(){
        super.onStart()

        val currentUser = mauth.currentUser

        if(currentUser!=null){
//            var intent = Intent(this@SignupActivity, CheckUp::class.java)
            val intent = Intent(this@SignUp, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}
