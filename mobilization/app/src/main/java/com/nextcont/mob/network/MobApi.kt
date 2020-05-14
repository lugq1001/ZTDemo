package com.nextcont.mob.network

import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.nextcont.mob.network.response.BaseResponse
import com.nextcont.mob.network.response.DeviceRegisterResponse
import com.nextcont.mob.network.response.LoginResponse
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.lang.Exception

object MobApi {

    private const val BaseUrl = "http://192.168.15.60:7000"

    private val json: Gson = Gson()

    /**
     * 设备注册
     */
    fun deviceRegister(deviceName: String, fingerprint: String): Single<DeviceRegisterResponse> {
        return Single.create { emitter ->
            try {
                val params = mapOf(
                    Pair("deviceName", deviceName),
                    Pair("fingerprint", fingerprint)
                )

                val response = "${BaseUrl}/device"
                    .httpPost()
                    .jsonBody(json.toJson(params))
                    .responseString()

                val resp = parseResponse<DeviceRegisterResponse>(response)
                Timber.d("注册设备: $resp")
                emitter.onSuccess(resp)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /**
     * 登录
     */
    fun login(username: String, password: String, deviceId: String): Single<LoginResponse> {
        return Single.create { emitter ->
            try {
                val params = mapOf(
                    Pair("username", username),
                    Pair("password", password),
                    Pair("deviceId", deviceId)
                )

                val response = "${BaseUrl}/login"
                    .httpPost()
                    .jsonBody(json.toJson(params))
                    .responseString()

                val resp = parseResponse<LoginResponse>(response)
                Timber.d("用户登录: $resp")
                emitter.onSuccess(resp)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private inline fun <reified T> parseResponse(response: ResponseResultOf<String>): T {
        return when (val result = response.third) {
            is Result.Failure -> {
                throw Exception("${response.second.statusCode}.${result.error.message}")
            }
            is Result.Success -> {
                val data = result.get()
                Timber.d(data)
                val resp = json.fromJson(data, BaseResponse::class.java)
                when (resp.code) {
                    0 -> {
                        json.fromJson(resp.data, T::class.java)
                    }
                    else -> throw Exception(resp.message)
                }
            }
        }
    }

}