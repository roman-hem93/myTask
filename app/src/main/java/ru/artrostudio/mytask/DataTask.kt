package ru.artrostudio.mytask
import kotlinx.serialization.Serializable


@Serializable
data class DataTask (var id : Long, var title : String, var description : String, var date : String, var status : Int)