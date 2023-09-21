package com.example.google_fit_test_task

import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.tasks.Task
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private val CHANNEL_AUTH = "google_auth"

    val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 11

    val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
        .build()

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL_AUTH
        ).setMethodCallHandler { call, result ->
            if (call.method == "signIn") {
                val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
                if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
                    GoogleSignIn.requestPermissions(
                        this,
                        GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                        account,
                        fitnessOptions
                    )
                }

            }
            else if (call.method == "isSignedIn") {
                result.success(accountCheck())
            }
        }
    }
    private fun accountCheck(): Boolean {
        val account = getGoogleAccount()
        return account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)
    }
    private fun exitFromGoogleAccount() {
        val account = getGoogleAccount()
        ;

    }
    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
}


/*
class FitnessGoogleApi(private var context: Context, private var activity: MainActivity) : FlutterActivity() {

    val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

    fun checkUsersPermissionFit() {
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                activity, // your activity
                1,
                account,
                fitnessOptions
            )
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            when (resultCode) {
                Activity.RESULT_OK -> when (requestCode) {
                }
            }
        }
    }
}
*/
