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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ButtonType() {
    RED, WHITE
}

@Composable
fun MasterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.RED,
    enabled: Boolean = true,
    text: String? = null,
    icon: Int? = null,
    fontSize: TextUnit = 23.sp
) {
    Button(
        modifier = Modifier
            .border(
                width = 5.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(50.dp)
            )
            .clip(RoundedCornerShape(50.dp))
            .then(modifier),
        onClick = onClick,
        enabled = enabled,
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
            if (text != null) {
                Text(
                    text = text,
                    fontSize = fontSize
                )
            }
            if (icon != null) {
                Icon(
                    painterResource(id = icon),
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .size(30.dp),
                    contentDescription = "null",
                )
            }
        }
    }
}
