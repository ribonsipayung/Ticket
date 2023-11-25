package com.ribonsipayung.ticket.ui

import androidx.lifecycle.ViewModel
import com.ribonsipayung.ticket.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Harga untuk satu tiket
private const val PRICE_MOVIE = 50000.00

// Biaya tambahan untuk pemesanan pada hari yang sama
private const val PRICE_FOR_SAME_SHOWTIME = 600000.00

// [OrderViewModel] menyimpan informasi tentang pemesanan tiket dalam hal jumlah, film, dan
// tanggal tayang. Ini juga tahu cara menghitung total harga berdasarkan detail pesanan tersebut.
class OrderViewModel : ViewModel() {

    // Status tiket untuk pemesanan ini
    private val _uiState = MutableStateFlow(OrderUiState(showtimeOptions = showtimeOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    // Atur jumlah tiket [numberTickets] untuk status pesanan ini dan perbarui harga
    fun setQuantity(numberTickets: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberTickets,
                price = calculatePrice(quantity = numberTickets)
            )
        }
    }
    fun setQuantityToOne() {
        _uiState.value = _uiState.value.copy(quantity = 1)
    }

    // Atur tiket film [desiredMovie] untuk status pesanan ini.
    // Hanya 1 film yang dapat dipilih untuk seluruh pesanan.
    fun setMovie(desiredMovie: String) {
        _uiState.update { currentState ->
            currentState.copy(movie = desiredMovie)
        }
    }

    // Atur [showtimeDate] untuk status pesanan ini dan perbarui harga
    fun setDate(showtimeDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = showtimeDate,
                price = calculatePrice(showtimeDate = showtimeDate)
            )
        }
    }

    // Reset status pesanan
    fun resetOrder() {
        _uiState.value = OrderUiState(showtimeOptions = showtimeOptions())
    }

    // Mengembalikan harga yang dihitung berdasarkan detail pesanan.
    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        showtimeDate: String = _uiState.value.date
    ): String {
        var calculatedPrice = quantity * PRICE_MOVIE
        // Jika pengguna memilih opsi pertama (hari ini) untuk tanggal tayang, tambahkan biaya tambahan
        if (showtimeOptions()[0] == showtimeDate) {
            calculatedPrice += PRICE_FOR_SAME_SHOWTIME
        }
        val decimalFormat = DecimalFormat("#.###")
        val formattedPrice = "Rp ${decimalFormat.format(calculatedPrice)}"
        return formattedPrice
    }

    // Mengembalikan daftar opsi tanggal dimulai dari tanggal saat ini dan 3 tanggal berikutnya.
    private fun showtimeOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // Tambahkan tanggal saat ini dan 3 tanggal berikutnya
        repeat(7) {
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }
}