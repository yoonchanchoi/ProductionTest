package com.example.productiontest.model

import java.io.Serializable

data class Coffee(
    var content: String,
    var product_CD: String,
    var product_NM: String,
    var file_PATH: String,
    var cate_NAME: String,
    var kcal: String,
    var fat: String,
    var sat_FAT: String,
    var sugars: String,
    var protein: String,
    var sodium: String,
    var caffeine: String,
    var favorite: Boolean = false
) : Serializable {

}
