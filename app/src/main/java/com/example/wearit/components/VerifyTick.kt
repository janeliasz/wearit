import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.example.wearit.R

@Composable
fun VerifyTick(
    modifier: Modifier,
    tickOpacity: Float,
    size: Dp,

    ){
    Box(
        modifier = Modifier
            .zIndex(1F)
            .size(size)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(id = R.drawable.check),
            contentDescription = null,
            modifier = Modifier
                .alpha(tickOpacity),
            tint = MaterialTheme.colors.primary,
        )
        Icon(
            painterResource(id = R.drawable.checkinside),
            contentDescription = null,
            modifier = Modifier
                .size(0.6*size)
                .alpha(tickOpacity),
            tint = MaterialTheme.colors.onPrimary,
        )
    }

}