package com.carfax.assignment.presentation.details

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.carfax.assignment.R
import com.carfax.assignment.presentation.ui.common.CallButtonStyle
import com.carfax.assignment.presentation.ui.common.CallDealerButton
import com.carfax.assignment.presentation.ui.util.asMileage
import com.carfax.assignment.presentation.ui.util.asUsd
import com.carfax.assignment.presentation.ui.util.cityState
import org.koin.androidx.compose.koinViewModel

@Composable
fun VehicleDetailRoute(
    id: String, onBack: () -> Unit, vm: VehicleDetailViewModel = koinViewModel()
) {
    val state by vm.state.collectAsState()
    LaunchedEffect(id) { vm.load(id) }
    VehicleDetailScreen(state, onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VehicleDetailScreen(
    state: VehicleDetailViewModel.UiState, onBack: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                })
        }) { paddind ->
        state.item?.let { v ->
            Column(
                modifier = Modifier
                    .padding(paddind)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = v.photoUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    error = painterResource(R.drawable.ic_placeholder),
                    fallback = painterResource(R.drawable.ic_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )

                // Title
                Text(
                    v.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                // Price | Mileage (bold)
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        v.price.asUsd(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("  |  ", style = MaterialTheme.typography.titleLarge)
                    Text(
                        v.mileage.asMileage(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Section header
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(Modifier.padding(horizontal = 12.dp))
                Text(
                    stringResource(R.string.vehicle_info),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                )

                val na = stringResource(R.string.value_not_available)

                InfoRow(
                    label = stringResource(R.string.label_location),
                    value = cityState(v.city, v.state)
                )
                InfoRow(
                    label = stringResource(R.string.label_exterior_color),
                    value = v.exteriorColor ?: na
                )
                InfoRow(
                    label = stringResource(R.string.label_interior_color),
                    value = v.interiorColor ?: na
                )
                InfoRow(
                    label = stringResource(R.string.label_drive_type),
                    value = v.driveType ?: na
                )
                InfoRow(
                    label = stringResource(R.string.label_transmission),
                    value = v.transmission ?: na
                )
                InfoRow(
                    label = stringResource(R.string.label_body_style),
                    value = v.bodyStyle ?: na
                )
                InfoRow(label = stringResource(R.string.label_engine), value = v.engine ?: na)
                // If you later add 'fuel' to the model, just uncomment:
                // InfoRow(label = "Fuel", value = v.fuel ?: "—")

                // Thin shadow-like divider before the CTA (approximate the mock drop shadow)
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(Modifier.padding(horizontal = 8.dp))

                // Full-width CALL DEALER button
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CallDealerButton(
                        phone = v.phone,
                        style = CallButtonStyle.Filled,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
        } ?: Box(
            Modifier
                .fillMaxSize()
                .padding(paddind), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Left column (grey label)
        Text(
            label,
            modifier = Modifier.width(140.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // Right column (value)
        Text(
            value, style = MaterialTheme.typography.bodyMedium
        )
    }
}
