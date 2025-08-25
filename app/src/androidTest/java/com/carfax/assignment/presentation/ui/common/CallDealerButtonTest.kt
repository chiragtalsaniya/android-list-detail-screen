package com.carfax.assignment.presentation.ui.common

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carfax.assignment.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CallDealerButtonTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shows_confirmation_dialog_when_clicked() {
        rule.setContent {
            CallDealerButton(phone = "5125551234")
        }
        // Tap button
        rule.onNodeWithText(rule.activity.getString(R.string.call_dealer)).performClick()
        // Dialog title should appear
        rule.onNodeWithText(rule.activity.getString(R.string.confirm_call_title)).assertExists()
    }
}
