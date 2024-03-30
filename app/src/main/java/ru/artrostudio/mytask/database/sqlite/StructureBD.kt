package ru.artrostudio.mytask.database.sqlite

object StructureBD {
    // имя и версия таблицы
    const val DATABASE_NAME = "bdMyTast"
    const val DATABASE_VERSION = 1

    // Константы с запросами на создание структуры БД
    // создание таблицы с задачами
    // вообще нужно реализовать метод, который на основе структуры таблиц (описаны ниже) будет составлять запрос на создание таблицы
    const val SQL_CREATE_TABLE_TASKS =
        "CREATE TABLE ${tableTasks.TABLE_NAME} (" +
                // общие колонки для всех таблиц
                "${allTable.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                "${allTable.COLUMN_NAME_STATUS_ITEM} INTEGER," +
                "${allTable.COLUMN_NAME_DATE_CREATION} INTEGER," +
                "${allTable.COLUMN_NAME_DATE_CHANGE} INTEGER," +
                "${allTable.COLUMN_NAME_DATE_DELETION} INTEGER," +
                //колонки конкретно этой таблицы
                "${tableTasks.COLUMN_NAME_TITLE} TEXT," +
                "${tableTasks.COLUMN_NAME_DESCRIPTION} TEXT," +
                "${tableTasks.COLUMN_NAME_DATE} TEXT," +
                "${tableTasks.COLUMN_NAME_STATUS} TEXT)"

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