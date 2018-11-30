package com.sqsong.wanandroid.ui.scan.fragment

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.ScanResult
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.SnackbarUtil

class ScanningResultDialog : DialogFragment() {

    private var mScanResult: ScanResult? = null
    private var mDialogListener: ScanResultDialogActionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ScanResultDialogActionListener) {
            mDialogListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScanResult = arguments?.getParcelable(Constants.KEY_SCAN_RESULT)
        if (mScanResult == null) {
            SnackbarUtil.showNormalToast(context!!, "Scan result cannot be null")
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let { AlertDialog.Builder(it, R.style.DialogStyle) }?.run {
            setTitle(getString(R.string.text_scan_result))
            setView(createDialogView(activity?.layoutInflater))
            setCancelable(false)
            setNegativeButton(R.string.text_cancel, null)
            if (mScanResult?.resultText?.startsWith("http") == true) {
                setNeutralButton(R.string.text_open_browser) { _, _ ->
                    mDialogListener?.openBrowser(mScanResult?.resultText)
                    dismiss()
                }
            }
            setPositiveButton(R.string.text_copy) { _, _ ->
                startPasteToClipboard(mScanResult?.resultText)
                dismiss()
            }
            create()
        }
        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

    private fun startPasteToClipboard(resultText: String?) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(resultText, resultText)
        clipboard.primaryClip = clipData
        SnackbarUtil.showToastText(context, getString(R.string.text_copy_success))
    }

    private fun createDialogView(inflater: LayoutInflater?): View? {
        val view = inflater?.inflate(R.layout.dialog_scanning_result, null)
        val bmpImage = view?.findViewById<ImageView>(R.id.bitmap_image)
        val resultTv = view?.findViewById<TextView>(R.id.result_tv)
        val formatTv = view?.findViewById<TextView>(R.id.format_tv)
        val typeTv = view?.findViewById<TextView>(R.id.type_tv)
        val timeTv = view?.findViewById<TextView>(R.id.time_tv)
        val byteArray = mScanResult?.bitmapByteArray
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            bmpImage?.setImageBitmap(bitmap)
        } else {
            bmpImage?.setImageResource(R.drawable.placeholder)
        }
        resultTv?.text = mScanResult?.resultText
        formatTv?.text = mScanResult?.format
        typeTv?.text = mScanResult?.type
        timeTv?.text = mScanResult?.time
        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        mDialogListener?.onDialogDismiss()
    }

    interface ScanResultDialogActionListener {
        fun onDialogDismiss()
        fun openBrowser(url: String?)
    }

    companion object {
        fun newInstance(result: ScanResult): ScanningResultDialog {
            val dialog = ScanningResultDialog()
            val bundle = Bundle()
            bundle.putParcelable(Constants.KEY_SCAN_RESULT, result)
            dialog.arguments = bundle
            return dialog
        }
    }

}