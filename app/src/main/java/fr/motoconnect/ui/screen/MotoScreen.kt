package fr.motoconnect.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.motoconnect.data.service.MotoService
import fr.motoconnect.ui.theme.MotoConnectTheme
import fr.motoconnect.viewmodel.CreateMotoViewModel
import fr.motoconnect.viewmodel.MotoViewModel

@Composable
fun MotoScreen(
    navController: NavController?,
    motoViewModel: MotoViewModel = viewModel()
){
    val motoUiState by motoViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {










        motoViewModel.getMotos().let {
            if(motoUiState.motos.isNotEmpty()){
                Text(text = "Vos motos :")
                motoUiState.motos.forEach {
                    Text(text = it.name)
                }
            }else{
                Button(onClick = {
                    navController?.navigate("moto-create")
                }) {
                    Text(text = "Ajouter une moto")
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateMotoScreen(
    createMotoViewModel: CreateMotoViewModel = viewModel()
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val createMotoUiState by createMotoViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Ajouter une moto", modifier = Modifier.padding(8.dp))

        TextField(
            value = createMotoUiState.name,
            singleLine = true,
            onValueChange = { createMotoViewModel.updateName(it) },
            label = { Text(text = "Nom de la moto")},
            isError = createMotoUiState.error.isNotEmpty(),
            modifier = Modifier.padding(10.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    createMoto(createMotoViewModel, context, keyboardController)
                }
            ),
        )

        Button(
            onClick = {
                createMoto(createMotoViewModel, context, keyboardController)
            },
            enabled = createMotoUiState.name.isNotEmpty() && createMotoUiState.error.isEmpty()
        ) {
            Text(text = "Ajouter la moto")
        }

        createMotoUiState.error.let {
            if(it.isNotEmpty()){
                Text(
                    modifier = Modifier
                        .padding(5.dp),
                    fontSize = 14.sp,
                    text = createMotoUiState.error
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun createMoto(createMotoViewModel: CreateMotoViewModel, context: Context, keyboardController: SoftwareKeyboardController?){
    //create moto
    keyboardController?.hide()
}

@Composable
@Preview(showBackground = true)
fun MotoScreenPreview(){
    MotoConnectTheme {
        CreateMotoScreen()
    }
}