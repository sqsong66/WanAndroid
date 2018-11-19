package com.sqsong.wanandroid.ui.settings.dialog

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import javax.inject.Inject

class LanguageSettingDialog @Inject constructor() : DialogFragment(), View.OnClickListener {

    @Inject
    lateinit var mPreferences: SharedPreferences

    private var englishRb: RadioButton? = null
    private var chineseRb: RadioButton? = null
    private var mListener: OnLanguageChangeListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnLanguageChangeListener) {
            mListener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let { AlertDialog.Builder(it, R.style.DialogStyle) }?.run {
            setTitle(R.string.text_choose_language)
            setView(createDialogView(activity?.layoutInflater))
            setCancelable(false)
            setNegativeButton(R.string.text_cancel, null)
            setPositiveButton(R.string.text_save) { _, _ ->
                val type = if (englishRb?.isChecked == true) Constants.LANGUAGE_TYPE_ENGLISH else Constants.LANGUAGE_TYPE_CHINESE
                mListener?.languageChanged(type)
                dismiss()
            }
            create()
        }
        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

    private fun createDialogView(inflater: LayoutInflater?): View? {
        val view = inflater?.inflate(R.layout.dialog_choose_language, null)
        view?.findViewById<LinearLayout>(R.id.english_ll)?.setOnClickListener(this)
        view?.findViewById<LinearLayout>(R.id.chinese_ll)?.setOnClickListener(this)
        englishRb = view?.findViewById(R.id.english_rb)
        chineseRb = view?.findViewById(R.id.chinese_rb)
        val type = mPreferences[Constants.LANGUAGE_TYPE, 0]
        if (type == Constants.LANGUAGE_TYPE_CHINESE) {
            chineseRb?.isChecked = true
        } else {
            englishRb?.isChecked = true
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.english_ll -> {
                if (englishRb?.isChecked == false) {
                    englishRb?.isChecked = true
                    chineseRb?.isChecked = false
                }
            }
            R.id.chinese_ll -> {
                if (chineseRb?.isChecked == false) {
                    chineseRb?.isChecked = true
                    englishRb?.isChecked = false
                }
            }
        }
    }

    interface OnLanguageChangeListener {
        fun languageChanged(languageType: Int)
    }

}