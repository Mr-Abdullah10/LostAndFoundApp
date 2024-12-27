package com.abdullah.lostfound.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullah.lostfound.databinding.FragmentSearchBinding
import com.abdullah.lostfound.ui.adapters.LostAdapter
import com.abdullah.lostfound.ui.dataclasses.Lost

import kotlinx.coroutines.launch

class CompleteFragment : Fragment() {
    lateinit var adapter: LostAdapter
    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: CompleteFragmentViewModel
    val items=ArrayList<Lost>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("test1", "Test")
        adapter= LostAdapter(items)
        binding.recyclerview.adapter=adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(context)

        viewModel= CompleteFragmentViewModel()
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.data.collect {
                it?.let {
                    items.clear()
                    items.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

}