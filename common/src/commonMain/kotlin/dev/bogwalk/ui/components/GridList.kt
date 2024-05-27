package dev.bogwalk.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import dev.bogwalk.models.GridScrollPosition
import dev.bogwalk.ui.style.cardSize
import dev.bogwalk.ui.style.mediumDp

@Composable
internal fun GridList(
    inUseDb: List<Int>,
    state: GridScrollPosition,
    onRememberScrollState: (GridScrollPosition) -> Unit,
    onDBSelected: (Int) -> Unit
) {
    val scrollState = rememberLazyGridState(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset)

    DisposableEffect(key1 = null) {
        onDispose {
            onRememberScrollState(
                GridScrollPosition(scrollState.firstVisibleItemIndex, scrollState.firstVisibleItemScrollOffset)
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            state = scrollState,
            contentPadding = PaddingValues(mediumDp),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            items(
                count = 47,
                key = { it }
            ) { i ->
                HangarCard(i + 1, (i + 1) in inUseDb) { onDBSelected(i + 1) }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun HangarCard(
    dbID: Int,
    isInUse: Boolean,
    onDBSelected: (Int) -> Unit
) {
    var isInFocus by remember { mutableStateOf(false) }

    ElevatedCard(
        onClick = { onDBSelected(dbID) },
        modifier = Modifier
            .requiredSize(cardSize)
            .padding(mediumDp)
            .border(4.dp, if (isInUse) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.large)
            .onPointerEvent(PointerEventType.Enter) { isInFocus = true }
            .onPointerEvent(PointerEventType.Exit) { isInFocus = false },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            contentColor = if (isInUse) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isInUse) 4.dp else 1.dp,
            focusedElevation = 5.dp
        )
    ) {
        Text(
            text = "$dbID",
            modifier = Modifier.fillMaxSize().wrapContentHeight(Alignment.CenterVertically),
            style = MaterialTheme.typography.displayLarge
        )
    }
}