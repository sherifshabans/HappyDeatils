package com.example.happyplaces.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.*

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.happyplaces.R
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.model.HappyPlaceModel
import com.google.android.libraries.maps.MapView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * An variable to get an instance calendar using the default time zone and locale.
     */
    private var cal = Calendar.getInstance()

    /**
     * A variable for DatePickerDialog OnDateSetListener.
     * The listener used to indicate the user has finished selecting a date. Which we will be initialize later on.
     */
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    // TODO (Step 6 : Now as per our Data Model Class we need some of the values to be passed so let us create that global which will be used later on.)
    // START
    private var saveImageToInternalStorage: Uri? = null

    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0 // A variable which will hold the longitude value.
    // END

    private  var    mHappyPlaceDetails : HappyPlaceModel?=null
    /**
     * This function is auto created by Android when the Activity Class is created.
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_happy_place)
        val et_description= findViewById<EditText>(R.id.et_description)
        val et_date= findViewById<EditText>(R.id.et_date)
        val et_location= findViewById<EditText>(R.id.et_location)
        val et_title= findViewById<EditText>(R.id.et_title)
        val iv_place_image= findViewById<ImageView>(R.id.iv_place_image)
        val btn_save =findViewById<Button>(R.id.btn_save)
        val tv_add_image =findViewById<TextView>(R.id.tv_add_image)
        val actionbar = findViewById<androidx.appcompat.widget.Toolbar?>(R.id.toolbar_add_place)
        setSupportActionBar(actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionbar.setNavigationOnClickListener{
            onBackPressed()
        }
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            mHappyPlaceDetails=intent.getParcelableExtra(
                MainActivity.EXTRA_PLACE_DETAILS,
              )as HappyPlaceModel ?
        }

        dateSetListener= DatePickerDialog.OnDateSetListener{
            view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            updateDateInView()
        }
        updateDateInView()

        if(mHappyPlaceDetails!=null){
       supportActionBar!!.title="Edit Happy Place"
       et_title.setText(mHappyPlaceDetails!!.title)
       et_date.setText(mHappyPlaceDetails!!.date)
       et_description.setText(mHappyPlaceDetails!!.description)
       et_location.setText(mHappyPlaceDetails!!.location)
        mLatitude= mHappyPlaceDetails!!.latitude
        mLongitude =mHappyPlaceDetails!!.longitude

         saveImageToInternalStorage= Uri.parse(
             mHappyPlaceDetails!!.image
         )
          iv_place_image.setImageURI(saveImageToInternalStorage)

            btn_save.text="UPDATE"

        }


        et_date.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)
        btn_save.setOnClickListener(this)













    }

    override fun onClick(v: View?) {
           when(v!!.id){
            R.id.et_date ->{
                DatePickerDialog(
                    this@AddHappyPlaceActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR)
                    ,cal.get(Calendar.MONTH)
                    ,cal.get(Calendar.DAY_OF_MONTH)

                ).show()

            }
            R.id.tv_add_image ->{
                val pictureDialog =AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItem= arrayOf("Select photo from Gallery",
                "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItem){
                    dialog,which ->
                    when(which){
                        // Here we have create the methods for image selection from GALLERY
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera() // TODO (Step 6: Call the camera selection method over here.)
                    }
                }
                pictureDialog.show()
            }
            R.id.btn_save ->{
                val et_title=findViewById<EditText>(R.id.et_title)
                val et_description=findViewById<EditText>(R.id.et_description)
                val et_location=findViewById<EditText>(R.id.et_location)
                val et_date=findViewById<EditText>(R.id.et_date)

                when {

                   et_title.text.isNullOrEmpty() -> {
                       Toast.makeText(this, "Please enter title ", Toast.LENGTH_LONG).show()

                   }
                   et_description.text.isNullOrEmpty() -> {
                       Toast.makeText(this, "Please enter a description ", Toast.LENGTH_LONG).show()

                   }
                   et_location.text.isNullOrEmpty() -> {
                       Toast.makeText(this, "Please enter a location ", Toast.LENGTH_LONG).show()

                   }
            saveImageToInternalStorage ==null  ->{
                Toast.makeText(this, "Please select an image ", Toast.LENGTH_LONG).show()
               }else ->{
                   val happyPlaceModel=HappyPlaceModel(
                       if(mHappyPlaceDetails==null)0 else mHappyPlaceDetails!!.id,
                       et_title.text.toString(),
                       saveImageToInternalStorage.toString(),
                       et_description.text.toString(),
                       et_date.text.toString(),
                       et_location.text.toString(),
                       mLatitude,
                       mLongitude,
                   )
                    val dbHandler=DatabaseHandler(this)
                    if(mHappyPlaceDetails==null){
                        val addHappyPlace=dbHandler.addHappyPlace(happyPlaceModel)
                        if(addHappyPlace>0){
                            setResult(Activity.RESULT_OK)
                            finish()

                        }
                    }else {
                        val updateHappyPlace=dbHandler.updateHappyPlace(happyPlaceModel)
                        if(updateHappyPlace>0){
                            setResult(Activity.RESULT_OK)
                            finish()

                        }
                    }

               }

               }
            }

           }
    }

    // TODO(Step 3 : Receive the result of GALLERY and CAMERA.)
    // START
    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        // Here this is used to get an bitmap from URI
                        @Suppress("DEPRECATION")
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        val  iv_place_image = findViewById<ImageView>(R.id.iv_place_image)

                        saveImageToInternalStorage =    saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("Saved Image: ","Path :: $saveImageToInternalStorage")
                        iv_place_image!!.setImageBitmap(selectedImageBitmap) // Set the selected image from GALLERY to imageView.
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@AddHappyPlaceActivity, "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
                // TODO (Step 7: Camera result will be received here.)
            } else if (requestCode == CAMERA) {

                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap // Bitmap from camera

                saveImageToInternalStorage =    saveImageToInternalStorage(thumbnail)
                Log.e("Saved Image: ","Path :: $saveImageToInternalStorage")

                val  iv_place_image = findViewById<ImageView>(R.id.iv_place_image)
                iv_place_image!!.setImageBitmap(thumbnail) // Set to the imageView.
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }
    // END

    // TODO (Step 4 : Creating a method for image capturing and selecting from camera.)
    // START
    /**
     * A method is used for image selection from GALLERY / PHOTOS of phone storage.
     */
    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    // Here after all the permission are granted launch the gallery to select and image.
                    if (report!!.areAllPermissionsGranted()) {

                        // TODO(Step 1 : Adding an image selection code here from Gallery or phone storage.)
                        // START
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent, GALLERY)
                        // END
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }
            }).onSameThread()
            .check()
    }

    // TODO (Step 4 : Creating a method for image capturing and selecting from camera.)
    // START
    /**
     * A method is used  asking the permission for camera and storage and image capturing and selection from Camera.
     */
    private fun takePhotoFromCamera() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // Here after all the permission are granted launch the CAMERA to capture an image.
                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }
            }).onSameThread()
            .check()
    }
    // END

    /**
     * A function used to show the alert dialog when the permissions are denied and need to allow it from settings app info.
     */
    private fun showRationalDialogForPermission()
    {
        AlertDialog.Builder(this).setMessage("" +
                "It looks like you have turned off permission required for this feature ." +
                " It can be enabled under the Applications Settings ")
            .setPositiveButton("Go To SETTINGS"){ _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()

    }
    // TODO (Step 2 : Creating a method to save a copy of an selected image to internal storage for use of Happy Places App.)
    // START
    /**
     * A function to save a copy of an image to internal storage for HappyPlaceApp to use.
     */
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        /**
         * The Mode Private here is
         * File creation mode: the default mode, where the created file can only
         * be accessed by the calling application (or all applications sharing the
         * same user ID).
         */
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }
    // END








    /**
     * A function to update the selected date in the UI with selected format.
     * This function is created because every time we don't need to add format which we have added here to show it in the UI.
     */
    private fun updateDateInView(){
        val myFormat="dd.MM.yyyy"
        val sdf=SimpleDateFormat(myFormat,Locale.getDefault())
        val et_date=findViewById<EditText>(R.id.et_date)
         et_date.setText(sdf.format(cal.time).toString())
    }

    companion object {
        // TODO(Step 2 : Create a variable for GALLERY Selection which will be later used in the onActivityResult method.)
        // START
        private const val GALLERY = 1
        // END

        // TODO(Step 5 : Create a variable for CAMERA Selection which will be later used in the onActivityResult method.)
        // START
        private const val CAMERA = 2
        // END // TODO(Step 1 : Creating an cont variable to use for Directory name for copying the selected image.)
        //        // START
                private const val IMAGE_DIRECTORY = "HappyPlacesImages"
        //        // END

    }
}