package org.internship.kmp.martin.services.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

import kotlin.test.assertEquals

class SpotifyApiServiceImplTest {
    @Test
    fun test() = runBlocking {
//        val token = "BQDJJOHU_iKkhFN3q_L52NfiGbRyWUa4xUGCj688u8lYgGFpXP_Ut9noaipZ-r5SRXSlm2Px_Mulfz_snGuZWFuzgayi-Us8Sglzjdtl9PLGsZnemUyAaKhvZjyVQVbNcjk4JK3YB0YsLDRojYbG0i2DLtz4Xsv56f1ode0CP-pOWT8pAnPG2okXKlRxRNOnCdY76cwaJeNnMXVw9njaiLvmQyQxBL5cat7dbMTbbT5MMyaF"
//        val response: HttpResponse = HttpClient().get("https://api.spotify.com/v1/me") {
//            header(HttpHeaders.Authorization, "Bearer $token")
//        }
//        var a: SpotifyUser? = null
//        if (response.status == HttpStatusCode.OK) {
//            a = SpotifyUser.fromJson(response.bodyAsText())
//        } else {
//            null
//        }
//        assertEquals(null, a)
    }
}


//https://i.scdn.co/image/ab6775700000ee85eec931074b7894c79d036a58