package ru.netology.nmedia.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import ru.netology.nmedia.R
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.viewmodel.AuthViewModel

class AppActivity : AppCompatActivity(R.layout.activity_app) {

    private val container by lazy { DependencyContainer.getInstance(application) }

    private val viewModel: AuthViewModel by viewModels(
        factoryProducer = {
            container.viewModelFactory
        })

    private val appAuth by lazy { container.appAuth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkGoogleApiAvailability()

        viewModel.data.observe(this) {
            invalidateOptionsMenu()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menu?.let {
            it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
            it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.signIn -> {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_postsFragment_to_authorizationFragment
                )
                true
            }
            R.id.signUp -> {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_postsFragment_to_registrationFragment
                )
                true
            }
            R.id.signOut -> {
                appAuth.removeAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 900).show()
                return
            }
        }
    }

}

