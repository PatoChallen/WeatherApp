package com.patofernandez.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.patofernandez.weatherapp.databinding.MainActivityBinding
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate")
        when(Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
            in 7..17 -> setTheme(R.style.AppTheme)
            in 18..20 -> setTheme(R.style.AfternoonAppTheme)
            else ->  setTheme(R.style.NightAppTheme)
        }
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        binding.background = when(Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
            in 7..17 -> R.color.colorPrimary
            in 18..20 -> R.color.colorAccent
            else -> R.color.colorPrimaryDark
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    companion object {
        const val TAG = "MainActivity"
    }

}
