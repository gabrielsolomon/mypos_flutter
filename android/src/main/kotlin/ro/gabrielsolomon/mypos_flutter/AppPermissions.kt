package ro.gabrielsolomon.mypos_flutter

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

class AppPermissions {

    fun isLocationPermissionGranted(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                activity.requestPermissions(
                        arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 1114
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }
}