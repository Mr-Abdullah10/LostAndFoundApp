package com.abdullah.lostfound.ui.main.lost

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.abdullah.lostfound.Repositories.AuthRepository
import com.abdullah.lostfound.databinding.ActivityLostDetailsBinding
import com.abdullah.lostfound.ui.dataclasses.Lost
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

        // Set visibility for buttons
        val user: FirebaseUser = AuthRepository().getCurrentUser()!!
        val isAdmin = user.email == "abdullahahsan438@gmail.com"

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
            // Call deleteLost function in ViewModel
            viewModel.deleteCase(lost.id)
        }

        // Observe updates to Lost object
        lifecycleScope.launch {
            viewModel.isUpdated.collect { isUpdated ->
                isUpdated?.let {
                    if (lost.status == "Item Claimed") {
                        // Uncommented code for sending notifications
                        // NotificationRepository().sendNotification(
                        //     lost.userId,
                        //     "Item Claimed",
                        //     "Your item has been claimed by ${user.displayName}",
                        //     this@LostDetailsActivity
                        // )
                    }
                    if (lost.status == "Delivered") {
                        // Uncommented code for sending notifications
                        // NotificationRepository().sendNotification(
                        //     lost.userId,
                        //     "Item Returned",
                        //     "Your lost of ${lost.item?.title} has been returned. You will receive it soon at your address.",
                        //     this@LostDetailsActivity
                        // )
                    }
                    if (lost.status == "Item Received") {
                        // Uncommented code for sending notifications
                        // NotificationRepository().sendNotification(
                        //     MainActivity.adminUid,
                        //     "Item Received",
                        //     "${lost.userName} has received the lost ${lost.item?.title}.",
                        //     this@LostDetailsActivity
                        // )
                    }
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
                        finish() // Close activity after deletion
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
