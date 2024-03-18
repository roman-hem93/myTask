package ru.artrostudio.mytask.database.sqlite

object StructureBD {
    // имя и версия таблицы
    const val DATABASE_NAME = "bdMyTast"
    const val DATABASE_VERSION = 1

    // системные столбцы (создаются в каждой таблице)
    object allTable {

        const val COLUMN_NAME_ID = "system_id"                          // id
        const val COLUMN_NAME_STATUS_ITEM = "system_status_item"        // статус строки (0 - считается удалённой, 1 - актуальная, ....)
        const val COLUMN_NAME_DATE_CREATION = "system_date_creation"    // дата создания
        const val COLUMN_NAME_DATE_CHANGE = "system_date_change"        // дата изменения
        const val COLUMN_NAME_DATE_DELETION = "system_date_deletion"    // дата удаления (выставления статуса=0)

    }

    // структура таблицы с задачами
    object tableTasks {
        const val TABLE_NAME = "tasks"                           // название таблицы
        const val COLUMN_NAME_TITLE = "title"                    // столбец: заголовок
        const val COLUMN_NAME_DESCRIPTION = "description"        // столбец: описание
        const val COLUMN_NAME_DATE = "date"                      // столбец: дата задачи
        const val COLUMN_NAME_STATUS = "status"                  // столбец: статус задачи
    }
}