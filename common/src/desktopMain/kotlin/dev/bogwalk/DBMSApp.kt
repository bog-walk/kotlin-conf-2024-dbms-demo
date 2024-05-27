package dev.bogwalk

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import dev.bogwalk.client.AppClient
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.app_name
import dev.bogwalk.common.generated.resources.dbms_logo
import dev.bogwalk.common.generated.resources.delete_ship_message
import dev.bogwalk.ui.DBApp
import dev.bogwalk.ui.components.CustomDialog
import dev.bogwalk.ui.style.DBMSTheme
import dev.bogwalk.ui.style.windowHeight
import dev.bogwalk.ui.style.windowWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationScope.DBMSAppDesktop() {
    val scope = rememberCoroutineScope()
    val api by remember { mutableStateOf(AppClient(scope)) }

    LaunchedEffect("initial load") {
        api.isLoadingData = true
        withContext(Dispatchers.IO) {
            api.loadSavedData()
        }
        api.isLoadingData = false
    }

    Window(
        onCloseRequest = {
            api.cleanUp()
            exitApplication()
        },
        state = WindowState(position = WindowPosition.Aligned(Alignment.Center), width = windowWidth, height = windowHeight),
        title = stringResource(Res.string.app_name),
        resizable = false,
        icon = painterResource(Res.drawable.dbms_logo)
    ) {
        DBMSTheme {
            if (api.isDeleteDialogOpen) {
                CustomDialog(stringResource(Res.string.delete_ship_message), api::deleteShip, api::closeDeleteDialog)
            }
            if (api.isWarningDialogOpen) {
                CustomDialog(stringResource(Res.string.leave_form_message), api::confirmLeaveForm, api::closeWarningDialog)
            }
            DBApp(api)
        }
    }
}