package ru.artrostudio.mytask.database.sqlite

object StructureBD {
    const val DATABASE_NAME = "bdMyTast"
    const val DATABASE_VERSION = 1


    object allTable {

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_STATUS_ITEM = "status_item"
        const val COLUMN_NAME_DATE_CREATION = "date_creation"
        const val COLUMN_NAME_DATE_CHANGE = "date_change"
        const val COLUMN_NAME_DATE_DELETION = "date_deletion"

    }

    object tableTasks {
        const val TABLE_NAME = "tasks"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_STATUS = "status"
    }
}