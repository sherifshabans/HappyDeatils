package com.example.happyplaces

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.nofit.presentation.MainActiv

class StartActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val anim = findViewById<LottieAnimationView>(R.id.intend)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//           anim.visibility=View.VISIBLE
            anim.playAnimation()
        },2000)
        val anim2 = findViewById<LottieAnimationView>(R.id.happy)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//           anim.visibility=View.VISIBLE
            anim2.playAnimation()
        },2000)



         val plan = findViewById<Button>(R.id.plan)
         val document = findViewById<Button>(R.id.document)

        plan.setOnClickListener {
            val intent = Intent(this@StartActivity,MainActiv::class.java)
           startActivity(intent)
        }
        document.setOnClickListener {
            val intent2 = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent2)
        }
/*
        val actionbar = findViewById<androidx.appcompat.widget.Toolbar?>(R.id.toolbar_start)
        setSupportActionBar(actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

*/


    }

}