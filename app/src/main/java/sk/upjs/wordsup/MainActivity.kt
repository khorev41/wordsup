package sk.upjs.wordsup


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.fragments.HistoryFragment
import sk.upjs.wordsup.fragments.HomeFragment
import sk.upjs.wordsup.fragments.LearnFragment
import sk.upjs.wordsup.fragments.SettingsFragment
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Prefs.getInstance(applicationContext).date != SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date()
            )){
            Prefs.getInstance(applicationContext).learned = 0;
        }

        bottomNavigation = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            val initialFragment = HomeFragment()

            supportFragmentManager.commit {
                add(R.id.fragment_container, initialFragment, HomeFragment::javaClass.name)
            }
            viewModel.activeFragmentTag = HomeFragment::javaClass.name
        } else {
            viewModel.activeFragmentTag = savedInstanceState.getString("active_fragment_tag")
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.learn -> {
                    openFragment(LearnFragment())
                    true
                }
                R.id.history -> {
                    openFragment(HistoryFragment())
                    true
                }
                R.id.settings -> {
                    openFragment(SettingsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    fun openFragment(fragment: Fragment) {
        var tag = fragment.javaClass.name
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)


        if (existingFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, existingFragment, tag)
                .commit()
        } else {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, fragment, tag)
                addToBackStack(tag)
            }
        }
        viewModel.activeFragmentTag = tag
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("active_fragment_tag", viewModel.activeFragmentTag)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.last() is HomeFragment) {
            finish()
        } else {
            try {
                super.onBackPressed()
            }catch (e: java.lang.Exception){

            }

        }

        when (supportFragmentManager.fragments.last()) {
            is HomeFragment -> bottomNavigation.selectedItemId = R.id.home
            is LearnFragment -> bottomNavigation.selectedItemId = R.id.learn
            is HistoryFragment -> bottomNavigation.selectedItemId = R.id.history
            is SettingsFragment -> bottomNavigation.selectedItemId = R.id.settings
        }
    }

}