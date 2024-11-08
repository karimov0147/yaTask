package com.example.yatask.ui.screens.homeScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.yatask.R
import com.example.yatask.data.models.TodoItem
import com.example.yatask.utils.Importance
import com.example.yatask.utils.NavigationPath
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel : HomeViewModel  = viewModel(HomeViewModelImpl::class)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isCollapsed = remember { derivedStateOf { scrollBehavior.state.collapsedFraction > 0.5 } }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7F6F2),
                    scrolledContainerColor = Color(0xFFF7F6F2),
                    titleContentColor = Color.Black,
                ),
                title = {
                    AppBar(!isCollapsed.value , viewModel)
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier.shadow(if (!isCollapsed.value) 0.dp else 6.dp)
            )
        },
        floatingActionButton = {
            FAB(navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xFFF7F6F2)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            val state by viewModel.viewState.collectAsState()
            TaskList( viewModel.getAllNoteList(state) , viewModel , navController)
        }
    }

}

@Composable
fun FAB(navController: NavController) {
    FloatingActionButton(
        containerColor = Color(0xFF007AFF),
        contentColor = Color.White,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(6.dp),
        modifier = Modifier.padding(bottom = 24.dp),
        onClick = {
            navController.navigate(
                NavigationPath.NOTE_SCREEN.name + "/{id}".replace(
                    "{id}",
                    "-1"
                )
            )
        },
    ) {
        Icon(painterResource(R.drawable.ic_fab), "Floating action button.")
    }
}

@Composable
fun AppBar(state : Boolean ,  viewModel: HomeViewModel) {
    val state1 by viewModel.viewState.collectAsState()
    Row(
        modifier = Modifier
            .padding(end = 24.dp, start = 60.dp)

    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                "Мои дела",
                fontSize = 32.sp,
                fontWeight = FontWeight(500),
                color = Color.Black
            )

            if (state) {
                Text(
                    "Выполнено — 5",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF8E8E90)
                )
            }
        }
        Image(
            painter =  if (state1) painterResource(R.drawable.ic_show) else painterResource(R.drawable.ic_hide)  ,
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = if (state) 10.dp else 4.dp)
                .size(24.dp)
                .align(Alignment.Bottom)
                .clickable(onClick = {
                    viewModel.changeViewState(!state1)

                })
        )
    }


}

@Composable
fun TaskList(list: SnapshotStateList<TodoItem>, viewModel: HomeViewModel , navigationController: NavController ) {
    val state by viewModel.viewState.collectAsState()
    LaunchedEffect(state) {
        list
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.06f),
                        Color.Black.copy(alpha = 0.12f)
                    )
                ), shape = RoundedCornerShape(8.dp)
            )
            .padding(bottom = 2.dp, start = 1.dp, end = 1.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        items(list) {
            val delete = SwipeAction(
                onSwipe = {
                   viewModel.removeTask(it)
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_basket),
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp),
                        tint = Color.White
                    )
                },
                background = Color(0xFFFF3B30),
                isUndo = true
            )

            val done = SwipeAction(
                onSwipe = {
                    viewModel.doneTask(it)
                },
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_done),
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp),
                        tint = Color.White
                    )
                },
                background = Color(0xFF34C759) ,
                isUndo = true
            )

            SwipeableActionsBox(
                modifier = Modifier,
                swipeThreshold = 40.dp,
                startActions = listOf(done),
                endActions = listOf(delete),
                backgroundUntilSwipeThreshold = Color(0xFFF7F6F2)
            ) {
                Item(it , viewModel , navigationController)
            }
        }
    }
}

@Composable
fun Item(note : TodoItem, viewModel: HomeViewModel , navigationController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            .clickable {
                navigationController.navigate(
                    NavigationPath.NOTE_SCREEN.name + "/{id}".replace(
                        "{id}",
                        note.id
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Checkbox(
            checked =  note.isCompleted,
            colors = CheckboxColors(
                checkedBorderColor =  Color(0xFF34C759) ,
                checkedCheckmarkColor = Color.White,
                uncheckedCheckmarkColor = Color.Transparent,
                checkedBoxColor = Color(0xFF34C759),
                uncheckedBoxColor = if (note.importance == Importance.HIGH) Color(0xFFFF3B30).copy(alpha = 0.16f) else Color.Transparent,
                disabledCheckedBoxColor = Color.Transparent,
                disabledUncheckedBoxColor = Color.Transparent,
                disabledIndeterminateBoxColor = Color.Transparent,
                uncheckedBorderColor = if (note.importance == Importance.HIGH) Color(0xFFFF3B30) else Color.Black.copy(alpha = 0.2f),
                disabledBorderColor = Color.Transparent,
                disabledUncheckedBorderColor = Color.Transparent,
                disabledIndeterminateBorderColor = Color.Transparent
            ),
            modifier = Modifier.size(24.dp),
            onCheckedChange = {
                viewModel.doneTask(note)
            },
            
        )

        Text(
             if (note.importance == Importance.HIGH) "‼\uFE0F "+note.text else note.text,
            color = if (note.isCompleted) Color.Black.copy(alpha = 0.3f) else Color.Black,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            maxLines = 3,
            style = if (note.isCompleted) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(textDecoration = TextDecoration.None),
            textAlign = TextAlign.Left,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .weight(1f)
        )

        Icon(
            modifier = Modifier
                .size(24.dp),
            painter = painterResource(R.drawable.ic_info),
            contentDescription = null,

            )
    }
}