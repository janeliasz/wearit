package com.example.wearit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wearit.ui.theme.MainRed
import com.example.wearit.ui.theme.SecondaryWhite


enum class ButtonType(){
    RED,
    WHITE
}


@Composable
fun MasterButton(
    type: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier,
//    content: Unit,
    icon: Int,
    text: String,
    width: Dp

) {
    when(type) {
        ButtonType.RED -> RedButton(
            onClick = onClick,
            modifier = modifier,
            //content = content
            icon = icon,
            text = text,
            width = width
        )
        ButtonType.WHITE -> WhiteButton(
            onClick = onClick,
            modifier = modifier,
//            content = content
        )
        else -> throw IllegalArgumentException("Non existent button type")// Must provide an 'else' branch
    }
}

@Composable
fun RedButton(
    onClick: () -> Unit,
    modifier: androidx.compose.ui.Modifier,
//    content: @Composable() (RowScope.() -> Unit),
    icon: Int,
    text: String,
    width: Dp

) {
    
    
    Button(
        onClick = onClick,
        modifier = androidx.compose.ui.Modifier
//            .fillMaxWidth()
            .width(width)
            .clip(RoundedCornerShape(50.dp))
            .background(MainRed)
            .then(modifier),


    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = SecondaryWhite,
//                textAlign = TextAlign.Center,
                fontSize = 23.sp
            )
            Icon(
                painterResource(id = icon),
                modifier = androidx.compose.ui.Modifier
                    .padding(10.dp, 0.dp)
                    .size(30.dp)
                ,
                contentDescription = "drawable icons",
                tint = SecondaryWhite
            )

        }
    }
}


@Composable
fun WhiteButton(
    onClick: () -> Unit,
    modifier: androidx.compose.ui.Modifier,
//    content: @Composable() (RowScope.() -> Unit)
) {

}



