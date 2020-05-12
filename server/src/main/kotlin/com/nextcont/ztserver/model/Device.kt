package com.nextcont.ztserver.model

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index
import org.dizitart.no2.objects.Indices

@Indices(Index(value = "userId", type = IndexType.Unique))
data class Device(
    @Id val id: String,
    val userId: String,
    val name: String,
    val netEnv: NetworkEnv
) {

    enum class NetworkEnv {
        Mobile,
        Wifi;

        companion object {
            fun fromString(str: String): NetworkEnv? {
                return when (str) {
                    "mobile" -> Mobile
                    "wifi" -> Wifi
                    else -> null
                }
            }
        }
    }
}