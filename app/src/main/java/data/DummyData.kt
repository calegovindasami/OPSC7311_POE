package data

import java.util.Date

class DummyData {
    fun generateDummyData(): List<ProjectViewModel> {
        val dummyData = mutableListOf<ProjectViewModel>()

        for (i in 1..10) {
            val project = ProjectViewModel(
                name = "Project $i",
                description = "This is project $i",
                startDate = Date(),
                endDate = Date(),
                minimumDailyHours = 4,
                maximumDailyHours = 8,
                null
            )

            dummyData.add(project)
        }

        return dummyData
    }

}