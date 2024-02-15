package fr.motoconnect.ui.screen.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.motoconnect.R
import java.util.Locale

@Composable
fun MapInfoMoto(
    distanceBetweenDeviceAndMoto: String,
    currentMoto: String,
    deviceState: Boolean
) {
    val deviceStateString =
        if (deviceState) stringResource(id = R.string.moto_in_motion) else stringResource(
            id = R.string.moto_stationary
        )
    val color = if (deviceState) Color(0xFF52CA5E) else Color(0xFFEC6D50)

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .width(250.dp)
                .clip(RoundedCornerShape(20))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = currentMoto.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = deviceStateString,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Image(
                        painter = painterResource(id = R.drawable.moto_status),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color),
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = stringResource(R.string.distance, distanceBetweenDeviceAndMoto),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
