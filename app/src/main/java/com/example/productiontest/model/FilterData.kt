package com.example.productiontest.model

import java.io.Serializable

data class FilterData(
    var checkCafein: Boolean = false,
    var sort: Int = 0,
    var startProtein: Int = 0,
    var endProtein: Int = 0,
    var startFat: Int = 0,
    var endFat: Int = 0,
    var startSugar: Int = 0,
    var endSugar: Int = 0
) : Serializable {

}
