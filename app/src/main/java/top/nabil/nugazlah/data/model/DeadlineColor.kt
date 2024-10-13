package top.nabil.nugazlah.data.model

import top.nabil.nugazlah.ui.theme.BlackPlaceholder
import top.nabil.nugazlah.ui.theme.BlackTypo
import top.nabil.nugazlah.ui.theme.GreenChill
import top.nabil.nugazlah.ui.theme.GreenDone
import top.nabil.nugazlah.ui.theme.OrangeAlert
import top.nabil.nugazlah.ui.theme.OrangeGuru
import top.nabil.nugazlah.ui.theme.RedDanger

val deadlineColor = mapOf(
    Pair(DeadlineType.DONE, GreenDone),
    Pair(DeadlineType.SANS, GreenChill),
    Pair(DeadlineType.WARN, OrangeAlert),
    Pair(DeadlineType.NINUNINU, OrangeGuru),
    Pair(DeadlineType.MISSED, RedDanger),
    Pair(DeadlineType.UNKNOWN, BlackPlaceholder),
)
