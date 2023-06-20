@file:Suppress("DEPRECATION")

package sk.upjs.wordsup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.wordinfo.WordsDefinitionViewModel
import sk.upjs.wordsup.fragments.LoadingFragment
import sk.upjs.wordsup.fragments.WordFragment

@AndroidEntryPoint
class WordActivity : AppCompatActivity() {

    private lateinit var word: Word


    private val viewModel: WordsDefinitionViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)
        val loadingFragment = LoadingFragment.newInstance()
        openFragment(loadingFragment)
        word = intent.getSerializableExtra("word") as Word
        viewModel.getWordsInfos(listOf(word))
        viewModel.wordsInfos.observe(this) {
            openFragment(WordFragment.newInstance(word, it[0]))
            closeFragment(loadingFragment)
        }
    }

    private fun openFragment(fragment: Fragment?) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment!!)
            transaction.addToBackStack(null)
            transaction.commit()
    }
    private fun closeFragment(fragment: Fragment?){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment!!).commit()
    }
}