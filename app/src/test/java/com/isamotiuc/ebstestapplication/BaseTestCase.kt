package com.isamotiuc.ebstestapplication

import androidx.annotation.CallSuper
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.mockito.MockitoAnnotations


abstract class BaseTestCase {
    @CallSuper
    @Before
    open fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    interface SchedulerProvider {
        fun io(): Scheduler
        fun ui(): Scheduler
    }
}
