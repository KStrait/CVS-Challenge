package com.kls.cvschallenge.ui.search

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.kls.cvschallenge.data.FlickrImage
import com.kls.cvschallenge.data.Media
import com.kls.cvschallenge.data.Result
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchItems: State<Result<List<FlickrImage>>> = searchViewModel.images.collectAsState()
    var query by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column {
                SearchBar(
                    query = query,
                    onQueryChanged = { newQuery ->
                        query = newQuery
                        searchViewModel.searchFlickrImages(newQuery)
                    }
                )
                when (searchItems.value) {
                    is Result.Success -> {
                        val data = (searchItems.value as Result.Success).data
                        ImageGrid(images = data) { img ->
                            val encodedImage = Uri.encode(Json.encodeToString(img))
                            navController.navigate("detailsScreen/$encodedImage")
                        }
                    }
                    is Result.Error -> {
                        val exception = (searchItems.value as Result.Error).exception
                        Text(exception.toString(), color = Color.Red)
                    }
                    is Result.Loading -> {
                        LoadingView()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
) {
    var text by rememberSaveable { mutableStateOf(query) }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onQueryChanged(newText)
        },
        placeholder = { Text(text = "Search...") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .border(1.dp, color = MaterialTheme.colorScheme.onBackground),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ImageGrid(images: List<FlickrImage>, onImageClicked: (FlickrImage) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(top = 16.dp)
    ) {
        items(images.size) { index ->
            images[index].let { img ->
                Card(
                    modifier = Modifier.aspectRatio(3f / 2f)
                ) {
                    ImageCard(image = img, onImageClicked)
                }
            }
        }
    }
}

@Composable
fun ImageCard(image: FlickrImage, onImageClicked: (FlickrImage) -> Unit) {
    Card(
        Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onImageClicked(image)
            }
    ) {
        androidx.compose.foundation.Image(
            painter = rememberImagePainter(image.media.m),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.Red)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val dummyImages = listOf(
        FlickrImage(
            title = "Image 1",
            link = "https://www.example.com/image1",
            media = Media(m = "https://via.placeholder.com/150"),
            date_taken = "2025-01-01T00:00:00Z",
            description = "Description of image 1",
            published = "2025-01-01T01:00:00Z",
            author = "Author 1",
            author_id = "author1",
            tags = "tag1, tag2"
        ),
        FlickrImage(
            title = "Image 2",
            link = "https://www.example.com/image2",
            media = Media(m = "https://via.placeholder.com/150"),
            date_taken = "2025-01-02T00:00:00Z",
            description = "Description of image 2",
            published = "2025-01-02T01:00:00Z",
            author = "Author 2",
            author_id = "author2",
            tags = "tag2, tag3"
        ),
        FlickrImage(
            title = "Image 3",
            link = "https://www.example.com/image3",
            media = Media(m = "https://via.placeholder.com/150"),
            date_taken = "2025-01-03T00:00:00Z",
            description = "Description of image 3",
            published = "2025-01-03T01:00:00Z",
            author = "Author 3",
            author_id = "author3",
            tags = "tag3, tag4"
        )
    )

    val searchItems: State<Result<List<FlickrImage>>> = remember {
        mutableStateOf(Result.Success(dummyImages))
    }
    SearchScreenPreview(searchItems)
}

@Composable
fun SearchScreenPreview(searchItems: State<Result<List<FlickrImage>>>) {
    var query by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column {
                SearchBar(
                    query = query,
                    onQueryChanged = { newQuery ->
                        query = newQuery
                    }
                )
                when (searchItems.value) {
                    is Result.Success -> {
                        val data = (searchItems.value as Result.Success).data
                        ImageGrid(images = data) { img ->

                        }
                    }
                    is Result.Error -> {
                        Text("Error occurred", color = Color.Red)
                    }
                    is Result.Loading -> {
                        LoadingView()
                    }
                }
            }
        }
    }
}