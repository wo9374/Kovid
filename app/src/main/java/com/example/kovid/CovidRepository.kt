package com.example.kovid

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kovid.model.CovidItem
import com.example.kovid.model.CurrentCovid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CovidRepository(application: Application){

    private val CURRENT_COVID_URL = "http://openapi.data.go.kr/openapi/service/rest/Covid19/"

    private val CURRENT_COVID_API_KEY_ENCODE = "fWjOJVhpjM%2B3yRGaxkRfldyTsXfvt2rPzz8D8rqvlkjqU1PUu9heerGe%2F48qtQxsovjakR3TcPbBtufVETkE%2Bg%3D%3D"
    private val CURRENT_COVID_API_KEY_DECODE = "fWjOJVhpjM+3yRGaxkRfldyTsXfvt2rPzz8D8rqvlkjqU1PUu9heerGe/48qtQxsovjakR3TcPbBtufVETkE+g=="

    private val covidRetrofit : Retrofit = RetrofitAPI.getRetrofitXmlParsing(CURRENT_COVID_URL)

    private val api = covidRetrofit.create(CovidService::class.java)

    fun getCovidData() : LiveData<List<CovidItem>>{
        val data = MutableLiveData<List<CovidItem>>()

        api.getInfo(CURRENT_COVID_API_KEY_DECODE,1 , 10 , 20220101,20220206).enqueue(object : Callback<CurrentCovid>{
            override fun onResponse(call: Call<CurrentCovid>, response: Response<CurrentCovid>) {
                Log.d("CovidRepository", "Success")
            }

            override fun onFailure(call: Call<CurrentCovid>, t: Throwable) {
                Log.d("CovidRepository", "Fail")
                t.stackTrace
            }
        })
        return data
    }
}