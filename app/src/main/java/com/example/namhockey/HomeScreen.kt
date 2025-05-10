package com.example.namhockey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.namhockey.data.Repository
import com.example.namhockey.data.News
import kotlinx.coroutines.flow.collectLatest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.compose.runtime.remember


@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        ProfileRow()
        Spacer(modifier = Modifier.height(16.dp))
        MatchScoreCard()
        MatchScreen()
        NewsSection() // Add the news section

    }
}

@Composable
fun ProfileRow() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Row(
        modifier = Modifier
            .fillMaxWidth() // Take up 90% of the screen width
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)) // rounded corners
            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp)) // border with rounded corners
            .padding(16.dp)
            .width(screenWidth * 0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Team Logo
        Image(
            painter = painterResource(id = R.drawable.anchor),
            contentDescription = "Team Logo",
            modifier = Modifier
                .size(40.dp)
                .background(Color.White), // Added background to the logo
            contentScale = ContentScale.Fit
        )

        // Profile Icon
        Image(
            painter = painterResource(id = R.drawable.profile), // Replace with your profile icon resource
            contentDescription = "Profile Icon",
            modifier = Modifier
                .size(40.dp)
                .background(Color.White),// Added background to the profile icon
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun LiveMatchScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Spacer(modifier = Modifier.weight(1f))
        MatchScoreCard()
    }
}

@Composable
fun MatchScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        MatchCard()
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun TimeBadge(time: String) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF00C853), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(text = time, color = Color.White)
    }
}

@Composable
fun MatchScoreCard() {
    val backgroundColor = Color.Black
    val textColor = Color.White
    val accentColor = Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Aggregate Score
            Text(
                text = "agg 0 : 0",
                color = accentColor,
                fontSize = 16.sp
            )

            // Main row with logos and score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Inter Logo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TeamLogo(
                        resourceId = R.drawable.dtslogo,
                        teamName = "DTS"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "DTS",
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Score
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "4",
                            color = textColor,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " : ",
                            color = textColor,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "3",
                            color = textColor,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Full-Time
                    Text(
                        text = "Full-Time",
                        color = accentColor,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Barcelona Logo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TeamLogo(
                        resourceId = R.drawable.saintslogo,
                        teamName = "Saints"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Saints",
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TeamLogo(resourceId: Int, teamName: String) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = "$teamName logo",
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
    )
}

@Composable
fun MatchCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Everton", color = Color.Black)
                Text("Tomorrow 16:00", color = Color.Black)
                Text("Manchester City", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MatchStat("W", "8%", "2")
                MatchStat("D", "26%", "6")
                MatchStat("W", "66%", "16", highlight = true)
            }
        }
    }
}

@Composable
fun MatchStat(label: String, percent: String, value: String, highlight: Boolean = false) {
    val bgColor = if (highlight) Color(0xFF00E676) else Color(0xFF333333)
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$label • $percent", color = Color.White)
        Text(value, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun NewsSection(notificationsEnabled: Boolean = true) {
    val context = LocalContext.current
    val repository = remember { Repository() }
    val newsList by repository.news.collectAsState(initial = emptyList())

    // Show notification for the latest news
    LaunchedEffect(newsList, notificationsEnabled) {
        if (notificationsEnabled && newsList.isNotEmpty()) {
            val latestNews = newsList.first()
            val channelId = "news_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "News Notifications"
                val descriptionText = "Notifications for latest news"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(latestNews.title)
                .setContentText(latestNews.content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(context)) {
                notify(latestNews.id, builder.build())
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Latest News",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        newsList.forEach { news ->
            NewsCard(
                title = news.title,
                description = news.content
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun NewsCard(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF616161),
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true, device = "id:medium_phone")
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}