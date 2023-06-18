package sk.upjs.wordsup


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.quiz.Quiz
import sk.upjs.wordsup.dao.quiz.QuizViewModel
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.fragments.HomeFragment
import sk.upjs.wordsup.fragments.LearnFragment
import sk.upjs.wordsup.fragments.QuizFragment
import sk.upjs.wordsup.fragments.SettingsFragment
import sk.upjs.wordsup.rest.RestApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycleScope.launch {
//            RestApi.wordsRestDao.getDictionaryData("baba")
//        }


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

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.last() is HomeFragment) {
            finish()
        } else {
            super.onBackPressed()
        }



        when (supportFragmentManager.fragments.last()) {
            is HomeFragment -> bottomNavigation.selectedItemId = R.id.home
            is LearnFragment -> bottomNavigation.selectedItemId = R.id.learn
            is QuizFragment -> bottomNavigation.selectedItemId = R.id.quiz
            is SettingsFragment -> bottomNavigation.selectedItemId = R.id.settings
        }
    }

}