package com.ribonsipayung.ticket.data


/**
 * Kelas data yang merepresentasikan status UI saat ini dalam hal [quantity], [movie],
 * [dateOptions], showtime terpilih [date], dan [price]
 */
data class OrderUiState(
    /** Jumlah tiket yang dipilih (1, 6, 12) */
    val quantity: Int = 0,
    /** Film dari tiket dalam pesanan (seperti "Budi Pekerti", "SIJJIN", dll.) */
    val movie: String = "",
    /** Tanggal terpilih untuk pertunjukan (seperti "1 Jan") */
    val date: String = "",
    /** Harga total untuk pesanan */
    val price: String = "",
    /** Opsi tanggal pertunjukan yang tersedia untuk pesanan */
    val showtimeOptions: List<String> = listOf(),
)
