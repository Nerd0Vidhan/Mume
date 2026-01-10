package com.lokal.mume.presentation.sort

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SortPopup(
    expanded: Boolean,
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(200.dp)
            .background(
                color = Color(0xFF1C1C1E),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        SortOption.values().forEach { option ->
            SortPopupItem(
                option = option,
                isSelected = option == selectedOption,
                onClick = {
                    onOptionSelected(option)
                    onDismiss()
                }
            )
        }
    }
}
