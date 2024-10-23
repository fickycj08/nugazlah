package org.d3ifcool.nugazyuk.data.model

import org.d3ifcool.nugazyuk.ui.theme.BlackPlaceholder
import org.d3ifcool.nugazyuk.ui.theme.GreenChill
import org.d3ifcool.nugazyuk.ui.theme.GreenDone
import org.d3ifcool.nugazyuk.ui.theme.OrangeAlert
import org.d3ifcool.nugazyuk.ui.theme.OrangeGuru
import org.d3ifcool.nugazyuk.ui.theme.RedDanger

val deadlineColor = mapOf(
    Pair(DeadlineType.DONE, GreenDone),
    Pair(DeadlineType.SANS, GreenChill),
    Pair(DeadlineType.WARN, OrangeAlert),
    Pair(DeadlineType.NINUNINU, OrangeGuru),
    Pair(DeadlineType.MISSED, RedDanger),
    Pair(DeadlineType.UNKNOWN, BlackPlaceholder),
)
