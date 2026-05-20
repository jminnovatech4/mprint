package com.jminnovatech.mprint.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun Loader() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("loading.json")
    )

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever
        )
    }
}