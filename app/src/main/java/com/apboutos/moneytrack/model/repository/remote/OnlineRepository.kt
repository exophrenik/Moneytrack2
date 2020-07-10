@file:Suppress("unused", "PrivatePropertyName")

package com.apboutos.moneytrack.model.repository.remote

import android.app.Application
import android.content.Intent
import android.util.Log
import com.apboutos.moneytrack.model.database.entity.User
import com.apboutos.moneytrack.utilities.error.RegisterError
import com.apboutos.moneytrack.viewmodel.receiver.LoginReceiver
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class OnlineRepository(var application: Application) {

    private val TAG = "OnlineRepository"
    private val retro : Retrofit = Retrofit.Builder().baseUrl("http://exophrenik.com/moneytrack/").addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create())).build()
    private val api : RemoteServerAPI = retro.create(RemoteServerAPI::class.java)

    private lateinit var loginResult: AuthenticationRequestResult


    fun authenticateUser(username : String , password : String) {

                api.authenticateUser(AuthenticationRequestData(username, password)).enqueue(object : Callback<AuthenticationRequestResult> {
                    override fun onFailure(call: Call<AuthenticationRequestResult>, t: Throwable) {
                        Log.e(TAG,t.message,t)
                        val intent = Intent()
                        intent.putExtra("error","SERVER_UNREACHABLE")
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.action = LoginReceiver.SERVER_LOGIN_RESPONSE
                        application.sendBroadcast(intent)
                    }

                    override fun onResponse(call: Call<AuthenticationRequestResult>, response: Response<AuthenticationRequestResult>) {

                        Log.d(TAG,"response code:" + response.code())
                        Log.d(TAG,"Response body: " + response.body())
                        val intent = Intent()
                        if(response.body() == null){
                            intent.putExtra("error","SERVER_UNREACHABLE")
                        }
                        else{
                            intent.putExtra("error",response.body()!!.error)
                        }
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.action = LoginReceiver.SERVER_LOGIN_RESPONSE
                        application.sendBroadcast(intent)
                    }
                })
    }

    fun registerUser( user : User) : RegisterError {

        if(!NetworkTester.internetConnectionIsAvailable(application)){
            return RegisterError.NO_INTERNET
        }

        var result : RegistrationResult? = null

        api.registerUser(user).enqueue(object : Callback<RegistrationResult>{
            override fun onFailure(call: Call<RegistrationResult>, t: Throwable) {
                Log.e(TAG,t.message,t)
            }

            override fun onResponse(call: Call<RegistrationResult>, response: Response<RegistrationResult>
            ) {
                result = response.body()
                Log.d(TAG,"response code:" + response.code())
                Log.d(TAG,result?.error)
            }
        })

        if(result == null){Log.e(TAG,"nullulululuulul")}
        if(result == null) {return RegisterError.SERVER_UNREACHABLE}

        if(result!!.error == "USER_EXISTS"){return RegisterError.USERNAME_TAKEN}

        if(result!!.error == "EMAIL_TAKEN"){return RegisterError.EMAIL_TAKEN}

        return RegisterError.NO_ERROR
    }

}


