package com.maritimo.control.ui.certificate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CertificateViewModel @Inject constructor() : ViewModel() {
    fun downloadCertificate(
        context: android.content.Context,
        studentName: String,
        courseName: String,
        certificateId: String
    ) {
        // Stub to satisfy download action in UI
    }
}
