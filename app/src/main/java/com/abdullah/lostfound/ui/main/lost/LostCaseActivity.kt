package com.abdullah.lostfound.ui.main.lost



import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
    lateinit var binding: ActivityLostCaseBinding;
    lateinit var viewModel: LostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LostViewModel()

        lifecycleScope.launch {
            viewModel.isSuccessfullySaved.collect {
                it?.let {
                    if (it == true) {
                        Toast.makeText(
                            this@LostCaseActivity,
                            "Successfully saved",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(this@LostCaseActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCreateCase.setOnClickListener {
            val name = binding.nameinput.text.toString().trim()
            val description = binding.descriptioninput.text.toString().trim()
            val address = binding.addressinput.text.toString().trim()
            val contact = binding.contactinput.text.toString().trim()
            val category = binding.categoryinput.selectedItem?.toString()?.trim() ?: ""
            val userId = binding.userId.text.toString().trim()
            val postDate = binding.postDate.text.toString().trim()
            val image = binding.imageView2.toString().trim()
            val email = binding.email.text.toString().trim()
            val userName = binding.nameinput.text.toString().trim()
            val status = "Pending"
            val isLost = binding.radioLost.isChecked
            val isFound = binding.radioFound.isChecked



            // Validate the input fields
            if (name.isEmpty() || description.isEmpty() || address.isEmpty() || contact.isEmpty()
                || userId.isEmpty() || postDate.isEmpty() || image.isEmpty() || email.isEmpty() || userName.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a Lost object
            val lost = Lost().apply {
                this.name = name
                this.description = description
                this.address = address
                this.contact = contact
                this.category = category
                this.status = status
                this.userId = userId
                this.postDate = postDate
                this.image = image
                this.email = email
                this.userName = userName
                this.isLost = isLost
                this.isFound = isFound

            }

            // Save the lost item
            if (uri == null) {
                viewModel.saveLost(lost)
            } else {
                val realPath = getRealPathFromURI(uri!!)
                if (realPath != null) {
                    viewModel.uploadImageAndSaveLost(realPath, lost)
                } else {
                    Toast.makeText(this, "Unable to resolve the image path", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.imageView2.setOnClickListener {
            chooseImageFromGallery()
        }

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
    }
}