package com.abdullah.lostfound.ui.main.lost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.abdullah.lostfound.databinding.ActivityLostCaseBinding
import com.abdullah.lostfound.ui.dataclasses.Lost
import kotlinx.coroutines.launch

class LostCaseActivity : AppCompatActivity() {
    private var uri: Uri? = null
    private lateinit var binding: ActivityLostCaseBinding
    private lateinit var viewModel: LostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LostViewModel()

        // Initialize Spinners
        setupPostTypeSpinner() // Spinner for "Lost" or "Found"
        setupCategorySpinner() // Spinner for "Category"

        // Observe ViewModel for save success
        lifecycleScope.launch {
            viewModel.isSuccessfullySaved.collect {
                it?.let {
                    if (it) {
                        Toast.makeText(this@LostCaseActivity, "Successfully saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        // Observe ViewModel for failure messages
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(this@LostCaseActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Handle form submission
        binding.btnCreateCase.setOnClickListener {
            handleFormSubmission()
        }

        binding.imageView2.setOnClickListener {
            chooseImageFromGallery()
        }
    }

    private fun setupPostTypeSpinner() {
        // Spinner values for "Lost" or "Found"
        val postTypes = listOf("Lost", "Found")

        // Set up the ArrayAdapter
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            postTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Attach the adapter to the Spinner
        binding.spinnerPostType.adapter = adapter
    }

    private fun setupCategorySpinner() {
        // Spinner values for "Category"
        val categories = listOf("Electronics", "Clothing", "Documents", "Other")

        // Set up the ArrayAdapter
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Attach the adapter to the Spinner
        binding.categoryinput.adapter = adapter
    }



    private fun handleFormSubmission() {
        val name = binding.nameinput.text.toString().trim()
        val description = binding.descriptioninput.text.toString().trim()
        val address = binding.addressinput.text.toString().trim()
        val contact = binding.contactinput.text.toString().trim()
        val category = binding.categoryinput.selectedItem?.toString()?.trim() ?: ""
        val postDate = binding.postDate.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val postType = binding.spinnerPostType.selectedItem?.toString()?.trim() ?: ""
        val status = "Pending"
       // val image = binding.upload.toString().trim()

        // Validate the input fields
        if (name.isEmpty() || description.isEmpty() || address.isEmpty() || contact.isEmpty()
             || postDate.isEmpty() || email.isEmpty() || email.isEmpty() || category.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a Lost object
        val lost = Lost()
        lost.name = name
        lost.description = description
        lost.address = address
        lost.contact = contact
        lost.category = category
        lost.status = status
        lost.postDate = postDate
        lost.email = email
        lost.isLost = postType == "Lost" // Determine if it is lost based on Spinner selection
        lost.isFound = postType == "Found"
        //lost.image = image



            if (uri == null)
            viewModel.saveLost(lost)
        else
            viewModel.uploadImageAndSaveLost(getRealPathFromURI(uri!!)!!, lost)

        // Save the Handcraft object (this would be a database operation, Firestore, etc.)
        // For now, just display the success message
        Toast.makeText(this, "Post Save Successfully!", Toast.LENGTH_SHORT).show()



}

private fun chooseImageFromGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    galleryLauncher.launch(intent)
}

private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result: ActivityResult ->
    if (result.resultCode == Activity.RESULT_OK) {
        uri = result.data?.data
        if (uri != null) {
            binding.imageView2.setImageURI(uri)
        } else {
            Log.e("Gallery", "No image selected")
        }
    }
}

private fun getRealPathFromURI(uri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (cursor.moveToFirst()) {
            return cursor.getString(columnIndex)
        }
    }
    return null
}}

