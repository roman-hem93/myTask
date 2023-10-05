package ru.artrostudio.mytask
import kotlinx.serialization.Serializable


@Serializable
data class DataTask (val title : String, val message : String, val date : String, val status : Int)