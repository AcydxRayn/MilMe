package com.ks49.milme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ks49.milme.data.Result
import com.ks49.milme.databinding.ActivityMainBinding
import com.ks49.milme.util.AuthManager
import com.ks49.milme.util.snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authManager: AuthManager
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)



        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        authManager = AuthManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        authManager.handleAuthentication(requestCode, resultCode, data) { isSuccessful, error ->
            if (isSuccessful) {
                reloadNavigation()
            } else {
                binding.navView.snackbar(
                    getString(
                        R.string.prompt_failed_to_login,
                        error?.localizedMessage ?: "Unknown Error"
                    )
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!authManager.isUserLoggedIn) {
            authManager.authUser()
        }
    }

    private fun signOut() {
        authManager.signOut().observe(this, Observer {
            it?.let { result ->
                when (result) {
                    is Result.Success -> reloadNavigation()
                    is Result.Error -> {
                        binding.root.snackbar(result.exception.localizedMessage ?: "Unknown Error")
                    }
                    is Result.Loading -> { /* Cancel your subscription to Do-nothing club.*/
                    }
                }
            }
        })
    }

    private fun reloadNavigation() {
        navController.navigate(R.id.navigation_home)
    }
}
