package com.ribonsipayung.ticket

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ribonsipayung.ticket.data.DataSource
import com.ribonsipayung.ticket.data.OrderUiState
import com.ribonsipayung.ticket.ui.OrderSummaryScreen
import com.ribonsipayung.ticket.ui.OrderViewModel
import com.ribonsipayung.ticket.ui.SelectOptionScreen
import com.ribonsipayung.ticket.ui.StartOrderScreen

/**
 * Enum yang merepresentasikan layar-layar dalam aplikasi
 */
enum class ticketScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Movie(title = R.string.choose_movie),
    Showtime(title = R.string.choose_showtime),
    Summary(title = R.string.order_summary)
}

/**
 * Komponen yang menampilkan AppBar dan tombol kembali jika navigasi kembali dimungkinkan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ticketAppBar(
    currentScreen: ticketScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Membuat AppBar dengan judul dan tombol kembali jika diperlukan
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                // Menampilkan tombol kembali jika navigasi kembali dimungkinkan
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun TicketApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Dapatkan entri back stack saat ini
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Dapatkan nama layar saat ini
    val currentScreen = ticketScreen.valueOf(
        backStackEntry?.destination?.route ?: ticketScreen.Start.name
    )

    // Scaffold adalah tata letak utama aplikasi yang menyediakan AppBar dan konten utama
    Scaffold(
        topBar = {
            // Menampilkan AppBar di bagian atas
            ticketAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        // Mengambil status UI dari ViewModel sebagai state
        val uiState by viewModel.uiState.collectAsState()

        // NavHost mengelola navigasi antara komposisi berdasarkan route dan back stack
        NavHost(
            navController = navController,
            startDestination = ticketScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Setiap layar diimplementasikan sebagai composable dalam NavHost
            composable(route = ticketScreen.Start.name) {
                // Menampilkan layar awal untuk memulai pesanan
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        // Menetapkan jumlah yang dipilih dan berpindah ke layar berikutnya
                        viewModel.setQuantity(it)
                        navController.navigate(ticketScreen.Movie.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = ticketScreen.Movie.name) {
                val context = LocalContext.current
                // Menampilkan layar pemilihan film
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(ticketScreen.Showtime.name) },
                    onCancelButtonClicked = {
                        // Membatalkan pesanan dan kembali ke layar awal
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = DataSource.movies.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setMovie(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = ticketScreen.Showtime.name) {
                // Menampilkan layar pemilihan waktu pertunjukan
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(ticketScreen.Summary.name) },
                    onCancelButtonClicked = {
                        // Membatalkan pesanan dan kembali ke layar awal
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.showtimeOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = ticketScreen.Summary.name) {
                val context = LocalContext.current
                // Menampilkan ringkasan pesanan
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        // Membatalkan pesanan dan kembali ke layar awal
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        // Berbagi rincian pesanan
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

/**
 * Mengatur ulang [OrderUiState] dan kembali ke [TicketScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(ticketScreen.Start.name, inclusive = false)
}

/**
 * Membuat intent untuk berbagi rincian pesanan
 */
private fun shareOrder(context: Context, subject: String, summary: String) {
    // Membuat intent implisit ACTION_SEND dengan rincian pesanan di dalam intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    // Menampilkan dialog aplikasi untuk berbagi pesanan
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_ticket_order)
        )
    )
}