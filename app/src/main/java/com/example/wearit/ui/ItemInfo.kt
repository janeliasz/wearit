import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wearit.R
import com.example.wearit.WearItScreen
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
import com.example.wearit.ui.BottomBarSpace
import com.example.wearit.ui.WardrobePageContent

@Composable
fun ItemInfo(
    navController: NavHostController,
    itemId: String,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
) {
    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            ItemInfoContent(
                innerPadding = innerPadding,
                itemId = itemId,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename
            )
        },
        bottomBar = {
            BottomBarSpace(navController = navController)
        }
    )


}




@Composable
fun ItemInfoContent(
    innerPadding: PaddingValues,
    itemId: String,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,

){
    Column(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        ZoomableImage(
            itemId = itemId,
            getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
            innerPadding = innerPadding
        )
        Divider(
            color = MaterialTheme.colors.primary,
            thickness = 8.dp,
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .clip(RoundedCornerShape(50))
//                .offset(y = -10.dp)
        )

    }
}

@Composable
fun ZoomableImage(
    itemId: String,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    innerPadding: PaddingValues,


    ) {
    val scale = remember { mutableStateOf(1f) }
    val rotationState = remember { mutableStateOf(0f) }
    Box(
//        contentAlignment = Alignment.Center
//        modifier = Modifier
//            .clip(RectangleShape) // Clip the box content
////            .fillMaxSize() // Give the size you want...
////            .size(100.dp)
//            .fillMaxWidth()
//            .background(Color.Gray)
//            .pointerInput(Unit) {
//                detectTransformGestures { centroid, pan, zoom, rotation ->
//                    scale.value *= zoom
//                    rotationState.value += rotation
//                }
//            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp - innerPadding.calculateBottomPadding()-20.dp)
                .background(Color.Gray)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        scale.value *= zoom
                        rotationState.value += rotation
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center


        ) {
            Image(
                modifier = Modifier
//                    .align(Alignment.Center) // keep the image centralized into the Box
                    .graphicsLayer(
                        // adding some zoom limits (min 50%, max 200%)
                        scaleX = maxOf(.5f, minOf(3f, scale.value)),
                        scaleY = maxOf(.5f, minOf(3f, scale.value)),
                        rotationZ = rotationState.value
                    ),
                contentDescription = null,
                bitmap = getItemPhotoByPhotoFilename( itemId ).asImageBitmap()
            )
        }

    }
}

@Composable
fun BottomBarSpace(
//    goToPickerScreen: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MasterButton(
            type = ButtonType.RED,
            onClick = { /*deleteItem(item)*/ },
            text = "DELETE",
            icon = null,
            modifier = Modifier
                .width(350.dp)
                .scale(1.3f)
        )

        MasterButton(
            type = ButtonType.WHITE,
            onClick = { navController.navigate(WearItScreen.Wardrobe.name) },
            text = "RETURN",
            icon = null,
            modifier = Modifier
                .width(350.dp)
                .scale(1.3f)
        )

    }
}
