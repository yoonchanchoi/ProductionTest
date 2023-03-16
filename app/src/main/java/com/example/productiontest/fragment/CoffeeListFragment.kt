package com.example.productiontest.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productiontest.CoffeeAdapter
import com.example.productiontest.DetailActivity
import com.example.productiontest.databinding.FragmentCoffeeListBinding
import com.example.productiontest.model.Coffee
import com.example.productiontest.viewmodel.MainViewModel

class CoffeeListFragment : Fragment() {

    private lateinit var binding: FragmentCoffeeListBinding
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var viewModel: MainViewModel
    var category: String = ""
    val dataList: ArrayList<Coffee> = arrayListOf()
    val adapter = CoffeeAdapter(dataList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoffeeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initialize()
    }

    private fun initialize() {
        initData()
        initListener()
        initUi()

        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val newCoffee = result.data?.getSerializableExtra("coffee") as Coffee
                    val newPosition = result.data?.getIntExtra("position", 0) as Int
                    adapter.changeItem(newPosition, newCoffee)
                }
            }
    }

    private fun initData() {
        adapter.filterData = viewModel.filterData
        adapter.filter.filter(viewModel.searchFiltData)
    }

    private fun initListener() {
        adapter.setItemClickListener(object : CoffeeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val coffee = adapter.getCoffeeList(position)
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra("coffee", coffee)
                intent.putExtra("position", position)
                startForResult.launch(intent)
            }
        })
    }

    private fun initUi() {
        val layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = layoutManager
    }
}
