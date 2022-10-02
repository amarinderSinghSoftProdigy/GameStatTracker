package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class DivisionResponse(
    @SerializedName("__v")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("basedOn")
    val basedOn: String,
    @SerializedName("clockMgrs")
    val clockMgrs: String,
    @SerializedName("cost")
    val cost: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("divisionName")
    val divisionName: String,
    @SerializedName("eventId")
    val eventId: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("isDelete")
    val isDelete: Boolean,
    @SerializedName("level")
    val level: String,
    @SerializedName("maxAge")
    val maxAge: String,
    @SerializedName("minAge")
    val minAge: String,
    @SerializedName("referees")
    val referees: String,
    @SerializedName("statMgrs")
    val statMgrs: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)