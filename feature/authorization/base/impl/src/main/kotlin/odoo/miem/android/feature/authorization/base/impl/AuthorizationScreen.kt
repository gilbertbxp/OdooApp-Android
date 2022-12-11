package odoo.miem.android.feature.authorization.base.impl

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import odoo.miem.android.common.uiKitComponents.buttons.TextButton
import odoo.miem.android.common.uiKitComponents.dividers.Divider
import odoo.miem.android.common.uiKitComponents.textfields.LoginTextField
import odoo.miem.android.core.uiKitTheme.OdooMiemAndroidTheme
import odoo.miem.android.core.uiKitTheme.commonPadding
import odoo.miem.android.core.uiKitTheme.dividerVerticalPadding
import odoo.miem.android.core.uiKitTheme.hseSecondary
import odoo.miem.android.core.uiKitTheme.mainHorizontalPadding
import odoo.miem.android.core.utils.rx.collectAsState
import odoo.miem.android.core.utils.state.LoadingResult
import odoo.miem.android.core.utils.state.SuccessResult
import odoo.miem.android.core.utils.state.subscribeOnError
import odoo.miem.android.feature.authorization.base.api.IAuthorizationScreen

/**
 * [AuthorizationScreen] реализация интерфейса [IAuthorizationScreen]
 *
 * Методы по назначению:
 * - [AuthorizationScreen] - входная точка в этот экран, нужен для начальных инициализация.
 * Например, инициализация viewModel
 * - [AuthorizationScreenContent] - непосредственно верстка данного экрана
 * - [AuthorizationScreenPreview] - превью верстки, которая получилась в [AuthorizationScreenContent]
 *
 * @author Ворожцов Михаил, Данилов Егор
 */
class AuthorizationScreen : IAuthorizationScreen {

    @SuppressLint("NotConstructor")
    @Composable
    override fun AuthorizationScreen(
        navController: NavHostController,
        showMessage: (Int) -> Unit
    ) {
        val viewModel: AuthorizationViewModel = viewModel()

        val authorizationStatus by viewModel.authorizationState.collectAsState()
        authorizationStatus.subscribeOnError(showMessage)

        // TODO Remove and go to next screen
        if (authorizationStatus is SuccessResult) {
            showMessage(R.string.login_welcome_header)
        }


        AuthorizationScreenContent(
            onGeneralAuthorization = viewModel::generalAuthorization,
            showMessage = showMessage,
            isLoading = authorizationStatus is LoadingResult
        )
    }

    @Composable
    private fun AuthorizationScreenContent(
        onGeneralAuthorization: (baseUrl: String, login: String, password: String) -> Unit = { _, _, _ -> },
        showMessage: (Int) -> Unit = {},
        isLoading: Boolean = false
    ) = Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        val odooGlobalUrl = stringResource(R.string.global_odoo_url)

        val loginButtonDesc = stringResource(R.string.login_button_desc)
        val loginWithHseButtonDesc = stringResource(R.string.login_with_hse_button_desc)

        var serverInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(odooGlobalUrl))
        }
        var emailInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        var passwordInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }

        var isServerInputError by remember { mutableStateOf(false) }
        var isLoginInputError by remember { mutableStateOf(false) }
        var isPasswordInputError by remember { mutableStateOf(false) }

        val onLoginButtonClick = {
            isServerInputError = serverInput.text.isBlank() || serverInput.text == odooGlobalUrl
            isLoginInputError = emailInput.text.isBlank()
            isPasswordInputError = passwordInput.text.isBlank()

            if (isServerInputError || isLoginInputError || isPasswordInputError) {
                showMessage(R.string.login_alert_message)
            } else {
                onGeneralAuthorization(
                    serverInput.text,
                    emailInput.text,
                    passwordInput.text
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.logo_odoo),
            contentDescription = stringResource(R.string.odoo_logo_desc),
            modifier = Modifier
                .padding(top = 30.dp)
                .size(width = 80.dp, height = 26.dp),
        )

        Text(
            text = stringResource(R.string.login_welcome_header),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(
                    start = mainHorizontalPadding,
                    top = 40.dp
                ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = stringResource(R.string.login_welcome_text),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(
                    horizontal = mainHorizontalPadding,
                    vertical = commonPadding
                ),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        LoginTextField(
            value = serverInput,
            labelResource = R.string.login_odoo_server,
            onValueChange = {
                isServerInputError = false
                serverInput = it
            },
            imeAction = ImeAction.Next,
            isError = isServerInputError
        )

        LoginTextField(
            value = emailInput,
            labelResource = R.string.login_email,
            onValueChange = {
                isLoginInputError = false
                emailInput = it
            },
            imeAction = ImeAction.Next,
            isError = isLoginInputError
        )

        LoginTextField(
            value = passwordInput,
            labelResource = R.string.login_password,
            onValueChange = {
                isPasswordInputError = false
                passwordInput = it
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = isPasswordInputError
        )

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 100.dp)
            )
        } else {
            TextButton(
                onClick = { onLoginButtonClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 75.dp)
                    .padding(horizontal = 36.dp)
                    .semantics { contentDescription = loginButtonDesc },
                textResource = R.string.login
            )

            Divider(
                textModifier = Modifier.padding(horizontal = commonPadding),
                paddingModifier = Modifier.padding(vertical = dividerVerticalPadding),
                textResource = R.string.login_divider_text
            )

            TextButton(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = hseSecondary,
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .semantics { contentDescription = loginWithHseButtonDesc },
                textResource = R.string.login_hse,
                iconResource = R.drawable.logo_hse,
            )
        }
    }

    @Composable
    @Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
    private fun AuthorizationScreenPreview() = OdooMiemAndroidTheme {
        AuthorizationScreenContent()
    }
}
