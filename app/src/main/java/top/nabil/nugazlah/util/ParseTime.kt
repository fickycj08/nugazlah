package top.nabil.nugazlah.util

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ParseTime {
    companion object {
        fun iso8601ToReadable(deadlineStr: String): String {
            val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
            val deadline = LocalDateTime.parse(deadlineStr, isoFormatter)
            val targetFormatter =
                DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy HH:mm:ss", Locale("id"))
            return deadline.atZone(ZoneId.of("Asia/Jakarta"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .format(targetFormatter)
        }

        @OptIn(ExperimentalMaterial3Api::class)
        fun formatDateToString(datePicked: DatePickerState): String {
            val selectedDate = Calendar.getInstance().apply {
                timeInMillis = datePicked.selectedDateMillis!!
            }

            val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
            return dateFormat.format(selectedDate.time)
        }

        @OptIn(ExperimentalMaterial3Api::class)
        fun formatTimeToString(time: TimePickerState): String {
            val hour = time.hour.toString().padStart(2, '0')
            val minute = time.minute.toString().padStart(2, '0')
            val second = "00"
            return "$hour:$minute:$second"
        }

        fun dateTimeStringToIso8601(date: String, time: String): String {
            val dateTimeFormat = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm:ss", Locale.getDefault())
            val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val dateTime = "$date $time"
            val parsedDate = dateTimeFormat.parse(dateTime)
            return iso8601Format.format(parsedDate!!)
        }


        fun scheduleAlarms(deadlineISO: String): List<Long> {
            val zoneId = ZoneId.of("Asia/Jakarta")
            val deadlineDateTime = LocalDateTime.parse(deadlineISO, DateTimeFormatter.ISO_DATE_TIME)
            val today = LocalDate.now()
            val hoursToDeadline = Duration.between(LocalDateTime.now(), deadlineDateTime).toHours()
            val alarms = mutableListOf<Long>()

            // h-7 day
            val alarm7DaysBefore = deadlineDateTime.minusDays(7)
            if (today.isBefore(alarm7DaysBefore.toLocalDate())) {
                alarms.add(alarm7DaysBefore.atZone(zoneId).toEpochSecond().times(1000))
            }

            // h-1 day
            val alarm1DayBefore = deadlineDateTime.minusDays(1)
            if (today.isBefore(alarm1DayBefore.toLocalDate())) {
                alarms.add(alarm1DayBefore.atZone(zoneId).toEpochSecond().times(1000))
            }

            // h-5 hours
            val alarm5HoursBefore = deadlineDateTime.minusHours(5)
            if (hoursToDeadline >= 5) {
                alarms.add(alarm5HoursBefore.atZone(zoneId).toEpochSecond().times(1000))
            }

            // deadline
            val deadlineInMillis = deadlineDateTime.atZone(zoneId).toEpochSecond() * 1000
            alarms.add(deadlineInMillis)

            return alarms
        }
    }
}