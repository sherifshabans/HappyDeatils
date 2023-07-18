package com.example.happyplaces.activities

import android.annotation.SuppressLint
import android.app.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.happyplaces.R
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.model.HappyPlaceModel
import com.example.happyplaces.utils.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.happyplaces.utils.SwipeToEditCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn =findViewById<FloatingActionButton>(R.id.fabAddhappyplace)
        btn.setOnClickListener {


            val intent = Intent (this, AddHappyPlaceActivity::class.java)
            startActivityForResult (intent,ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlacesListFromLocalDB()
    }
    private fun setupHappyPlacesRecyclerView(
        happyPlaceList :ArrayList<HappyPlaceModel>){
        val rv_happy_places_list = findViewById<RecyclerView>(R.id.rv_happy_places_list)
        rv_happy_places_list.layoutManager=LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)
        val placesAdapter=HappyPlacesAdapter(this,happyPlaceList)
        rv_happy_places_list.adapter=placesAdapter

        placesAdapter.setOnClickListener(object :HappyPlacesAdapter.OnClickListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity
                    ,HappyPlaceDetailActivity::class.java)
        intent.putExtra(EXTRA_PLACE_DETAILS,model)
                    startActivity(intent)
            }

        } )
        // TODO(Step 3: Bind the edit feature class to recyclerview)
        // START
        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // TODO (Step 5: Call the adapter function when it is swiped)
                // START
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
                // END
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)
        // END
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.removeat(viewHolder.adapterPosition)
                getHappyPlacesListFromLocalDB()


            }

        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)


    }

    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler= DatabaseHandler(this)
        val getHappyPlaceList:ArrayList<HappyPlaceModel> = dbHandler.getHappyPlaceList()
        val rv_happy_places_list = findViewById<RecyclerView>(R.id.rv_happy_places_list)
        val no_records_available = findViewById<TextView>(R.id.no_records_available)

        if(getHappyPlaceList.size>0){
            rv_happy_places_list.visibility=View.VISIBLE
            no_records_available.visibility=View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }else {
            rv_happy_places_list.visibility=View.GONE
            no_records_available.visibility=View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if(requestCode== ADD_PLACE_ACTIVITY_REQUEST_CODE){
        if(resultCode==Activity.RESULT_OK){
            getHappyPlacesListFromLocalDB()
        }else {
            Log.e("Activity","Canslled or Back pressed")
        }
    }
    }

    companion object{
       var  ADD_PLACE_ACTIVITY_REQUEST_CODE=1
       var EXTRA_PLACE_DETAILS="extra_place_details"
    }

}