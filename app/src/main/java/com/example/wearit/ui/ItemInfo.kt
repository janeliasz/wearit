import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.wearit.R
import com.example.wearit.WearItScreen
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
import com.example.wearit.model.Item
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun ItemInfo(
    returnToWardrobe: () -> Unit,
    itemId: String,
    getItemById: (itemId: String) -> Item,
    deleteItem: (item: Item) -> Unit,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,

) {
    val currItem = getItemById(itemId)

    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            ItemInfoContent(
                innerPadding = innerPadding,
                itemId = itemId,
                currItem = currItem,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename

            )
        },
        bottomBar = {
            BottomBarSpace(
                returnToWardrobe = returnToWardrobe,
                currItem = currItem,
                deleteItem = deleteItem
            )
        }
    )


}


@Composable
fun ItemInfoContent(
    innerPadding: PaddingValues,
    itemId: String,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    currItem: Item,

    ){
    Column(
        modifier = Modifier
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePreview(
            currItem = currItem,
            getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
            innerPadding = innerPadding,
            modifier=Modifier,
        )
        Divider(
            color = MaterialTheme.colors.primary,
            thickness = 5.dp,
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .clip(RoundedCornerShape(50))
        )
    }
}


@Composable
fun BottomBarSpace(
    returnToWardrobe: () -> Unit,
    currItem: Item,
    deleteItem: (item: Item) -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colors.onPrimary),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MasterButton(
            type = ButtonType.RED,
            onClick = {
                deleteItem(currItem)
                returnToWardrobe()
            },
            text = "DELETE",
            icon = null,
            modifier = Modifier
                .width(350.dp)
                .scale(1.3f)
        )

        MasterButton(
            type = ButtonType.WHITE,
            onClick = { returnToWardrobe() },
            text = "RETURN",
            icon = null,
            modifier = Modifier
                .width(350.dp)
                .scale(1.3f)
        )

    }
}
@Composable
fun ImagePreview(
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    innerPadding: PaddingValues,
    modifier: Modifier,
    currItem: Item,

    ) {
    Column(
        modifier = Modifier
            .height(LocalConfiguration.current.screenHeightDp.dp - innerPadding.calculateBottomPadding() - 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        var angle by remember { mutableStateOf(0f) }
        var zoom by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                .zIndex(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colors.onPrimary),
            contentAlignment = Alignment.Center

        ) {
            Image(
                painter = painterResource(R.drawable.wearit),
                contentDescription = "logo",
            )
            Button(
                onClick = {
                    offsetX = 0f
                    offsetY = 0f
                    zoom = 1f
                    angle = 0f
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.undo_arrow),
                    contentDescription = "image",
                    modifier = Modifier
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
        }

        Box(modifier = Modifier
            .clip(RoundedCornerShape(20))
            .border(5.dp, MaterialTheme.colors.primary, RoundedCornerShape(20))
            .padding(0.dp, 60.dp)
        ) {
            Image(
                bitmap = getItemPhotoByPhotoFilename(currItem.photoFilename).asImageBitmap(),
                contentDescription = "image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(350.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .graphicsLayer(
                        scaleX = zoom,
                        scaleY = zoom,
                        rotationZ = angle
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures(
                            onGesture = { _, pan, gestureZoom, gestureRotate ->
                                angle += gestureRotate
                                zoom *= gestureZoom
                                val x = pan.x * zoom
                                val y = pan.y * zoom
                                val angleRad = angle * PI / 180.0
                                offsetX += (x * cos(angleRad) - y * sin(angleRad)).toFloat()
                                offsetY += (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                            }
                        )
                    }
                    .then(modifier)
            )
        }
    }
}