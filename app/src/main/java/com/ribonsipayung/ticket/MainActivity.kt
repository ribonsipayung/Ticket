package com.ribonsipayung.ticket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.ribonsipayung.ticket.ui.theme.TicketTheme

/**
 * [MainActivity] adalah kelas utama yang mewarisi [ComponentActivity].
 * Digunakan untuk menangani pembuatan aktivitas dan menetapkan tata letak konten aplikasi.
 */
class MainActivity : ComponentActivity() {
    /**
     * Metode yang dipanggil saat aktivitas dibuat. Di sini, kita menetapkan tata letak konten aplikasi
     * menggunakan Compose dan menerapkan tema [TicketTheme].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengatur perilaku jendela agar tidak memasukkan konten di bawah bilah sistem
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Menetapkan tata letak konten aplikasi menggunakan Compose
        setContent {
            TicketTheme {
                TicketApp()
            }
        }
    }
}