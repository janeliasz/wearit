import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.wearit.R
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(navigateToPicker: () -> Unit){
    val scale = 1f
    LaunchedEffect(key1 = true ){
        delay(800L)
        navigateToPicker()
    }
    Box(
        contentAlignment = Alignment.Center,

        ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
