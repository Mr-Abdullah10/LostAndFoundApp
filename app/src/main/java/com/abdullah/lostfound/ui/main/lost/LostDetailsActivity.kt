package com.abdullah.lostfound.ui.main.lost


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.abdullah.lostfound.Repositories.AuthRepository
import com.abdullah.lostfound.Repositories.NotificationRepository
import com.abdullah.lostfound.databinding.ActivityLostDetailsBinding
import com.abdullah.lostfound.ui.dataclasses.Lost
import com.abdullah.lostfound.ui.main.home.MainActivity

import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LostDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityLostDetailsBinding;
    lateinit var lost: Lost
    lateinit var viewModel: LostDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LostDetailsViewModel()
        lost = Gson().fromJson(intent.getStringExtra("data"), Lost::class.java)

        val userId = binding.userId.text.toString()
        val postDate = binding.postDate.text.toString()
        val item = binding.item.text.toString()
        val extraInformation = binding.extraInfromation.text.toString()
        val postalAddress = binding.postalAddress.text.toString()
        val userName = binding.userName.text.toString()
        val userEmail = binding.userEmail.text.toString()
        val userContact = binding.userContact.text.toString()
        val status = binding.status.text.toString()

        val user: FirebaseUser = AuthRepository().getCurrentUser()!!
        var isAdmin = false
        if (user.email.equals("abdullahahsan438@gmail.com"))
            isAdmin = true

        if (!lost.status.equals("Item Placed") || !isAdmin)
            binding.confirmPost.visibility = View.GONE

        if (!lost.status.equals("Item Claimed") || !isAdmin)
            binding.claimItem.visibility = View.GONE

        if (!lost.status.equals("Delivered") || isAdmin)
            binding.confirmItemReceived.visibility = View.GONE

        binding.confirmPost.setOnClickListener {
            lost.status = "Item Claimed"
            viewModel.updateLost(lost)
        }
        binding.claimItem.setOnClickListener {
            lost.status = "Delivered"
            viewModel.updateLost(lost)
        }
        binding.confirmItemReceived.setOnClickListener {
            lost.status = "Item Received"
            viewModel.updateLost(lost)
        }

        lifecycleScope.launch {
            viewModel.isUpdated.collect {
                it?.let {
                    if (lost.status.equals("Item Claimed")) {
                        NotificationRepository().sendNotification(lost.userId, "Item Claimed", "Your item has been claimed by ${user.displayName}", this@LostDetailsActivity)                    }
                    if (lost.status.equals("Delivered")) {
                        NotificationRepository().sendNotification(lost.userId, "Item Returned", "Your lost of ${lost.item?.title} has been returned, you will receive it soon at your address", this@LostDetailsActivity)
                    }
                    if (lost.status.equals("Item Received")) {
                        NotificationRepository().sendNotification(MainActivity.adminUid, "Item Received", "${lost.userName} has received the lost ${lost.item?.title}.", this@LostDetailsActivity)
                    }
                    Toast.makeText(this@LostDetailsActivity, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(this@LostDetailsActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}