package com.example.yatask.ui.screens.noteInfoScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.yatask.R
import com.example.yatask.data.models.TodoItem
import com.example.yatask.utils.Importance
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun NoteScreen(navController: NavController , argId : String){
    var message : String = ""
    var date : Date? = null
    val importance = Importance.NORMAL


    val viewModel : NoteInfoViewModel = viewModel(NoteInfoViewModelImpl::class)
    viewModel.getNoteById(argId)
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value){
        DatePickerModal(onDateSelected = {
            date = Date(it ?: return@DatePickerModal)
        } , onDismiss = {
            showDialog.value = false
        })
    }
    Column(modifier = Modifier.fillMaxSize()) {
        CustomAppBar(navController = navController,
            clickListener = {
                viewModel.saveNote(
                    TodoItem(
                        (1..999).random().toString(),
                        if (message.isNotEmpty()) message else return@CustomAppBar,
                        importance = importance,
                        deadline = date,
                        isCompleted = false,
                        createdAt = Date(),
                        modifiedAt = Date()
                    )
                )
            }
            )
        Column (modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF7F6F2)) ,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top) {
            AddNoteLayout(viewModel) { it ->
                message = it
            }
            ImportanceLayout()
            DateLayout(showDialog , viewModel)
            DeleteLayout(navController)
        }
    }
}
@Composable
fun DropDownDemo() {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val usernames = listOf("Alexander", "Isabella", "Benjamin", "Sophia", "Christopher")

    Column(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top

    ) {

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = usernames[itemPosition.value])
//                Image(
//                    painter = painterResource(id = R.drawable.drop_down_ic),
//                    contentDescription = "DropDown Icon"
//                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                usernames.forEachIndexed { index, username ->
                    DropdownMenuItem(text = {
                        Text(text = username)
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                        })
                }
            }
        }

    }
}

@Composable
fun CustomAppBar(clickListener: () -> Unit , navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color(0xFFF7F6F2)) ,
        verticalAlignment = Alignment.CenterVertically)
    {
        Icon(painter = painterResource(R.drawable.ic_fab),
            contentDescription = null ,
            tint = Color.Black ,
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    navController.navigateUp()
                }
                .padding(16.dp)
                .rotate(45f)

        )
        Spacer(Modifier.weight(1f))

        Text(modifier = Modifier
            .height(56.dp)
            .clickable {
                clickListener()
                navController.navigateUp()
            }
            .padding(end = 16.dp, start = 16.dp)
            .wrapContentHeight(align = Alignment.CenterVertically)
            ,
            text = "Сохранить".uppercase(),
            color = Color( 0xFF007AFF) ,
            fontSize = 14.sp ,
            lineHeight = 24.sp ,)

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteLayout(viewModel: NoteInfoViewModel , callback :( String )-> Unit){
    val todoItem by viewModel.todoItem.collectAsState()
    var input by rememberSaveable { mutableStateOf(todoItem?.text ?: "") }
    TextField(
        value = input,
        onValueChange = { newText ->
            input = newText
            callback(newText.trim())
        },
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .defaultMinSize(minHeight = 112.dp)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.06f),
                        Color.Black.copy(alpha = 0.12f)
                    )
                ), shape = RoundedCornerShape(8.dp)
            )
            .padding(bottom = 2.dp, start = 1.dp, end = 1.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(fontSize = 16.sp),
        placeholder = {
            Text("Что надо сделать...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400) ,
                    lineHeight = 16.sp,
                    color = Color.Black.copy(alpha = 0.3f),
                    modifier = Modifier
                    )
        },


//        decorationBox = { _ ->
//            if (input.isEmpty()) {
//                Text("Что надо сделать...",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight(400) ,
//                    lineHeight = 16.sp,
//                    color = Color.Black.copy(alpha = 0.3f),
//                    modifier = Modifier.padding(16.dp)
//                    )
//            }else{
//                Text(input ,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight(400) ,
//                    lineHeight = 16.sp,
//                    color = Color.Black,
//                    modifier = Modifier.padding(16.dp))
//            }
//        }
    )
}

@Composable
fun ImportanceLayout(){
    Column (modifier = Modifier
        .padding(top = 12.dp)
        .height(72.dp)
        .fillMaxWidth()
        .clickable {

        }) {

        Text("Важность" ,
            fontSize = 16.sp ,
            lineHeight = 18.sp ,
            fontWeight = FontWeight(400),
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp , top = 16.dp)
        )

//        DropDownDemo()

        Text("Нет" ,
            fontSize = 14.sp ,
            lineHeight = 16.sp ,
            fontWeight = FontWeight(400),
            color = Color.Black.copy(alpha = 0.2f),
            modifier = Modifier.padding(start = 16.dp , top = 4.dp)
        )

    }

    HorizontalDivider(
        thickness = 0.5.dp ,
        modifier = Modifier.padding(start = 16.dp , end = 16.dp)
    )
}

@Composable
fun DateLayout(state : MutableState<Boolean> , viewModel: NoteInfoViewModel){
    val todoItem by viewModel.todoItem.collectAsState()



    Row (modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            state.value = true
        }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column (modifier = Modifier
            .weight(1f)
            .height(72.dp)) {

            Text("Сделать до" ,
                fontSize = 16.sp ,
                lineHeight = 18.sp ,
                fontWeight = FontWeight(400),
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp , top = 16.dp)
            )

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            Text(if (todoItem?.deadline != null ) sdf.format(todoItem?.deadline?.date) else "",
                fontSize = 14.sp ,
                lineHeight = 16.sp ,
                fontWeight = FontWeight(400),
                color = Color(0xFF007AFF),
                modifier = Modifier.padding(start = 16.dp , top = 4.dp)
            )
        }

        Switch(checked = false , onCheckedChange = {
            state.value = true
        } ,
            modifier = Modifier.padding(end = 16.dp))
    }
}

@Composable
fun DeleteLayout(navController: NavController){
    Column (modifier = Modifier
        .padding(top = 24.dp)
        .height(56.dp)
        .fillMaxWidth()
        ) {
        HorizontalDivider(thickness = 0.5.dp)

        Row (modifier = Modifier
            .height(55.dp)
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigateUp()
            },
            verticalAlignment = Alignment.CenterVertically)
        {
            Icon(painter = painterResource(R.drawable.ic_basket) ,
                contentDescription = null ,
                tint = Color(0xFFFF3B30),
                modifier = Modifier.size(24.dp))

            Text("Удалить" ,
                fontSize = 16.sp ,
                lineHeight = 20.sp ,
                fontWeight = FontWeight(400),
                color = Color(0xFFFF3B30),
                modifier = Modifier.padding(start = 16.dp )
            )

        }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MaterialSpinner(
    title: String, options: List<T>,
    onSelect: (option: T) -> Unit, modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = it },
        modifier = Modifier.then(modifier)
    ) {
        TextField(
            value = selectedOption.toString(),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        
        
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString(), style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        selectedOption = option
                        onSelect(selectedOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}





