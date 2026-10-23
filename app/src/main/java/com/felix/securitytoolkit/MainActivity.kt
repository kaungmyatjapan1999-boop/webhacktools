package com.felix.securitytoolkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Night = Color(0xFF0B1020)
private val Panel = Color(0xFF151D33)
private val Muted = Color(0xFF9AA8C7)
private val Cyan = Color(0xFF36D7F2)
private val Purple = Color(0xFF9B6BFF)
private val Green = Color(0xFF40E0A0)
private val Pink = Color(0xFFFF6CAB)
private val Amber = Color(0xFFFFC857)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Night,
                    surface = Panel,
                    primary = Cyan,
                    secondary = Purple
                )
            ) {
                FelixDashboard()
            }
        }
    }
}

data class ToolItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun FelixDashboard() {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedTool by remember { mutableStateOf<ToolItem?>(null) }
    var search by remember { mutableStateOf("") }

    val tools = listOf(
        ToolItem("Web Scanner", "Authorized site checks", Icons.Filled.TravelExplore, Cyan),
        ToolItem("HTTP Headers", "Review security headers", Icons.Filled.Security, Green),
        ToolItem("SSL / TLS", "Certificate overview", Icons.Filled.Lock, Purple),
        ToolItem("DNS Lookup", "Public DNS records", Icons.Filled.Dns, Amber),
        ToolItem("JWT Analyzer", "Decode token claims", Icons.Filled.Key, Pink),
        ToolItem("URL Inspector", "Parse URL components", Icons.Filled.Link, Cyan),
        ToolItem("API Auditor", "Auth & access checklist", Icons.Filled.Api, Green),
        ToolItem("Web Crawler", "Discover public links", Icons.Filled.AccountTree, Purple),
        ToolItem("Scan Reports", "Findings & history", Icons.Filled.Assessment, Amber)
    )

    Scaffold(
        containerColor = Night,
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF10172A), contentColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0; selectedTool = null },
                    icon = { Icon(Icons.Filled.Dashboard, null) },
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1; selectedTool = null },
                    icon = { Icon(Icons.Filled.History, null) },
                    label = { Text("History") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2; selectedTool = null },
                    icon = { Icon(Icons.Filled.Settings, null) },
                    label = { Text("Settings") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Night)
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(Cyan, Purple))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Bolt, null, tint = Night, modifier = Modifier.size(30.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("FELIX", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
                    Text("WEB SECURITY LAB", color = Muted, fontSize = 10.sp, letterSpacing = 2.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF18372F))
                        .padding(horizontal = 11.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Green))
                        Spacer(Modifier.width(6.dp))
                        Text("READY", color = Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (selectedTool != null) {
                ToolDetail(selectedTool!!) { selectedTool = null }
            } else if (selectedTab == 0) {
                Column(Modifier.fillMaxSize()) {
                    HeroCard()
                    Spacer(Modifier.height(18.dp))
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SECURITY TOOLS", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Text("${tools.size} MODULES", color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                        placeholder = { Text("Search tools...", color = Muted) },
                        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Muted) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Cyan,
                            unfocusedBorderColor = Color(0xFF2A3550)
                        )
                    )
                    val filtered = tools.filter {
                        it.title.contains(search, ignoreCase = true) ||
                                it.subtitle.contains(search, ignoreCase = true)
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filtered) { tool ->
                            ToolCard(tool) { selectedTool = tool }
                        }
                    }
                }
            } else {
                PlaceholderPage(
                    title = if (selectedTab == 1) "Scan History" else "Settings",
                    subtitle = if (selectedTab == 1)
                        "Your recent security checks will appear here."
                    else "Customize your security workspace."
                )
            }
        }
    }
}

@Composable
fun HeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF174D6A), Color(0xFF45306F), Color(0xFF20264A))))
            .padding(20.dp)
    ) {
        Column {
            Text("YOUR SECURITY COMMAND CENTER", color = Color(0xFFB5F4FF), fontSize = 10.sp, letterSpacing = 1.4.sp)
            Spacer(Modifier.height(10.dp))
            Text("Explore. Analyze.\nProtect.", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 32.sp)
            Spacer(Modifier.height(8.dp))
            Text("Tools for authorized web security testing.", color = Color(0xFFD3D9F2), fontSize = 12.sp)
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Shield, null, tint = Green, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(7.dp))
                Text("LOCAL DASHBOARD  •  READY TO USE", color = Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ToolCard(tool: ToolItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(19.dp))
            .background(Panel)
            .clickable(onClick = onClick)
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(tool.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(tool.icon, null, tint = tool.color, modifier = Modifier.size(23.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(tool.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(5.dp))
        Text(tool.subtitle, color = Muted, fontSize = 10.sp, lineHeight = 14.sp)
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("OPEN MODULE", color = tool.color, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Filled.ArrowForward, null, tint = tool.color, modifier = Modifier.size(13.dp))
        }
    }
}

@Composable
fun ToolDetail(tool: ToolItem, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("‹  Back to dashboard", color = Cyan, modifier = Modifier.clickable { onBack() })
        Box(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(tool.color.copy(alpha = .35f), Panel)))
                .padding(22.dp)
        ) {
            Column {
                Icon(tool.icon, null, tint = tool.color, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(14.dp))
                Text(tool.title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text(tool.subtitle, color = Muted)
            }
        }
        Text("MODULE OVERVIEW", color = tool.color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(
            "This screen is the UI entry point for this module. Connect a tested implementation " +
                    "to run checks against systems you own or are authorized to assess.",
            color = Color(0xFFD5DCEF), lineHeight = 22.sp
        )
        Text("Status: UI prototype — scan engine not connected", color = Amber, fontSize = 12.sp)
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = tool.color),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("MODULE READY FOR INTEGRATION", color = Night, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlaceholderPage(title: String, subtitle: String) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.AutoAwesome, null, tint = Purple, modifier = Modifier.size(54.dp))
        Spacer(Modifier.height(16.dp))
        Text(title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, color = Muted)
    }
}
