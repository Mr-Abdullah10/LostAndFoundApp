package com.abdullah.lostfound.ui.main.lost

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.abdullah.lostfound.R
import com.abdullah.lostfound.Repositories.AuthRepository
import com.abdullah.lostfound.databinding.ActivityLostDetailsBinding
import com.abdullah.lostfound.ui.dataclasses.Lost
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LostDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostDetailsBinding
    private lateinit var lost: Lost
    private lateinit var viewModel: LostDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = LostDetailsViewModel()

        // Parse Lost object from Intent
        lost = Gson().fromJson(intent.getStringExtra("data"), Lost::class.java)

        // Retrieve the fragment source identifier
        val isFromLostFragment = intent.getBooleanExtra("isFromLostFragment", true)

        // Populate UI elements with Lost data
        binding.postDate.text = intent.getStringExtra("postDate")
        binding.categoryinput.text = intent.getStringExtra("categoryinput")
        binding.descriptioninput.text = intent.getStringExtra("descriptioninput")
        binding.addressinput.text = intent.getStringExtra("addressinput")
        binding.nameinput.text = intent.getStringExtra("nameinput")
        binding.email.text = intent.getStringExtra("email")
        binding.contactinput.text = intent.getStringExtra("contactinput")
        binding.status.text = intent.getStringExtra("status")
        binding.spinnerPostType.text = if (lost.isLost) "Lost" else "Found"

        // Load image using Glide
        Glide.with(this)
            .load(lost.image)
            .error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(binding.imageView2)

        // Set visibility for buttons based on fragment source
        val user: FirebaseUser = AuthRepository().getCurrentUser()!!
        val isAdmin = user.email == "abdullahahsan438@gmail.com"

        // Admin can see all buttons
        if (isAdmin) {
            binding.claimItem.visibility = View.VISIBLE
            binding.returnItem.visibility = View.VISIBLE
        } else {
            if (isFromLostFragment) {
                binding.claimItem.visibility = View.GONE // Hide Claim button in Lost Fragment
                binding.returnItem.visibility = View.VISIBLE // Show Return button in Lost Fragment
            } else {
                binding.claimItem.visibility = View.VISIBLE // Show Claim button in Found Fragment
                binding.returnItem.visibility = View.GONE // Hide Return button in Found Fragment
            }
        }

        if (!isAdmin || lost.status == "Case Deleted") {
            binding.deleteCase.visibility = View.GONE
        }

        // Set button click listeners
        binding.claimItem.setOnClickListener {
            lost.status = "Item Claimed"
            viewModel.updateLost(lost)
        }

        binding.returnItem.setOnClickListener {
            lost.status = "Delivered"
            viewModel.updateLost(lost)
        }

        binding.deleteCase.setOnClickListener {
            viewModel.deleteCase(lost.id)
        }

        // Observe updates to Lost object
        lifecycleScope.launch {
            viewModel.isUpdated.collect { isUpdated ->
                isUpdated?.let {
                    Toast.makeText(this@LostDetailsActivity, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        // Observe deletion of Lost object
        lifecycleScope.launch {
            viewModel.isDeleted.collect { isDeleted ->
                isDeleted?.let {
                    if (it) {
                        Toast.makeText(this@LostDetailsActivity, "Case deleted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        // Observe failure messages
        lifecycleScope.launch {
            viewModel.failureMessage.collect { message ->
                message?.let {
                    Toast.makeText(this@LostDetailsActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
