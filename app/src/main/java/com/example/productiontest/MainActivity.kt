package com.example.productiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.productiontest.databinding.ActivityMainBinding
import com.example.productiontest.fragment.*
import com.example.productiontest.model.Coffee
import com.example.productiontest.viewmodel.MainViewModel
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), FilterFragment.SendEventListener {
    private lateinit var binding: ActivityMainBinding
    private val adapter = PageAdapter(supportFragmentManager)
    private lateinit var mainViewMode: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewMode = ViewModelProvider(this).get(MainViewModel::class.java)
        initialize()

//        Solution().solution("hello", 3)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun initialize() {
        initData()
        initListener()
        initUI()
    }

    private fun initData() {
        val menuJson = assets.open("menu.json").reader().readText()
        menuParsing(menuJson)
    }

    private fun menuParsing(menuJson: String) {
        val menuJSONObject = JSONObject(menuJson)
        val menuKeys = menuJSONObject.keys()
        val allCoffeeList: ArrayList<Coffee> = arrayListOf()

        for (key in menuKeys) {
            val jsonArray = menuJSONObject.getJSONObject(key).getJSONArray("list")
            val coffeeList: ArrayList<Coffee> = arrayListOf()
            for (i in 0 until jsonArray.length()) {
                val coffee: Coffee =
                    Gson().fromJson(jsonArray.get(i).toString(), Coffee::class.java)
                coffee.favorite = SharedPreferencesManager.getBoolean(coffee.product_CD)
                mainViewMode.updatedMaxPro(coffee.protein.toDouble())
                mainViewMode.updatedMaxFat(coffee.fat.toDouble())
                mainViewMode.updatedMaxSugar(coffee.sugars.toDouble())
                coffeeList.add(coffee)
                allCoffeeList.add(coffee)
            }
            val coffeeListFragment = CoffeeListFragment().apply {
                category = key
                dataList.addAll(coffeeList)
            }

            adapter.fragmentList.add(coffeeListFragment)
        }
        val allCoffeeListFragment = CoffeeListFragment().apply {
            category = "All"
            dataList.addAll(allCoffeeList)
        }
        adapter.fragmentList.add(0, allCoffeeListFragment)
    }

    private fun initListener() {
        binding.btnFilter.setOnClickListener {
            val filterFragment = FilterFragment()
            filterFragment.show(supportFragmentManager, filterFragment.tag)
        }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(str: String): Boolean {
                return false
            }

            override fun onQueryTextChange(str: String): Boolean {
                mainViewMode.searchStr(str)
                adapter.notifyDataSetChanged()
                return false
            }
        })
    }

    private fun initUI() {
        binding.vp.adapter = adapter
        binding.tlMenu.setupWithViewPager(binding.vp)
    }

    override fun pagerNotify() {
        adapter.notifyDataSetChanged()
    }
}
