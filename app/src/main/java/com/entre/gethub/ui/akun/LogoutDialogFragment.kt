package com.entre.gethub.ui.akun

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LogoutDialogFragment(private val onLogout: () -> Unit) :
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Keluar")
                .setMessage("Apakah Anda yakin untuk keluar?")
                .setPositiveButton("Keluar") { dialog, _ ->
                    onLogout()
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialog
            .dismiss()
    }
}