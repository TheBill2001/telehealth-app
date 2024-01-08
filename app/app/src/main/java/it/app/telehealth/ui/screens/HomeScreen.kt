package it.app.telehealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.app.telehealth.R
import it.app.telehealth.ui.viewmodels.AuthorizationViewModel
import it.app.telehealth.ui.viewmodels.ProfileViewModel

@Composable
fun HomeScreen(
    profileViewModel: ProfileViewModel,
    authorizationViewModel: AuthorizationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUserProfile by profileViewModel.currentUserProfile.observeAsState()
    val isLoggedInState by authorizationViewModel.isLoggedIn.collectAsState()

    val greetingString: String by remember {
        derivedStateOf {
            if (currentUserProfile?.name != null) {
                try {
                    context.resources.getString(R.string.greeting_string_1, currentUserProfile?.name?.split(" ")?.last() ?: "")
                } catch (e: NoSuchElementException) {
                    context.resources.getString(R.string.greeting_string_2)
                }
            } else
                context.resources.getString(R.string.greeting_string_2)
        }
    }

    LaunchedEffect(Unit) {
        if (isLoggedInState && currentUserProfile == null) {
            profileViewModel.fetchCurrentUserProfile(context)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = greetingString,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }

//        val itemsList = (0..5).toList()
//        val itemsIndexedList = listOf("A", "B", "C")
//
//        val itemModifier = Modifier
//            .border(1.dp, Color.Blue)
//            .height(80.dp)
//            .wrapContentSize()
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(3)
//        ) {
//            items(itemsList) {
//                Text("Item is $it", itemModifier)
//            }
//            item {
//                Text("Single item", itemModifier)
//            }
//            itemsIndexed(itemsIndexedList) { index, item ->
//                Text("Item at index $index is $item", itemModifier)
//            }
//        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel(), viewModel(), rememberNavController())
}