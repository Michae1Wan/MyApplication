package com.michael.myapplication.ui.restaurant

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.michael.myapplication.domain.model.Restaurant
import com.michael.myapplication.ui.common.ErrorScreen
import com.michael.myapplication.ui.common.LoadingScreen
import com.michael.myapplication.util.toTimeString
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RestaurantListScreen() {
    val coroutineScope = rememberCoroutineScope()
    val navigator = rememberListDetailPaneScaffoldNavigator<Restaurant>()
    BackHandler(navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
        }
    }
    val viewModel: RestaurantListViewModel = koinViewModel()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    when {
        viewState.isLoading -> LoadingScreen()
        viewState.showError -> ErrorScreen { viewModel.loadRestaurants() }
        else ->
            ListDetailPaneScaffold(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    AnimatedPane {
                        RestaurantList( viewState.restaurants) { restaurant ->
                            coroutineScope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, restaurant)
                            }
                        }
                    }
                },
                detailPane = {
                    if (navigator.currentDestination?.contentKey != null) {
                        AnimatedPane {
                            RestaurantDetail(navigator.currentDestination?.contentKey!!)
                        }
                    }
                }
            )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RestaurantList(restaurants: List<Restaurant>, onItemClicked: (Restaurant) -> Unit) {
    if (restaurants.isEmpty()) EmptyScreen()
    else {
        Timber.d("Composing restaurant list")
        LazyColumn {
            items(items = restaurants, key = { restaurant -> restaurant.id }) { restaurant ->
                RestaurantCard(restaurant) {
                    onItemClicked(restaurant)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, onItemClicked: (Restaurant) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onItemClicked(restaurant) }
        ,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                ,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(restaurant.imageLink)
                    .crossfade(true)
                    .build(),
                contentDescription = restaurant.name,
                contentScale = ContentScale.FillWidth,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Restaurant image default image")
                    }
                },
            )
            if (restaurant.deals.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Red)
                        .padding(4.dp)
                    ,
                ) {
                    val firstDeal = restaurant.deals.first()
                    val startTime = firstDeal.start?.toTimeString() ?: firstDeal.openTime?.toTimeString()
                    val endTime = firstDeal.end?.toTimeString() ?: firstDeal.closeTime?.toTimeString()

                    Text(
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        text = "${firstDeal.discount}% off${if (firstDeal.dineIn) " - Dine in" else ""}"
                    )
                    Text(
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        text = when {
                            endTime != null -> {
                                if (startTime != null) "From $startTime to $endTime"
                                else "${if (firstDeal.dineIn) "Arrive before" else "Before"} $endTime"
                            }
                            startTime != null -> {
                                "${if (firstDeal.dineIn) "Arrive after" else "After"} $startTime"
                            }
                            else -> "Anytime today"
                        }
                    )
                }
            }
        }

        Text(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.titleLarge,
            text = restaurant.name
        )
        Text(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelSmall,
            text = restaurant.cuisines.joinToString(", ")
        )
    }
}

@Composable
fun RestaurantDetail(restaurant: Restaurant) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ,
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
            ,
            model = ImageRequest.Builder(LocalContext.current)
                .data(restaurant.imageLink)
                .crossfade(true)
                .build(),
            contentDescription = restaurant.name,
            contentScale = ContentScale.FillWidth,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Restaurant image default image")
                }
            },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.headlineLarge,
                text = restaurant.name,
            )
        }
    }
}