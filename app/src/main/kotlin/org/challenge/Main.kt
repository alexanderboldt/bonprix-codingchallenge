package org.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.challenge.repository.garment.GarmentsRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.challenge.repository.garment.model.Category
import org.challenge.repository.garment.model.Color
import org.challenge.repository.garment.model.Garment

fun main() = application {
    initialize()

    val state = WindowState().apply { placement = WindowPlacement.Maximized }
    Window(onCloseRequest = ::exitApplication, state = state, title = "Bonprix Codingchallenge") {
        MaterialTheme {
            Content()
        }
    }
}

@Composable
fun Content() {
    val coroutineScope = rememberCoroutineScope()
    val garmentsRepository = GarmentsRepository()

    var garmentsResult: Result<List<Garment>> by remember { mutableStateOf(Result.success(emptyList())) }

    var isSearchActive by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxSize()) {
        // filter section
        Column(modifier = Modifier.padding(16.dp).width(250.dp).verticalScroll(rememberScrollState())) {
            val categories = mutableListOf<Category>()
            val colors = mutableListOf<Color>()

            FilterTitle("Categories")
            Category.entries.forEach { category ->
                var checkedState by remember { mutableStateOf(false) }
                FilterOption(category.value, checkedState) { checkedState = it }

                if (checkedState) categories.add(category)
            }

            Spacer(modifier = Modifier.padding(16.dp))

            FilterTitle("Colors")
            Color.entries.forEach { color ->
                var checkedState by remember { mutableStateOf(false) }
                FilterOption(color.value, checkedState) { checkedState = it }

                if (checkedState) colors.add(color)
            }

            Spacer(modifier = Modifier.padding(16.dp))

            TextButton(onClick = {
                coroutineScope.launch {
                    isSearchActive = true
                    garmentsResult = garmentsRepository.getAllGarments(categories = categories, colors = colors)
                    isSearchActive = false
                }
            }, modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                enabled = !isSearchActive,
                colors = ButtonDefaults.textButtonColors(red, contentColor = androidx.compose.ui.graphics.Color.White)) {

                Text("Search")
            }
            if (isSearchActive) LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = red)
        }
        // result section
        Box(modifier = Modifier.fillMaxSize()) {
            garmentsResult.onSuccess { garments ->
                if (garments.isEmpty()) {
                    Text("No garments found. Please change some filters.", modifier = Modifier.align(Alignment.Center), color = red, fontWeight = FontWeight.Bold)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(garments, key = { it.id }) { garment ->
                            Text(text = garment.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(text = "Category: ${garment.category}")
                            Text(text = "Color: ${garment.color}")
                            Text(text = "Material: ${garment.material}")
                            Text(text = "Compatible with: " + garment.compatibleWith.joinToString(", "))
                        }
                    }
                }
            }.onFailure {
                Text("Could not load garments", modifier = Modifier.align(Alignment.Center), color = red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FilterTitle(text: String) = Text(text, fontWeight = FontWeight.Bold, color = red)

@Composable
fun FilterOption(text: String, state: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox( checked = state, onCheckedChange, colors = CheckboxDefaults.colors(checkedColor = red))
        Text(text)
    }
}

val red = androidx.compose.ui.graphics.Color(0xffbe181a)
