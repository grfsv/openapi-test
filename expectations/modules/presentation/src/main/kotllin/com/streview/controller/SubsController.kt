package com.streview.controller


import com.streview.application.usecases.subs.*
import com.streview.application.usecases.subs.dto.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.Source
import org.koin.ktor.ext.inject

fun Route.subsController() {
    get("/sub") {

        TODO("実装する")
    }
}
だCl