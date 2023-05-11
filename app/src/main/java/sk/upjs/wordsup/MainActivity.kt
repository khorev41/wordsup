package sk.upjs.wordsup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import sk.upjs.wordsup.fragments.HomeFragment
import sk.upjs.wordsup.fragments.LearnFragment
import sk.upjs.wordsup.fragments.QuizFragment
import sk.upjs.wordsup.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragment.newInstance("", ""))
                    true
                }
                R.id.learn -> {
                    openFragment(LearnFragment.newInstance("", ""))
                    true
                }
                R.id.quiz -> {
                    openFragment(QuizFragment.newInstance("", ""))
                    true
                }
                R.id.settings -> {
                    openFragment(SettingsFragment.newInstance("", ""))
                    true
                }
                else -> {
                    false
                }

            }
        }

        openFragment(HomeFragment.newInstance("", ""));
    }

    fun openFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}