package it.app.telehealth.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshablePage(
    modifier: Modifier = Modifier,
    minArtificialDelay: Long = 0,
    refresh: suspend () -> Unit,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()

    if (state.isRefreshing) {
        LaunchedEffect(true) {
            val elapsed = measureTimeMillis {
                refresh()
            }
            /**
             * In the case when a refresh is too fast, the experience can be quite jarring.
             * So we add a delay to make it seem like it takes minArtificialDelay amount of time.
             */
            if (elapsed < minArtificialDelay)
                delay(minArtificialDelay - elapsed)
            state.endRefresh()
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Box(
            modifier = Modifier.nestedScroll(state.nestedScrollConnection)
        ) {
            content()

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
            )
        }
    }
}