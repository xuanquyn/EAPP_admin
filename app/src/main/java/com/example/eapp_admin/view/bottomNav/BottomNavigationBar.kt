package com.example.eapp_admin.view.bottomNav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eapp_admin.R

enum class BottomNavItem(
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    Dashboard(
        route = "dashboard",
        selectedIcon = BottomNavIcons.DashboardSelected,
        unselectedIcon = BottomNavIcons.DashboardUnselected
    ),
    ManageWord(
        route = "manageword",
        selectedIcon = BottomNavIcons.MnWordSelected,
        unselectedIcon = BottomNavIcons.MnWordUnSelected
    ),
    ManageUser(
        route = "manageuser",
        selectedIcon = BottomNavIcons.MnUserSelected,
        unselectedIcon = BottomNavIcons.MnUserUnSelected
    ),
    ManageRevenue(
        route = "managerevenue",
        selectedIcon = BottomNavIcons.MnRevenueSelected,
        unselectedIcon = BottomNavIcons.MnRevenueUnselected
    );
}

@Composable
fun BottomNavigationBar(currentRoute: String, onTabSelected: (BottomNavItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xfff2f1eb))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem.values().forEach { item ->
            val isSelected = currentRoute == item.route
            Icon(
                painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                contentDescription = item.route,
                modifier = Modifier
                    .size(45.dp)
                    .clickable { onTabSelected(item) },
                tint = Color.Unspecified
            )
        }
    }
}



