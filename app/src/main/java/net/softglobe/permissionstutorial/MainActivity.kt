package net.softglobe.permissionstutorial

import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.DialogInterface.OnClickListener
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var permissionsList = arrayOf(POST_NOTIFICATIONS, CAMERA)
    var permissionRequestCode = 1

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       findViewById<Button>(R.id.permissions_btn).setOnClickListener {
           if (checkPermissions()) {
               Toast.makeText(this, "All permissions already granted", Toast.LENGTH_SHORT).show()
           } else {
               requestPermissions(permissionsList, permissionRequestCode)
           }
       }
    }

    private fun checkPermissions() : Boolean{
        for (permission in permissionsList) {
            val result = checkSelfPermission(permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            var shouldShowPermissionRationale = false

            for (permision in permissions) {
                if (shouldShowRequestPermissionRationale(permision)) {
                    shouldShowPermissionRationale = true
                    break
                }
            }

            if (shouldShowPermissionRationale) {
                showOkcancelAlertDialog("Alert", "Please allow all the permissions in order to serve you to the fullest") { dialog, which ->
                    requestPermissions(permissions, permissionRequestCode)
                }
            } else {
                var areAllPermissionsGranted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        areAllPermissionsGranted = false
                        break
                    }
                }

                if (areAllPermissionsGranted) {
                    Toast.makeText(this, "All permissions have been granted", Toast.LENGTH_SHORT).show()
                } else {
                    showOkcancelAlertDialog("Alert", "Please go to app settings and allow the permissions manually.") { dialog, which ->
                        //move the user to app permissions page
                    }
                }
            }
        }
    }

    private fun showOkcancelAlertDialog(title : String, message : String, listener: OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.icon_warning_sign)
            .setPositiveButton("OK", listener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}