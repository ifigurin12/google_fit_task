package com.example.google_fit_test_task

import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.tasks.Tasks.await
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
        .build()

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL_AUTH
        ).setMethodCallHandler { call, result ->
            if (call.method == "signIn") {
                val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
                if (!accountCheck()) {
                    GoogleSignIn.requestPermissions(
                        this,
                        GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                        account,
                        fitnessOptions
                    )
                }
                result.success(accountCheck())
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

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
    private fun accountCheck(): Boolean {
        val account = getGoogleAccount()
        return account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)
    }
    private fun signOutFromGoogleFit() : Boolean
    {
        var isSignOut = false;
        Fitness.getConfigClient(this, getGoogleAccount())
            .disableFit()
            .addOnSuccessListener {
                isSignOut = true
            }
            .addOnFailureListener { _ ->
                isSignOut = true
            }
        return isSignOut
    }

    private fun getCountData() {
        val end = LocalDateTime.now()
        val start = end.minusYears(1)
        val endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond()
        val startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond()

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_WEIGHT)
            .setTimeRange(startSeconds, endSeconds, TimeUnit.SECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        val response = Fitness.getHistoryClient(this, account)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // The aggregate query puts datasets into buckets, so flatten into a
                // single list of datasets

            }
    }
    fun dumpDataSet(dataSet: DataSet) {
        val TAG = "FITNESS API DATA"
        // Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.}")
        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            for (field in dp.dataType.fields) {
                Log.i(TAG, "\tField: ${field.name.toString()} Value: ${dp.getValue(field)}")
            }
        }
    }
}


