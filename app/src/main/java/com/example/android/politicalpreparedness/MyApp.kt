package com.example.android.politicalpreparedness

import android.app.Application
import androidx.work.*
import com.example.android.politicalpreparedness.data.ElectionsDataSource
import com.example.android.politicalpreparedness.data.database.LocalDB
import com.example.android.politicalpreparedness.data.repository.CivicsRepository
import com.example.android.politicalpreparedness.data.worker.RefreshDataWorker
import com.example.android.politicalpreparedness.presentation.election.ElectionsViewModel
import com.example.android.politicalpreparedness.presentation.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.presentation.representative.RepresentativeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
class MyApp : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                ElectionsViewModel(
                        get(),
                        get() as ElectionsDataSource
                )
            }
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                RepresentativeViewModel(get())
            }

            single {
                //This view model is declared singleton to be used across multiple fragments
                VoterInfoViewModel(get())
            }

            single { CivicsRepository(get()) as ElectionsDataSource }
            single { LocalDB.createElectionsDao(this@MyApp)}
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }

        delayedInit()
    }

    private fun delayedInit() {

        applicationScope.launch {
            setupRecurringWork()
        }

    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)
    }
}