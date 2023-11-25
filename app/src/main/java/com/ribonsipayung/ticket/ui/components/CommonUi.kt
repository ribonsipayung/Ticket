package com.ribonsipayung.ticket.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ribonsipayung.ticket.R

/**
 * Komponen yang menampilkan [price] yang diformat dan ditampilkan di layar
 */
@Composable
fun FormattedPriceLabel(subtotal: String, modifier: Modifier = Modifier) {
    // Menampilkan teks dengan format tertentu menggunakan string resource
    Text(
        text = stringResource(R.string.subtotal_price, subtotal),
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}