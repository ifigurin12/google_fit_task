package com.example.google_fit_test_task

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private val CHANNEL_AUTH = "auth_google"
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_AUTH).setMethodCallHandler {
                call, result ->
            if (call.method == "signIn") {
                val api = AuthGoogleApi();
                if(api.)
            }

        }
    }
    val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
        .build()
    class AuthGoogleApi : FlutterActivity() {

        private val serverClientId  = "460023958134-hovmn9ka16d6t0oveur4b6o48hrpgki5.apps.googleusercontent.com"
        var account : GoogleSignInAccount? = null
        lateinit var mGoogleSignInClient : GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(
                Scope("https://www.googleapis.com/auth/fitness.activity.read"),
                Scope("https://www.googleapis.com/auth/fitness.heart_rate.read"),
                Scope("https://www.googleapis.com/auth/fitness.body.read"),
            )
            .requestServerAuthCode(serverClientId)
            .build()


        override fun onStart() {
            super.onStart()
            account = GoogleSignIn.getLastSignedInAccount(this)
            if(account == null)
            {
                signIn()
            }
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        }
        private fun signIn() {
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)

        }
    }
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
