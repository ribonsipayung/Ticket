package com.ribonsipayung.ticket.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ribonsipayung.ticket.R
import com.ribonsipayung.ticket.data.OrderUiState
import com.ribonsipayung.ticket.ui.components.FormattedPriceLabel
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * [OrderSummaryScreen] menunjukkan rincian pesanan dan mengharapkan
 * [orderUiState] yang mewakili status pesanan, [onCancelButtonClicked]
 * lambda yang memicu pembatalan pesanan, dan [onSendButtonClicked] lambda
 * yang akan menangani pengiriman pesanan.
 */
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: OrderViewModel = viewModel()
    // Mendapatkan sumber daya dari konteks lokal
    val resources = LocalContext.current.resources

    // Mendapatkan string untuk menampilkan jumlah tiket yang dipilih
    val numberOfTickets = resources.getQuantityString(
        R.plurals.tickets,
        orderUiState.quantity,
        orderUiState.quantity
    )
    // Memuat dan memformat string sumber daya dengan parameter
    val orderSummary = stringResource(
        R.string.order_details,
        numberOfTickets,
        orderUiState.movie,
        orderUiState.date,
        orderUiState.quantity
    )
    val newOrder = stringResource(R.string.new_ticket_order)
    // Membuat daftar item pesanan untuk ditampilkan
    val items = listOf(
        // Baris ringkasan 1: menampilkan jumlah tiket yang dipilih
        Pair(stringResource(R.string.quantity), if (orderUiState.quantity == 3) "1" else numberOfTickets),
        // Baris ringkasan 2: menampilkan film yang dipilih
        Pair(stringResource(R.string.movie), orderUiState.movie),
        // Baris ringkasan 3: menampilkan waktu tayang yang dipilih
        Pair(stringResource(R.string.showtime), orderUiState.date)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Bagian rincian pesanan
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            // Menampilkan label dan nilai untuk setiap item pesanan
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                // Menampilkan garis pembatas antar item pesanan
                Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            }
            // Spasi sebelum menampilkan harga
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            // Menampilkan harga dengan format yang sudah ditentukan
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier
                .weight(1f, false)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                // Tombol Kirim Pesanan
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Mengatur quantity menjadi 1 sebelum mengirim pesanan
                        viewModel.setQuantityToOne()
                        // Memanggil fungsi onSendButtonClicked
                        onSendButtonClicked(newOrder, orderSummary)
                    }
                ) {
                    Text(stringResource(R.string.send))
                }
                // Tombol Batalkan Pesanan
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

// Preview untuk melihat tampilan OrderSummaryScreen
@Preview
@Composable
fun OrderSummaryPreview(){
    // Menampilkan OrderSummaryScreen dalam mode preview
    OrderSummaryScreen(
        orderUiState = OrderUiState(0, "Test", "Test", "$300.00"),
        onSendButtonClicked = { subject: String, summary: String -> },
        onCancelButtonClicked = {},
        modifier = Modifier.fillMaxHeight()
    )
}