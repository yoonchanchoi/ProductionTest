package com.example.productiontest.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.productiontest.PageAdapter
import com.example.productiontest.databinding.FragmentFilterBinding
import com.example.productiontest.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.LabelFormatter.LABEL_GONE

class FilterFragment : BottomSheetDialogFragment() {

    interface SendEventListener {
        fun pagerNotify()
    }

    private lateinit var binding: FragmentFilterBinding
    lateinit var viewModel: MainViewModel
    lateinit var sendEventListener: SendEventListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sendEventListener = context as SendEventListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initialize()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            if (this is BottomSheetDialog) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
                behavior.isHideable = false
            }
        }
    }

    private fun initialize() {
        initListener()
        initUI()
    }

    private fun initListener() {
        binding.rsProtein.addOnChangeListener { slider, value, fromUser ->
            binding.tvProRange.text = "${slider.values[0].toInt()} ~ ${slider.values[1].toInt()}"
        }
        binding.rsFat.addOnChangeListener { slider, value, fromUser ->
            binding.tvFatRange.text = "${slider.values[0].toInt()} ~ ${slider.values[1].toInt()}"
        }
        binding.rsSugar.addOnChangeListener { slider, value, fromUser ->
            binding.tvSugarRange.text = "${slider.values[0].toInt()} ~ ${slider.values[1].toInt()}"
        }
        binding.btnCloss.setOnClickListener {
            dismiss()
        }
        binding.btnInit.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle("초기화")
                .setMessage("초기화 하실건가요?")
                .setPositiveButton("네",
                    DialogInterface.OnClickListener { dialog, which ->
                        viewModel.initFiltData()
                        sendEventListener.pagerNotify()
                        this.dismiss()
                    })
                .setNegativeButton("아니오",
                    DialogInterface.OnClickListener { dialog, which ->
                    })
            builder.show()
        }
        binding.btnApply.setOnClickListener {
            viewModel.updatedProtein(
                binding.rsProtein.values[0].toInt(),
                binding.rsProtein.values[1].toInt()
            )
            viewModel.updatedFat(
                binding.rsFat.values[0].toInt(),
                binding.rsFat.values[1].toInt()
            )
            viewModel.updatedSugar(
                binding.rsSugar.values[0].toInt(),
                binding.rsSugar.values[1].toInt()
            )
            viewModel.cafeinCheck(binding.scCafainCheck.isChecked)
            viewModel.lowSort(binding.rbLow.isChecked)
            viewModel.baseSort(binding.rbBasic.isChecked)
            viewModel.highSort(binding.rbHigh.isChecked)
            sendEventListener.pagerNotify()
            dismiss()
        }
    }

    private fun initUI() {
        binding.rsProtein.setValues(
            viewModel.filterData.startProtein.toFloat(),
            viewModel.filterData.endProtein.toFloat()
        )
        binding.rsFat.setValues(
            viewModel.filterData.startFat.toFloat(),
            viewModel.filterData.endFat.toFloat()
        )
        binding.rsSugar.setValues(
            viewModel.filterData.startSugar.toFloat(),
            viewModel.filterData.endSugar.toFloat()
        )

        binding.scCafainCheck.isChecked = viewModel.filterData.checkCafein
        binding.rsProtein.valueFrom = viewModel.rangeFilterData.startProtein.toFloat()
        binding.rsProtein.valueTo = viewModel.rangeFilterData.endProtein.toFloat()
        binding.rsFat.valueFrom = viewModel.rangeFilterData.startFat.toFloat()
        binding.rsFat.valueTo = viewModel.rangeFilterData.endFat.toFloat()
        binding.rsSugar.valueFrom = viewModel.rangeFilterData.startSugar.toFloat()
        binding.rsSugar.valueTo = viewModel.rangeFilterData.endSugar.toFloat()

        when (viewModel.filterData.sort) {
            -1 -> binding.rbLow.isChecked = true
            0 -> binding.rbBasic.isChecked = true
            1 -> binding.rbHigh.isChecked = true
        }
    }
}

