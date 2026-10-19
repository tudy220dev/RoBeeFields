package com.example.hivepoint.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hivepoint.R
import com.example.hivepoint.model.ChangelogCategory
import com.example.hivepoint.model.ChangelogEntry
import com.example.hivepoint.model.ChangelogRepository
import com.example.hivepoint.model.ChangelogSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    selectedLanguage: String = "Română",
    onOpenDrawer: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ChangelogCategory.ALL) }
    val isEnglish = selectedLanguage == "English"

    val filteredEntries = remember(selectedCategory) {
        if (selectedCategory == ChangelogCategory.ALL) {
            ChangelogRepository.entries
        } else {
            ChangelogRepository.entries.filter { it.category == selectedCategory }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEnglish) "About RoBee Fields" else "Despre RoBee Fields")
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = if (isEnglish) "Menu" else "Meniu"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Info Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.main_logo),
                                contentDescription = "RoBee Fields Logo",
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Column {
                                Text(
                                    text = "RoBee Fields",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (isEnglish)
                                        "Navigation System, Offline-First & Decentralized"
                                    else
                                        "Sistem De navigare, Offline-First și Descentralizat",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        Text(
                            text = if (isEnglish)
                                "Developed by Radu Tudor-Andrei and Icătoiu Ștefan (2nd Year Robotics Students) for beekeepers and field operations without internet connection."
                            else
                                "Dezvoltat de Radu Tudor-Andrei și Icătoiu Ștefan (Studenți Robotică Anul 2) pentru apicultori și intervenții pe teren fără conexiune la internet.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = if (isEnglish)
                                "Principles: Zero central servers • Pure local storage • Compact P2P SMS/mesh transfer."
                            else
                                "Principii: Zero servere centrale • Stocare locală pură • Transfer P2P SMS/mesh compact.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Changelog Header & Category Filters
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Changelog",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isEnglish)
                                    "RoBee Fields Update History"
                                else
                                    "Istoricul actualizărilor RoBee Fields",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Scalable Category Filter Chips
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ChangelogCategory.entries.forEach { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { selectedCategory = category }
                            ) {
                                Text(
                                    text = category.label,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Timeline Entries
            itemsIndexed(filteredEntries) { index, entry ->
                TimelineEntryItem(
                    entry = entry,
                    selectedLanguage = selectedLanguage,
                    isFirst = index == 0,
                    isLast = index == filteredEntries.lastIndex
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TimelineEntryItem(
    entry: ChangelogEntry,
    selectedLanguage: String,
    isFirst: Boolean,
    isLast: Boolean
) {
    val lineColor = MaterialTheme.colorScheme.outlineVariant
    val dotColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Left Column: Date & Version
        Column(
            modifier = Modifier
                .width(90.dp)
                .padding(top = 2.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = entry.getDate(selectedLanguage),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = entry.version,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Center Axis Column: Dot and Vertical Timeline Line
        Box(
            modifier = Modifier
                .width(16.dp)
                .fillMaxHeight()
                .drawWithContent {
                    val circleCenterY = 10.dp.toPx()
                    val circleRadius = 5.dp.toPx()
                    val strokeWidth = 2.dp.toPx()
                    val centerX = size.width / 2f

                    // Draw vertical line top to bottom
                    val startY = if (isFirst) circleCenterY else 0f
                    val endY = if (isLast) circleCenterY else size.height

                    drawLine(
                        color = lineColor,
                        start = Offset(centerX, startY),
                        end = Offset(centerX, endY),
                        strokeWidth = strokeWidth
                    )

                    // Draw node dot
                    drawCircle(
                        color = dotColor,
                        radius = circleRadius,
                        center = Offset(centerX, circleCenterY)
                    )

                    // Inner white/surface accent in the dot
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = Offset(centerX, circleCenterY)
                    )
                }
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: Changelog Details Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category Tag Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = entry.getTag(selectedLanguage),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                // Title
                Text(
                    text = entry.getTitle(selectedLanguage),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                // Description
                entry.getDescription(selectedLanguage)?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                    )
                }

                // Sections (New, Improved, Updated, etc.)
                entry.sections.forEach { section ->
                    ChangelogSectionView(
                        section = section,
                        selectedLanguage = selectedLanguage
                    )
                }
            }
        }
    }
}

@Composable
fun ChangelogSectionView(
    section: ChangelogSection,
    selectedLanguage: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = section.getTitle(selectedLanguage),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        section.getItems(selectedLanguage).forEach { itemText ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "• ",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = itemText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }
    }
}
