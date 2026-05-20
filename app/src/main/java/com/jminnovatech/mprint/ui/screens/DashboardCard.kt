import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.Alignment

@Composable
fun DashboardCard(title: String, value: String, color: Brush, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .padding(6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(value, color = Color.White, fontSize = 22.sp)
                Text(title, color = Color.White)
            }
        }
    }
}