import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.example.wearit.R
import com.example.wearit.model.Item

@Composable
fun ImageIcon(
    modifier: Modifier,
    tickOpacity: Float,
    size: Dp,
    icon: Int,
    onClick: () -> Unit,

    ){
    Box(
        modifier = Modifier
            .zIndex(1F)
            .size(size)
            .clickable { onClick() }
            .then(modifier),
        contentAlignment = Alignment.Center,


    ) {
        Icon(
            painterResource(id = R.drawable.check),
            contentDescription = null,
            modifier = Modifier
                .alpha(tickOpacity)
            ,
            tint = MaterialTheme.colors.primary,
        )
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(0.6*size)
                .alpha(tickOpacity)
                .clip(shape = RoundedCornerShape(50.dp))
            ,
            tint = MaterialTheme.colors.onPrimary,
        )
    }

}