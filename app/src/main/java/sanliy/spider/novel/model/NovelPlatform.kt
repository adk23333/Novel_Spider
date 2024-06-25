package sanliy.spider.novel.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import sanliy.spider.novel.R

enum class NovelPlatform(
    val zh: String,
    @DrawableRes val drawableID: Int,
    @StringRes val stringID: Int,
) {
    SFACG(
        "菠萝包",
        R.drawable.ic_action_sfacg,
        R.string.home_sfacg
    ),
    CI_WEI_MAO(
        "刺猬猫",
        R.drawable.ic_action_ciweimao,
        R.string.home_ciweimao
    );
}