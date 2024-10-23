package org.d3ifcool.nugazyuk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import org.d3ifcool.nugazyuk.alarm.AlarmData
import org.d3ifcool.nugazyuk.alarm.Scheduler
import org.d3ifcool.nugazyuk.data.local.NugazlahDatabase
import org.d3ifcool.nugazyuk.data.remote.ServerConnect
import org.d3ifcool.nugazyuk.data.remote.response.ResponseGetDetailTaskData
import org.d3ifcool.nugazyuk.repository.AuthRepository
import org.d3ifcool.nugazyuk.repository.ClassRepository
import org.d3ifcool.nugazyuk.repository.TaskRepository
import org.d3ifcool.nugazyuk.screen.AddTaskScreen
import org.d3ifcool.nugazyuk.screen.AddTaskViewModel
import org.d3ifcool.nugazyuk.screen.AlarmScreen
import org.d3ifcool.nugazyuk.screen.ClassViewModel
import org.d3ifcool.nugazyuk.screen.HomeScreen
import org.d3ifcool.nugazyuk.screen.HomeViewModel
import org.d3ifcool.nugazyuk.screen.LoginScreen
import org.d3ifcool.nugazyuk.screen.LoginViewModel
import org.d3ifcool.nugazyuk.screen.ClassScreen
import org.d3ifcool.nugazyuk.screen.DetailTaskScreen
import org.d3ifcool.nugazyuk.screen.Screen
import org.d3ifcool.nugazyuk.screen.TaskDetailViewModel
import org.d3ifcool.nugazyuk.ui.theme.DarkSurface
import org.d3ifcool.nugazyuk.ui.theme.NugazlahTheme
import org.d3ifcool.nugazyuk.util.ParseTime
import org.d3ifcool.nugazyuk.util.Resource
import org.d3ifcool.nugazyuk.util.vmFactoryHelper



class MainActivity : ComponentActivity() {
    private lateinit var permissionsLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // TODO manage cancellation
        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val manageExternal =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            if (manageExternal != PackageManager.PERMISSION_GRANTED) {
                permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            permissionsLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }

        setContent {
            val navController = rememberNavController()
            val scheduler by lazy { Scheduler(this) }
            val nugazlahDatabase by lazy { NugazlahDatabase.getInstance(this) }
            val nugazlahApi by lazy { ServerConnect.getInstance() }
            val authorizedNugazlahApi by lazy {
                val token = runBlocking {
                    nugazlahDatabase.tokenDao().get()
                }
                val tok = if (token != null) token.token else ""

                ServerConnect.getAuthorizedInstance(tok)
            }

            val authRepository = AuthRepository(nugazlahApi, nugazlahDatabase.tokenDao())
            val taskRepository = TaskRepository(authorizedNugazlahApi, nugazlahDatabase.taskDao())

            var isOpenFromAlarm = false
            var taskIdFromAlarm = ""
            var startDestination = when (val result = runBlocking { authRepository.isLoggedIn() }) {
                is Resource.Success -> {
                    if (result.data!!) {
                        Screen.HomeScreen
                    } else {
                        Screen.LoginScreen
                    }
                }

                is Resource.Error -> {
                    Screen.LoginScreen
                }
            }

            if ((intent.getStringExtra("for") ?: "") == "detailTask") {
                taskIdFromAlarm = intent.getStringExtra("taskId")!!
                isOpenFromAlarm = true
                startDestination = "${Screen.DetailTaskScreen}/$taskId"
            }

            if ((intent.getStringExtra("for") ?: "") == "alarmTask") {
                taskIdFromAlarm = intent.getStringExtra("taskId")!!
                isOpenFromAlarm = true
                startDestination = "${Screen.AlarmScreen}/$taskIdFromAlarm"
            }

            NugazlahTheme(
                darkTheme = true,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkSurface
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = Screen.LoginScreen) {
                            val loginViewmodel = viewModel<LoginViewModel>(
                                factory = vmFactoryHelper {
                                    LoginViewModel(authRepository, navController)
                                }
                            )
                            LoginScreen(vm = loginViewmodel)
                        }
                        composable(route = Screen.HomeScreen) {
                            val classRepository =
                                ClassRepository(authorizedNugazlahApi)
                            val homeViewModel = viewModel<HomeViewModel>(
                                factory = vmFactoryHelper {
                                    HomeViewModel(
                                        classRepository,
                                        authRepository,
                                        navController
                                    )
                                }
                            )
                            HomeScreen(navController = navController, vm = homeViewModel)
                        }
                        composable(route = "${Screen.ClassScreen}/{classId}/{className}/{classMakerId}") {
                            val classId = remember {
                                it.arguments?.getString("classId")
                            }
                            val className = remember {
                                it.arguments?.getString("className")
                            }
                            val classMakerId = remember {
                                it.arguments?.getString("classMakerId")
                            }

                            val classViewModel = viewModel<ClassViewModel>(
                                factory = vmFactoryHelper {
                                    ClassViewModel(
                                        className ?: "",
                                        classId ?: "",
                                        classMakerId ?: "",
                                        taskRepository,
                                        authRepository,
                                        scheduler
                                    )
                                }
                            )
                            ClassScreen(
                                navController = navController,
                                vm = classViewModel
                            )
                        }
                        composable(route = "${Screen.DetailTaskScreen}/{taskId}") {
                            var taskId = remember {
                                it.arguments?.getString("taskId")
                            }
                            if (isOpenFromAlarm) {
                                taskId = taskIdFromAlarm
                            }

                            val taskDetailViewModel = viewModel<TaskDetailViewModel>(
                                factory = vmFactoryHelper {
                                    TaskDetailViewModel(
                                        taskRepository = taskRepository,
                                        navController = navController,
                                        taskId = taskId ?: ""
                                    )
                                }
                            )
                            DetailTaskScreen(
                                navController = navController,
                                vm = taskDetailViewModel,
                            )
                        }
                        composable(route = "${Screen.AddTaskScreen}/{classId}") {
                            val classId = remember {
                                it.arguments?.getString("classId")
                            }

                            val addTaskViewModel = viewModel<AddTaskViewModel>(
                                factory = vmFactoryHelper {
                                    AddTaskViewModel(
                                        taskRepository = taskRepository,
                                        navController = navController,
                                        classId = classId ?: ""
                                    )
                                }
                            )
                            AddTaskScreen(
                                navController = navController,
                                vm = addTaskViewModel
                            )
                        }
                        composable(route = "${Screen.AlarmScreen}/{taskId}") {
                            var taskId = remember {
                                it.arguments?.getString("taskId")
                            }

                            if (isOpenFromAlarm) {
                                taskId = taskIdFromAlarm
                            }

                            lateinit var task: ResponseGetDetailTaskData

                            runBlocking {
                                val result = taskRepository.getTaskDetail(taskId ?: "")
                                when (result) {
                                    is Resource.Success -> {
                                        task = result.data!!
                                    }

                                    is Resource.Error -> {
                                        Toast.makeText(
                                            applicationContext,
                                            "Gagal mengambil data tugas",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            AlarmScreen(
                                navController = navController,
                                data = AlarmData(
                                    id = 1,
                                    taskId = task.id,
                                    subject = "",
                                    title = task.title,
                                    description = task.description,
                                    deadline = ParseTime.iso8601ToReadable(task.deadline)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}