package com.example.productiontest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.productiontest.PageAdapter
import com.example.productiontest.model.Coffee
import com.example.productiontest.model.FilterData
import com.google.gson.Gson
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private var maxProtein: Int = 0
    private var maxFat: Int = 0
    private var maxSugar: Int = 0
    val filterData = FilterData()
    val rangeFilterData = FilterData()
    var searchFiltData = String()

    // 사용자의 단백질 필터 값
    fun updatedProtein(startProtein: Int, endProtein: Int) {
        filterData.startProtein = startProtein
        filterData.endProtein = endProtein
    }

    // 사용자의 지방 필터 값
    fun updatedFat(startFat: Int, endFat: Int) {
        filterData.startFat = startFat
        filterData.endFat = endFat
    }

    // 사용자의 설탕 필터 값
    fun updatedSugar(startSugar: Int, endSugar: Int) {
        filterData.startSugar = startSugar
        filterData.endSugar = endSugar
    }

    // 최대 단백질 값 찾기
    fun updatedMaxPro(protein: Double) {
        if (protein.toInt() >= maxProtein) {
            maxProtein = protein.toInt()
            filterData.endProtein = protein.toInt()
            rangeFilterData.endProtein = protein.toInt()
        }
    }

    // 최대 지방 값 찾기
    fun updatedMaxFat(fat: Double) {
        if (fat.toInt() >= maxFat) {
            maxFat = fat.toInt()
            filterData.endFat = fat.toInt()
            rangeFilterData.endFat = fat.toInt()
        }
    }

    //최대 설탕 값 찾기
    fun updatedMaxSugar(sugar: Double) {
        if (sugar.toInt() >= maxSugar) {
            maxSugar = sugar.toInt()
            filterData.endSugar = sugar.toInt()
            rangeFilterData.endSugar = sugar.toInt()
        }
    }

    //초기화
    fun initFiltData() {
        filterData.startProtein = 0
        filterData.startFat = 0
        filterData.startSugar = 0
        filterData.endProtein = maxProtein
        filterData.endFat = maxFat
        filterData.endSugar = maxSugar
        filterData.sort = 0
        filterData.checkCafein = false
    }

    //저칼로리 정렬
    fun lowSort(check: Boolean) {
        if (check) {
            filterData.sort = -1
        }
    }

    //기본로리 정렬
    fun baseSort(check: Boolean) {
        if (check) {
            filterData.sort = 0
        }
    }

    //고칼로리 정렬
    fun highSort(check: Boolean) {
        if (check) {
            filterData.sort = 1
        }
    }

    fun cafeinCheck(check: Boolean) {
        filterData.checkCafein = check
    }

    fun searchStr(str: String) {
        searchFiltData = str
    }
}

