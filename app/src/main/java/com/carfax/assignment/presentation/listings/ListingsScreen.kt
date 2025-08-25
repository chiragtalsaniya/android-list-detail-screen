package com.carfax.assignment.presentation.listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carfax.assignment.R
import com.carfax.assignment.domain.model.Vehicle
import com.carfax.assignment.presentation.ui.common.CallDealerButton
import com.carfax.assignment.presentation.ui.util.asMileage
import com.carfax.assignment.presentation.ui.util.asUsd
import com.carfax.assignment.presentation.ui.util.cityState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsRoute(onOpen: (String) -> Unit, vm: ListingsViewModel = koinViewModel()) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.refresh(useRx = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.carfax)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) { inner ->
        ListingsScreen(
            state = state,
            onOpen = onOpen,
            onRetry = { vm.refresh(useRx = true) },
            modifier = Modifier.padding(inner)
        )
    }
}

@Composable
private fun ListingsScreen(
    state: ListingsViewModel.UiState,
    onOpen: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.items.isEmpty() -> Box(
            modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        state.error != null && state.items.isEmpty() -> ErrorView(state.error, onRetry)
        else -> LazyColumn(modifier.fillMaxSize()) {
            items(state.items, key = { it.id }) { v -> VehicleCard(v) { onOpen(v.id) } }
        }
    }
}

@Composable
private fun VehicleCard(v: Vehicle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = CardDefaults.shape
    ) {
        Column {
            // Vehicle photo
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(v.photoUrl).crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_placeholder),
                fallback = painterResource(R.drawable.ic_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )

            // Text block
            Column(Modifier.padding(12.dp)) {
                // Year Make Model Trim
                Text(
                    v.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(6.dp))

                // Price | Mileage
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        v.price.asUsd(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("   |   ", style = MaterialTheme.typography.bodyMedium)
                    Text(v.mileage.asMileage(), style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(6.dp))

                // Location
                Text(
                    cityState(v.city, v.state),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Thin divider (slightly inset like the mock)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

            // CALL DEALER text-style button centered
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CallDealerButton(v.phone)
            }
        }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}

