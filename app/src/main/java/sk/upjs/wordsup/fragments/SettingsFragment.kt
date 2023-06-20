package sk.upjs.wordsup.fragments

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.GreetingsActivity
import sk.upjs.wordsup.prefs.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.prefs.PrefsViewModel
import java.util.*

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: PrefsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //https://stackoverflow.com/questions/12711418/change-preference-screen-background-color
        requireView().setBackgroundColor(resources.getColor(R.color.primary, resources.newTheme()))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val namePreference = findPreference<EditTextPreference>("name_string")
        namePreference?.summary = Prefs.getInstance(requireContext()).name
        namePreference?.text = Prefs.getInstance(requireContext()).name

        namePreference?.setOnPreferenceChangeListener { _, newValue ->
            val s = newValue.toString().replace(Regex("\\s+"), " ").trim()
                .lowercase(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            Prefs.getInstance(requireContext()).name = s
            namePreference.summary = s
            namePreference.text = s
            //TODO
            true
        }

        val targetPreference = findPreference<EditTextPreference>("target_string")
        targetPreference?.summary = Prefs.getInstance(requireContext()).target.toString()
        targetPreference?.text = Prefs.getInstance(requireContext()).target.toString()

        targetPreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }

        targetPreference?.setOnPreferenceChangeListener { _, newValue ->
            val n = newValue.toString().replace(" ", "").trim().toInt()
            Prefs.getInstance(requireContext()).target = n
            targetPreference.summary = n.toString()
            targetPreference.text = n.toString()
            true
        }

        val deleteDataPreference = findPreference<Preference>("delete_data")
        deleteDataPreference?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.data_clear))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    // Respond to negative button press
                }.setPositiveButton(getString(R.string.yes)) { _, _ ->

                    viewModel.clearDatabase()
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().clear()
                        .apply()

                    val intent = Intent(it.context, GreetingsActivity::class.java)
                    it.context.startActivity(intent)
                    requireActivity().finish()
                }.show()
            true
        }
    }
}
