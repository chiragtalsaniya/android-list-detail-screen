package com.carfax.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.carfax.assignment.presentation.ui.nav.AppNavGraph
import com.carfax.assignment.ui.theme.CARFAXAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CARFAXAssignmentTheme {
                Surface {
                    AppNavGraph()
                }
            }
        }
    }
}