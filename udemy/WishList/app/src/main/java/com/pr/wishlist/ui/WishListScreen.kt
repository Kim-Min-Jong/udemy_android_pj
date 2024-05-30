package com.pr.wishlist.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

// scaffold를 사용하여 탑바 만들기
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // material design 의 기본 구성을 담고 있는 컴포저블
    Scaffold(
        //  탑바 생성
        topBar = {
            AppBarView(title = "WishList") {
                Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {

        }
    }
}