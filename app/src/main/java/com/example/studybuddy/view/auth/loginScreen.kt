package com.example.studybuddy.view.auth

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.R
import com.example.studybuddy.ui.theme.BlueText
import com.example.studybuddy.ui.theme.buttonColor
import com.example.studybuddy.ui.theme.grey
import com.example.studybuddy.view.destinations.DashBoardScreenRouteDestination
import com.example.studybuddy.view.destinations.SignupScreenRouteDestination
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@com.ramcosta.composedestinations.annotation.Destination(
    start = true,
    deepLinks = [
        DeepLink(
            action = Intent.ACTION_VIEW,
            uriPattern = "study_buddy://login")
    ]
)
@Composable
fun LoginScreenRoute(
    navigator: DestinationsNavigator,
) {
    val viewModel: AuthViewModel = hiltViewModel()
    LoginScreen(
        viewModel = viewModel,
        onLoginSuccess = { navigator.navigate(DashBoardScreenRouteDestination) },
        signupisclicked = { navigator.navigate(SignupScreenRouteDestination) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    signupisclicked: () -> Unit = {}

) {
    var password by remember {
        mutableStateOf("")
    }
    var userEmail by remember {
        mutableStateOf("")
    }
    var passwordVisible by remember { mutableStateOf(false) }
val authState by viewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {

            is AuthState.Error -> {
                val message = (authState as AuthState.Error).message
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Authenticated -> {
                onLoginSuccess()
            }

           else -> Unit
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login Image",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .size(250.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Log in",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(25.dp)) {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                )
                Spacer(modifier = Modifier.height(5.dp))

                TextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = grey,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Password",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = grey,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible)
                            painterResource(id = R.drawable.baseline_visibility_24)
                        else
                            painterResource(id = R.drawable.baseline_visibility_off_24)

                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Image(
                                painter = icon,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (userEmail.isNotBlank() && password.isNotBlank()&& isValidEmail(userEmail)) {
                            viewModel.login(
                                email = userEmail,
                                password = password
                            )
                        } else {
                            if (isValidEmail(userEmail)==false)
                            {
                                viewModel._authState.value = AuthState.Error("Please fill valid email")

                            }
                            else{
                                viewModel._authState.value = AuthState.Error("Please enter both email and password")
                            }

                        }
                    },
                    enabled = viewModel._authState.value != AuthState.Loading ,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor // Set the custom color here
                    )
                ) {
                    Text(text = "Log in")
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        text = "No Account?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.bodyMedium.copy(color = BlueText),
                        modifier = Modifier.clickable { signupisclicked()}
                    )
                }


            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    return email.matches(emailRegex)
}


