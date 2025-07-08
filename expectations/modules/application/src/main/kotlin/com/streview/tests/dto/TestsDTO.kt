package com.streview.usecase.tests.dto
import kotlinx.serialization.Serializable
import com.streview.application.usecases.*
import kotlinx.datetime.LocalDate

@Serializable
data class TestGetResObject(val getObjectPropInt: Int)

@Serializable
data class TestGetRes(val testGetResString: String, val testGetResArray: List<String>, val testGetResObject: List<TestGetResObject>): OutputPort


@Serializable
data class TestPostReqArrayItem(val arrayInDate: String, val List<String>)

@Serializable
data class TestPostReq(val testPostReqArray: List<>): InputPort


@Serializable
data class TestPostRes(val testUuid: String): OutputPort

