package com.example.happyplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.example.happyplaces.R
import com.example.happyplaces.model.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_place_detail)
        var  happyPlaceDetailModel:HappyPlaceModel ?=null
        val toolbar_happy_place_detail= findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_happy_place_detail)
        val iv_place_image= findViewById<ImageView>(R.id.iv_place_image)
        val tv_description= findViewById<TextView>(R.id.tv_description)
        val tv_location= findViewById<TextView>(R.id.tv_location)
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel=
                intent.getParcelableExtra(
                    MainActivity.EXTRA_PLACE_DETAILS)  as HappyPlaceModel?
        }
        if(happyPlaceDetailModel!=null){
            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title=happyPlaceDetailModel.title
            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }
            iv_place_image.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tv_description.text=happyPlaceDetailModel.description
            tv_location.text=happyPlaceDetailModel.location
            
        }

    }
}