package com.udemy.happyplaces

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.udemy.happyplaces.databinding.ActivityAddHappyPlaceBinding
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityAddHappyPlaceBinding? = null
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarAddPlace)
        //액션바가 있으면
        if(supportActionBar != null){
            // 버튼 보여주게 한다
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        // 액션바 설정
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()

        }
        binding?.etDate?.setOnClickListener(this)
        binding?.ivPlaceImage?.setOnClickListener(this)
    }

    // a모든 클릭 리스너
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.et_date -> {
                DatePickerDialog(this@AddHappyPlaceActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                    .show()
            }
            R.id.iv_place_image ->{
                println(1)
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from Gallery", "Capture photo from Camera")
                pictureDialog.setItems(pictureDialogItems){
                        _, which ->
                        when(which){
                            0 -> choosePhotoFroGallery()
                            1 -> {

                            }
                        }
                }
                pictureDialog.create().show()
            }
        }
    }
    private fun choosePhotoFroGallery(){
        // 권한 설정 서드파티 라이브러리
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener {
            // 권한이 확인되면
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if(report.areAllPermissionsGranted()){
                    Toast.makeText(this@AddHappyPlaceActivity,"permission granted", Toast.LENGTH_LONG).show()
                }
            }
            // 권한이 필요한지 보여줘야함
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                showRationaleDialogForPermissions()
            }
        }).onSameThread().check()
    }

    // 권한 없을 시 왜 필요한 지 보여주며 설정으로
    private fun showRationaleDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission required for this feature. It can be enabled under the Application Settings")
            .setPositiveButton("GO TO SETTINGS"){ _, _ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }


    private fun updateDateInView(){
        val format = "yyyy.MM.dd"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }
}