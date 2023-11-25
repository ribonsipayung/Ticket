package com.ribonsipayung.ticket.data

import com.ribonsipayung.ticket.R

// Objek DataSource berfungsi sebagai sumber data statis untuk daftar film dan opsi jumlah tiket.
object DataSource {
    // Daftar film
    val movies = listOf(
        R.string.gampang_cuan,
        R.string.sijjin,
        R.string.days,
        R.string.srimulat,
        R.string.rumah_iblis,
        R.string.budi_pekerti,
        R.string.the_hunger,
    )

    // Opsi kelas tiket
    val quantityOptions = listOf(
        Pair(R.string.regular, 1),
        Pair(R.string.vip, 3),
    )
}
