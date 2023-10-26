package com.gm.ai.guidebook.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.gm.ai.guidebook.R
import com.gm.ai.guidebook.ui.components.GuideSearch
import com.gm.ai.guidebook.ui.theme.GuideBookTheme
import com.gm.ai.guidebook.ui.theme.GuideTheme

/**
 * Created by gle.bushkaa email(gleb.mokryy@gmail.com) on 10/26/2023
 */

@Preview
@Composable
private fun HomeScreenPreview() {
    GuideBookTheme(darkTheme = true) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(
    state: HomeState = HomeState(),
    onEvent: (HomeEvent) -> Unit = {},
) {
    val mediumOffset = GuideTheme.offset.medium
    val regularOffset = GuideTheme.offset.regular
    val superGiganticOffset = GuideTheme.offset.superGigantic

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(GuideTheme.palette.background)
            .padding(regularOffset),
        constraintSet = homeScreenDecoupledConstraints(
            mediumOffset = mediumOffset,
            regularOffset = regularOffset,
            superGiganticOffset = superGiganticOffset,
        ),
    ) {
        GuideSearch(
            modifier = Modifier.layoutId(HOME_SEARCH_BAR),
            value = state.searchQuery,
            onValueChanged = {
                val event = HomeEvent.SendSearchQuery(it)
                onEvent(event)
            },
        )
        AnimatedVisibility(
            modifier = Modifier.layoutId(NO_GUIDES_ITEM),
            visible = state.guides.isEmpty(),
            enter = fadeIn(
                animationSpec = tween(800),
            ),
            exit = fadeOut(
                animationSpec = tween(200),
            ),
        ) {
            NoGuideItems(
                modifier = Modifier,
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .padding(bottom = regularOffset)
                .layoutId(GUIDES_LIST),
            visible = state.guides.isNotEmpty(),
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(800),
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(800),
            ),
        ) {
            GuidesList(list = state.guides)
        }
    }
}

@Composable
private fun NoGuideItems(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = GuideTheme.offset.medium)
                .size(100.dp),
            painter = painterResource(id = R.drawable.ic_book),
            contentDescription = null,
            tint = GuideTheme.palette.onBackground.copy(
                alpha = 0.8f,
            ),
        )
        Text(
            text = "No guides found",
            style = GuideTheme.typography.bodyLarge,
            color = GuideTheme.palette.onBackground,
        )
    }
}

private fun homeScreenDecoupledConstraints(
    mediumOffset: Dp,
    regularOffset: Dp,
    superGiganticOffset: Dp,
) = ConstraintSet {
    val homeSearchBar = createRefFor(HOME_SEARCH_BAR)
    val noGuidesItem = createRefFor(NO_GUIDES_ITEM)
    val guidesList = createRefFor(GUIDES_LIST)

    constrain(homeSearchBar) {
        top.linkTo(anchor = parent.top, margin = mediumOffset)
        start.linkTo(anchor = parent.start)
        end.linkTo(anchor = parent.end)
    }

    constrain(noGuidesItem) {
        top.linkTo(anchor = homeSearchBar.bottom)
        start.linkTo(anchor = parent.start)
        end.linkTo(anchor = parent.end)
        bottom.linkTo(anchor = parent.bottom, margin = superGiganticOffset)
    }

    constrain(guidesList) {
        top.linkTo(anchor = homeSearchBar.bottom, margin = regularOffset)
        start.linkTo(anchor = parent.start)
        end.linkTo(anchor = parent.end)
    }
}

private const val HOME_SEARCH_BAR = "home_search_bar"

private const val NO_GUIDES_ITEM = "no_guides_item"
private const val GUIDES_LIST = "guides_list"
