package sk.upjs.wordsup


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizViewModel
import sk.upjs.wordsup.fragments.HomeFragment
import sk.upjs.wordsup.fragments.LearnFragment
import sk.upjs.wordsup.fragments.QuizFragment
import sk.upjs.wordsup.fragments.SettingsFragment
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    private val  viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigation = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragment.newInstance())
                    true
                }
                R.id.learn -> {
                    openFragment(LearnFragment.newInstance("", ""))
                    true
                }
                R.id.quiz -> {
                    openFragment(QuizFragment.newInstance())
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


        openFragment(HomeFragment.newInstance());
    }

    fun openFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}