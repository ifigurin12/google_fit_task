package com.example.google_fit_test_task

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.tasks.Task
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class MainActivity: FlutterActivity() {
    private val CHANNEL_AUTH = "google_auth"
    private val CHANNEL_FITNESS_DATA = "fitness_data"

    val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 11

    val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("460023958134-hovmn9ka16d6t0oveur4b6o48hrpgki5.apps.googleusercontent.com")
        .addExtension(fitnessOptions)
        .requestEmail()
        .build()


    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL_AUTH
        ).setMethodCallHandler { call, result ->
            if (call.method == "signIn") {
                val account = getGoogleAccount()
                if (signIn())
                    {
                        result.success(true)
                    }


                else {
                    result.success(false)
                }

            }
            else if (call.method == "isSignedIn") {
                result.success(accountCheck())
            }
            else if (call.method == "signOut")
            {
                val isSignOut = signOutFromGoogleFit()
                result.success(isSignOut)
            }
        }
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL_FITNESS_DATA
        ).setMethodCallHandler{ call, result ->
            if (call.method == "getCountData") {
                val data = getCountData()
                print(data);
                result.success(1)
            }

        }
    }

    private fun signIn(): Boolean{
        val client = GoogleSignIn.getClient(this, gso)
        val task: Task<GoogleSignInAccount> = client.silentSignIn()
        if (task.isSuccessful) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                getGoogleAccount(),
                fitnessOptions
            )
            return true;
        } else {
           return false;
        }
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions);//
    private fun accountCheck(): Boolean {
        val account = getGoogleAccount()
        return account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)
    }
    private fun signOutFromGoogleFit() : Boolean
    {
        val client = GoogleSignIn.getClient(this, gso)
        var isSignOut = false;
        client.signOut()
            .addOnSuccessListener {
                Log.i("SIGN_OUT", "Удалось выйти")
                isSignOut = true
            }
            .addOnFailureListener { _ ->
                isSignOut = true
                Log.i("SIGN_OUT", "Удалось выйти")
            }
        return isSignOut
    }

    private fun getCountData() {
        checkFitPermission()
        val end = LocalDateTime.now()
        val start = end.minusYears(1)
        val endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond()
        val startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond()

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_HEART_RATE_BPM)
            .setTimeRange(startSeconds, endSeconds, TimeUnit.DAYS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        val response = Fitness.getHistoryClient(this, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
               dumpDataSet(response.getDataSet(DataType.TYPE_HEART_RATE_BPM))

            }
    }
    private fun checkFitPermission()
    {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1)
        }
        else if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.BODY_SENSORS)
                    == PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.BODY_SENSORS),
                1)
        }
    }
    fun dumpDataSet(dataSet: DataSet) {
        val TAG = "FITNESS API DATA"
        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            for (field in dp.dataType.fields) {
                Log.i(TAG, "\tField: ${field.name.toString()} Value: ${dp.getValue(field)}")
            }
        }
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType}")
        Log.i(TAG, dataSet.toString())

    }
}


