import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton

@Composable
fun SettingsScreen(
    isAppInDarkTheme: Boolean,
    switchTheme: (darkTheme: Boolean) -> Unit,
    goToPicker: () -> Unit
) {
    Scaffold(
        content = {
            SettingsContent(
                isAppInDarkTheme = isAppInDarkTheme,
                switchTheme = switchTheme
            )
        },
        bottomBar = {
            SettingsBottomBar(
                goToPicker = goToPicker
            )
        }
    )
}

@Composable
fun SettingsContent(
    isAppInDarkTheme: Boolean,
    switchTheme: (darkTheme: Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 50.dp, end = 20.dp, bottom = 120.dp)
    ) {
        Text(
            text = "SETTINGS",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Toggle dark mode"
            )

            Switch(
                checked = isAppInDarkTheme,
                onCheckedChange = { darkTheme -> switchTheme(darkTheme) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                    uncheckedTrackColor = MaterialTheme.colors.surface,
                    uncheckedThumbColor = MaterialTheme.colors.primary,
                )
            )
        }

        Text(
            text = "ABOUT US",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "This application was created during Mobile Applications On Android Platform university course.",
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Creators:",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Jan Eliasz",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Michał Sternik",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Kamil Krawiec",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsBottomBar(
    goToPicker: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .height(100.dp)
            .fillMaxWidth(),

        ) {
        MasterButton(
            type = ButtonType.WHITE,
            onClick = goToPicker,
            text = "BACK",
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth()
        )
    }
}