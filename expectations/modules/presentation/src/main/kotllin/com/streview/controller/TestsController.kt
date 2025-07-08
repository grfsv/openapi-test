package com.streview.controller


import com.streview.application.usecases.test.*
import com.streview.application.usecases.test.dto.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.Source
import org.koin.ktor.ext.inject

fun Route.testController() {
    post("/test") {

        TODO("実装する")
    }
    get("/test/{t_id}") {
        // パスパラメータの取得
        val tId = call.parameters["t_id"]

        TODO("実装する")
    }
}
