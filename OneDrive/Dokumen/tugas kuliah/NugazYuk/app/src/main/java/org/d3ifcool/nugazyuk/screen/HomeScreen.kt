package org.d3ifcool.nugazyuk.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.d3ifcool.nugazyuk.R
import org.d3ifcool.nugazyuk.ui.theme.GreenCard
import org.d3ifcool.nugazyuk.ui.theme.JetbrainsMono
import org.d3ifcool.nugazyuk.ui.theme.PurpleSurface
import org.d3ifcool.nugazyuk.ui.theme.WhitePlaceholder
import org.d3ifcool.nugazyuk.ui.theme.WhitePlain
import org.d3ifcool.nugazyuk.ui.theme.component.ClassCard
import org.d3ifcool.nugazyuk.ui.theme.component.CustomButton
import org.d3ifcool.nugazyuk.ui.theme.component.CustomInput
import org.d3ifcool.nugazyuk.ui.theme.component.CustomTextArea
import org.d3ifcool.nugazyuk.ui.theme.component.FloatingActionButtonCustom
import org.d3ifcool.nugazyuk.util.ValidationError
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: HomeViewModel
) {
    val state = vm.state.collectAsState().value
    val swipeRefreshState = rememberPullRefreshState(vm.isGetClassesLoading, { vm.getMyClasses() })
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    LaunchedEffect(navController.currentBackStackEntry) {
        vm.getMyClasses()
    }

    LaunchedEffect(key1 = true) {
        vm.eventFlow.collect { event ->
            when (event) {
                is HomeScreenEvent.ShowToast -> {
                    coroutineScope.launch {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            SmallTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(id = R.string.logo_collapsed),
                        color = WhitePlain,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Start
                    )
                },
                actions = {
                    TextButton(onClick = {
                        vm.onStateChange(state.copy(isDialogLogoutOpen = true))
                    }) {
                        Text(
                            text = "\uD83D\uDC64",
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(5.0f, 10.0f),
                                    blurRadius = 3f
                                )
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFC8B891), // Mengatur warna latar belakang Top App Bar
                    titleContentColor = Color.Black, // Mengatur warna teks judul
                    actionIconContentColor = Color.Black // Mengatur warna ikon aksi
                )
            )

        },
        floatingActionButton = {
            FloatingActionButtonCustom(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .testTag("ActionButtonAddClass"),
                actionLogo = Icons.Rounded.Add,
            ) { vm.onStateChange(state.copy(isDialogOpen = true)) }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pullRefresh(swipeRefreshState),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                if (!vm.isGetClassesLoading && state.classes.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.there_is_no_classes),
                            style = MaterialTheme.typography.labelLarge,
                            color = WhitePlaceholder
                        )
                    }
                }
                items(items = state.classes, key = { it.id }) {
                    val classData = it
                    ClassCard(
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        data = it,
                        onClick = {
                            navController.navigate(
                                "${Screen.ClassScreen}/${classData.id}/${classData.name}/${classData.maker}"
                            )
                        },
                        testTagSemantic = if (it.name == "Lorem ipsum") "ClassCardTag" else ""
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = vm.isGetClassesLoading,
                state = swipeRefreshState,
                modifier = Modifier
                    .padding(paddingValues)
            )
        }
    }

    if (state.isDialogLogoutOpen) {
        BasicAlertDialog(
            onDismissRequest = { vm.onStateChange(state.copy(isDialogLogoutOpen = false)) },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.logout_confirmation),
                    style = MaterialTheme.typography.labelLarge,
                    color = WhitePlain
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            vm.onStateChange(state.copy(isDialogLogoutOpen = false))
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_text_cancel),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors().copy(containerColor = GreenCard),
                        onClick = {
                            vm.logout(context)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_text_confirm),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }

    if (state.isDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { vm.onStateChange(state.copy(isDialogOpen = false)) },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(PurpleSurface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        vm.onStateChange(
                            state.copy(
                                isDialogOpen = false,
                                isDialogMakeRoomOpen = true
                            )
                        )
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.make_room),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        vm.onStateChange(
                            state.copy(
                                isDialogOpen = false,
                                isDialogJoinRoomOpen = true
                            )
                        )
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.join_room),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    if (state.isDialogJoinRoomOpen) {
        BasicAlertDialog(
            onDismissRequest = { vm.onStateChange(state.copy(isDialogJoinRoomOpen = false)) },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFC8B891))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.class_code_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    hint = stringResource(id = R.string.class_code_placeholder),
                    text = state.classCode,
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF6C5A3B)),
                    onChange = {
                        if (it.length <= 6) {
                            vm.onStateChange(state.copy(classCode = it.uppercase(Locale.ROOT)))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomButton(
                        text = stringResource(id = R.string.button_text_confirm),
                        onClick = {
                            try {
                                vm.joinClass()
                            } catch (e: ValidationError) {
                                return@CustomButton
                            }
                        },
                        backgroundColor = Color(0xFF6C5A3B)
                    )
                }
            }
        }
    }

    if (state.isDialogMakeRoomOpen) {
        BasicAlertDialog(
            onDismissRequest = { vm.onStateChange(state.copy(isDialogMakeRoomOpen = false)) },
        ) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFC8B891))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.lecturer_input_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    testTagSemantic = "InputLecturerName",
                    hint = stringResource(id = R.string.lecturer_input_placeholder),
                    text = state.classLecturer,
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF6C5A3B)), // Warna teks input menjadi #6C5A3B
                    onChange = {
                        vm.onStateChange(state.copy(classLecturer = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.class_name_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    hint = stringResource(id = R.string.class_name_placeholder),
                    text = state.className,
                    testTagSemantic = "InputClassName",
                    onChange = {
                        vm.onStateChange(state.copy(className = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.class_description_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = JetbrainsMono,
                    color = WhitePlain
                )
                CustomTextArea(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    testTagSemantic = "InputDescriptionClass",
                    hint = stringResource(id = R.string.class_description_placeholder),
                    text = state.classDescription,
                    height = 100.dp,
                    onChange = {
                        vm.onStateChange(state.copy(classDescription = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 2.dp),
                            text = stringResource(id = R.string.class_icon_label),
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = JetbrainsMono,
                            color = WhitePlain
                        )
                        CustomInput(
                            testTagSemantic = "InputIconClass",
                            hint = stringResource(id = R.string.class_icon_placeholder),
                            text = state.classIcon,
                            onChange = {
                                if (it.isNotEmpty() && it.isNotBlank() && it.length < 3) {
                                    val iconChar = it[0]
                                    if (!Character.isLetterOrDigit(iconChar) && !Character.isWhitespace(
                                            iconChar
                                        )
                                    ) {
                                        vm.onStateChange(state.copy(classIcon = it))
                                    }
                                } else if (it.isEmpty()) {
                                    vm.onStateChange(state.copy(classIcon = ""))
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    CustomButton(
                        text = stringResource(id = R.string.button_text_confirm),
                        onClick = {
                            try {
                                vm.createMyClass()
                            } catch (e: ValidationError) {
                                return@CustomButton
                            }
                        },
                        backgroundColor = Color(0xFF6C5A3B)
                    )
                }
            }
        }
    }
}
