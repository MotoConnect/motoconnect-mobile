package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.BaseDistance
import fr.motoconnect.viewmodel.uiState.MotoUIState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MotoFluidsComponent(
    motoUIState: MotoUIState,
    resetEngineOil: () -> Unit,
    resetBrakeFluid: () -> Unit,
    resetChainLubrication: () -> Unit,
) {

    val engineOilPercentage = motoUIState.moto?.engineOil?.toFloat()?.div(BaseDistance.ENGINE_OIL.distance.toFloat()) ?: 0f
    val breakFluidPercentage = motoUIState.moto?.breakFluid?.toFloat()?.div(BaseDistance.BRAKE_FLUID.distance.toFloat()) ?: 0f
    val chainLubricationPercentage = motoUIState.moto?.chainLubrication?.toFloat()?.div(BaseDistance.CHAIN_LUBRICATION.distance.toFloat()) ?: 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.moto_engine_oil),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressMotoFuilds(fluidPercentage = engineOilPercentage)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = { resetEngineOil() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(text = stringResource(R.string.moto_reset))
                                }
                                Text(
                                    text = "${motoUIState.moto?.engineOil}/${BaseDistance.ENGINE_OIL.distance} km",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.End,
                                    modifier= Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                                )
                            }

                        }
                    }
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), modifier = Modifier
                    .fillMaxWidth()
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.moto_break_fluid),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressMotoFuilds(fluidPercentage = breakFluidPercentage)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(
                                onClick = { resetBrakeFluid() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                            ) {
                                Text(text = stringResource(R.string.moto_reset))
                            }
                            Text(
                                text = "${motoUIState.moto?.breakFluid}/${BaseDistance.BRAKE_FLUID.distance} km",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 20.sp,
                                textAlign = TextAlign.End,
                                modifier= Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                            )
                        }
                    }
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), modifier = Modifier
                    .fillMaxWidth()
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.moto_chain_greasing),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressMotoFuilds(fluidPercentage = chainLubricationPercentage)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(
                                onClick = { resetChainLubrication() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(text = stringResource(R.string.moto_reset))
                            }
                            Text(
                                text = "${motoUIState.moto?.chainLubrication}/${BaseDistance.CHAIN_LUBRICATION.distance} km",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 20.sp,
                                textAlign = TextAlign.End,
                                modifier= Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun LinearProgressMotoFuilds(fluidPercentage: Float){

    if (fluidPercentage > 1f) {
        LinearProgressIndicator(
            progress = 1f,
            color = colorResource(id =R.color.red),
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        LinearProgressIndicator(
            progress = fluidPercentage,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

