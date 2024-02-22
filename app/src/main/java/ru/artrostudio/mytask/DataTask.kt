package ru.artrostudio.mytask
import kotlinx.serialization.Serializable


@Serializable
data class DataTask (var id : Int, var title : String, var message : String, var date : String, var status : Int)