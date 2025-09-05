package net.inferno.teethbrush

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientLifecycleObserver
import dagger.hilt.android.AndroidEntryPoint
import net.inferno.teethbrush.theme.AppTheme
import net.inferno.teethbrush.ui.main.MainUI

@AndroidEntryPoint
class MainActivity : FragmentActivity(), AmbientLifecycleObserver.AmbientLifecycleCallback {

    private val ambientObserver = AmbientLifecycleObserver(
        this,
        this,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(ambientObserver)

        setContent {
            AppTheme {
                MainUI()
            }
        }
    }
}