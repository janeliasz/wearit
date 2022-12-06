package com.example.wearit.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


enum class ButtonType(){
    RED,
    WHITE
}

@Composable
fun MasterButton(
    type: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier,
    icon: Int?,
    text: String,


    ) {
    Button(
        modifier = Modifier
            .then(modifier)
            .border(
                width = 5.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(50.dp)
            )
            .clip(RoundedCornerShape(50.dp)),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (type === ButtonType.RED) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
            contentColor = if (type === ButtonType.RED) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
        )
    )

    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = text,
                color = if (type == ButtonType.RED) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontSize = 23.sp
            )
            if(icon!= null) {
                Icon(
                    painterResource(id = icon),
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .size(30.dp),
                    contentDescription = "drawable icons",
                    tint = if (type == ButtonType.RED) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary,
                )
            }
        }
    }
}

