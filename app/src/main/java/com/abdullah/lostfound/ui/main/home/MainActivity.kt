package com.abdullah.lostfound.ui.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.abdullah.lostfound.AboutUsActivity
import com.abdullah.lostfound.dataSource.CloudinaryUploadHelper.Companion.initializeCloudinary
import com.abdullah.lostfound.R
import com.abdullah.lostfound.ui.Auth.AuthViewModel
import com.abdullah.lostfound.ui.Auth.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        var adminUid = "YIIQGVn8hoVVcJ7eiZaH21ADEyz1"
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeCloudinary(this)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val imageView = findViewById<ImageView>(R.id.drawer_icon)
        imageView.setOnClickListener {
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setupWithNavController(navHostFragment.navController)

        observeCurrentUser()
        askNotificationPermission()
    }

    private fun observeCurrentUser() {
        lifecycleScope.launchWhenStarted {
            viewModel.currentUser.collect { user ->
                if (user == null) {
                    // User logged out, redirect to login screen
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Populate navigation drawer with user data
                    val navigationView = findViewById<NavigationView>(R.id.nav_view)
                    val headerView = navigationView.getHeaderView(0)
                    // Example: Populate nav header with user data
                    // headerView.findViewById<TextView>(R.id.userName).text = user.displayName
                    // headerView.findViewById<TextView>(R.id.userEmail).text = user.email
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val viewModel: AuthViewModel by viewModels()
        when (item.itemId) {
            R.id.item_logout -> {
                lifecycleScope.launch { // Launch a coroutine
                    viewModel.Logout()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
            R.id.item_about_us -> {
                startActivity(Intent(this, AboutUsActivity::class.java))
                // Handle About Us logic
            }
        }
        return true
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                } else {
                    Log.d("FCM", "FCM token: ${task.result}")
                }
            }
        }
    }
}