package sk.upjs.wordsup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sk.upjs.wordsup.R


class LoadingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = LoadingFragment().apply {

        }
    }
}