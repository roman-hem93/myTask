package ru.artrostudio.mytask.database.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// В этом классе происходит прямое взаимодействие с БД
// тут только самые глобальные общие методы
// в идеале полностью абстрагироваться от текущего приложения
// класс работает в параллельных потоках (корутинах)
class MySQLiteOpenHelper(context: Context, _requestCreateTables : String) : SQLiteOpenHelper(context, StructureBD.DATABASE_NAME, null, StructureBD.DATABASE_VERSION) {
    // context - контекст
    // _requestCreateTables - запрос на создание всех таблиц в БД

    private val requestCreateTables = _requestCreateTables

    // Обязательный метод
    // Вызывается при первом создании базы данных
    // Здесь должно происходить создание таблиц и их первоначальное заполнение
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(requestCreateTables)

        Log.i("Developer.BD","Выполнился метод onCreate SQLite (создание бд)")
    }

    // Обязательный метод
    // Вызывается, когда базу данных необходимо обновить (для удаления таблиц, добавления таблиц, изменения количества столбцов, а в целом нафиг нужно)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //удаление таблицы
        //db.execSQL("DROP TABLE IF EXISTS название удаляемой таблицы")

        //создание новой пустой таблицы
        //onCreate(db)

        Log.i("Developer.BD","Выполнился метод onUpgrade SQLite (обновление версии бд)")
    }

    // ниже самописные методы

    //ЗАПИСЬ В ТАБЛИЦУ
    fun insert(table: String, values: ContentValues, lambdaOKforBD: (Long)-> Unit, lambdaERRORforBD: ()-> Unit) {
        // table - таблица, в которую пишем
        // values данные в виде ключ-значение
        // lambdaOKforBD - лямбда-функция, выполняемая в случае успеха
        // lambdaERRORforBD - лямбда-функция, выполняемая в случае ошибок

        CoroutineScope(Dispatchers.IO).launch {// в потоке Dispatchers.IO выполняем тяжелую фоновую задачу
            // открывает связь с БД с правами на всё
            val db = this@MySQLiteOpenHelper.writableDatabase

            val newRowId = db.insert(table, null, values)

            // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
            db.close()

            Log.i("Developer.BD","Выполнена запись в  БД. Таблица: $table, id: $newRowId, данные: $values")

            CoroutineScope(Dispatchers.Main).launch {// после окончания тяжелой задачи выполняем в потоке Dispatchers.Main то, что взаимодействует с интерфейсом
                // возвращаем id новой записи
                lambdaOKforBD(newRowId)
            }
        }
    }

    //ЧТЕНИЕ ДАННЫХ
    fun query(table: String, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, groupBy: String?, having: String?, sortOrder: String?, lambdaOKforBD: (ArrayList<ContentValues>)-> Unit, lambdaERRORforBD: ()-> Unit) {
        // table - таблица, в которой роемся
        // columns - возвращаемые столбцы или null чтобы вернуть все (ПРИМЕР: columns = arrayOf("nameColumn", "nameColumn2") или columns = null)
        // selection - столбцы, учитываемые в фильрации WHERE или null чтобы не фильтровать (ПРИМЕР: selection = "nameColumn = ?, nameColumn2 = ?" )
        // selectionArgs - значения для столбцов WHERE  (ПРИМЕР selectionArgs = arrayOf("5", "10")) // значения подставятся вместо знаков вопроса в selection
        //                 допускается не использовать selectionArgs, а указать полное выражение WHERE в whereClause без знаков ?, но тогда нужно selectionArgs=null
        // groupBy - группировка - это что такое?
        // having - фильтрация ГРУПП (т.е. работает в паре с groupBy) или null (ПРИМЕР having = "nameColumn > 5")
        // sortOrder - порядок сортировки или null (ПРИМЕР sortOrder = "nameColumn DESC")
        // lambdaOKforBD - лямбда-функция, выполняемая в случае успеха
        // lambdaERRORforBD - лямбда-функция, выполняемая в случае ошибок

        CoroutineScope(Dispatchers.IO).launch {// в потоке Dispatchers.IO выполняем тяжелую фоновую задачу
            // открывает связь с БД с правами на чтение (на практике запись и удаление тоже сработали, надо разбираться)
            val db = this@MySQLiteOpenHelper.readableDatabase
            val cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, sortOrder)

            val list = ArrayList<ContentValues>()

            // проходимся по полученным данным
            with(cursor) {
                while (moveToNext()) {
                    // из результата получаем массив с названиями столбцов
                    val values = ContentValues()

                    val columns = cursor.columnNames

                    // собираем всю инфу из строки
                    for (colum in columns) {
                        values.put(colum, getString(getColumnIndexOrThrow(colum)))
                    }
                    list.add(values)
                }
            }
            // закрываем курсор
            cursor.close()

            // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
            db.close()

            Log.i("Developer.BD","Выполнена вызгрузка из БД. Из таблицы: $table, данные: $list")

            CoroutineScope(Dispatchers.Main).launch {// после окончания тяжелой задачи выполняем в потоке Dispatchers.Main то, что взаимодействует с интерфейсом
                lambdaOKforBD(list)
            }
        }
    }

    // ОБНОВЛЕНИЕ ДАННЫХ
    fun update(table: String, values: ContentValues, whereClause: String?, whereArgs: Array<String>?, lambdaOKforBD: ()-> Unit, lambdaERRORforBD: ()-> Unit) {
        // table - таблица, в которой обновляем данные
        // values -  данные в виде ключ-значение
        // whereClause - столбцы, учитываемые в фильрации WHERE или null чтобы не фильтровать (ПРИМЕР: whereClause = "nameColumn = ?, nameColumn2 = ?" )
        // whereArgs - значения для столбцов WHERE  (ПРИМЕР whereArgs = arrayOf("5", "10")) // значения подставятся вместо знаков вопроса в whereClause
        //             допускается не использовать whereArgs, а указать полное выражение WHERE в whereClause без знаков ?, но тогда нужно whereArgs=null
        // lambdaOKforBD - лямбда-функция, выполняемая в случае успеха
        // lambdaERRORforBD - лямбда-функция, выполняемая в случае ошибок

        CoroutineScope(Dispatchers.IO).launch {// в потоке Dispatchers.IO выполняем тяжелую фоновую задачу
            // открывает связь с БД с правами на всё
            val db = this@MySQLiteOpenHelper.writableDatabase

            // выполняем запрос обновления данных (возвращает количество затронутых строк)
            val updatedRows = db.update(table, values, whereClause, whereArgs)

            // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
            db.close()

            Log.i("Developer.BD","Обновлена информация в БД. Количество затрунутых строк: $updatedRows")

            CoroutineScope(Dispatchers.Main).launch {// после окончания тяжелой задачи выполняем в потоке Dispatchers.Main то, что взаимодействует с интерфейсом
                lambdaOKforBD()
            }
        }
    }

    // УДАЛЕНИЕ ДАННЫХ
    fun delete(table: String, whereClause: String?, whereArgs: Array<String>?, lambdaOKforBD: ()-> Unit, lambdaERRORforBD: ()-> Unit) {
        // table - таблица, в которой удаляем данные
        // whereClause - столбцы, учитываемые в фильрации WHERE или null чтобы не фильтровать (ПРИМЕР: whereClause = "nameColumn = ?, nameColumn2 = ?" )
        // whereArgs - значения для столбцов WHERE  (ПРИМЕР whereArgs = arrayOf("5", "10")) // значения подставятся вместо знаков вопроса в whereClause
        //             допускается не использовать whereArgs, а указать полное выражение WHERE в whereClause без знаков ?, но тогда нужно whereArgs=null
        // lambdaOKforBD - лямбда-функция, выполняемая в случае успеха
        // lambdaERRORforBD - лямбда-функция, выполняемая в случае ошибок

        CoroutineScope(Dispatchers.IO).launch {// в потоке Dispatchers.IO выполняем тяжелую фоновую задачу
            // открывает связь с БД с правами на всё
            val db = this@MySQLiteOpenHelper.writableDatabase

            val deletedRows = db.delete(table, whereClause, whereArgs)

            // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
            db.close()

            Log.i("Developer.BD","Удалили из БД столько строк: $deletedRows")

            CoroutineScope(Dispatchers.Main).launch {// после окончания тяжелой задачи выполняем в потоке Dispatchers.Main то, что взаимодействует с интерфейсом
                lambdaOKforBD()
            }
        }
    }

    // ПЕЧАТАЕТ В ПРОТОКОЛ ТАБЛИЦУ
    fun printTable(table: String) {
        // table - таблица, которую напечатаем

        val lambdaOKforBD : (ArrayList<ContentValues>)-> Unit = {result: ArrayList<ContentValues> ->
            Log.i("Developer.BD","------------------ Начинается вавод таблицы $table")
            var i = 0
            for (row in result) {
                // запрашиваем набор всех ключей в этой строке
                val keys = row.keySet()

                var str : String = ""

                for (key in keys) {
                    val value = row.getAsString(key)
                    str = str + "$key=$value "
                }

                Log.i("Developer.BD","Строка $i: $str")

                i++
            }

            Log.i("Developer.BD","------------------ Вывод таблицы $table окончен")
        }
        val lambdaERRORforBD : ()-> Unit = {}

        // выгружаем таблицу целиком
        val result = this.query(table, null, null, null, null, null, null, lambdaOKforBD, lambdaERRORforBD)
    }

    //ПРОИЗВОЛЬНЫЙ ЗАПРОС
    fun requestSQL(request : String, lambdaOKforBD: ()-> Unit, lambdaERRORforBD: ()-> Unit) {
        // request - произвольный запрос
        // lambdaOKforBD - лямбда-функция, выполняемая в случае успеха
        // lambdaERRORforBD - лямбда-функция, выполняемая в случае ошибок

        CoroutineScope(Dispatchers.IO).launch {// в потоке Dispatchers.IO выполняем тяжелую фоновую задачу
            // открывает связь с БД с правами на всё
            val db = this@MySQLiteOpenHelper.writableDatabase

            db.execSQL(request)

            // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
            db.close()

            Log.i("Developer.BD","Выполнен произвольный запрос к БД")

            CoroutineScope(Dispatchers.Main).launch {// после окончания тяжелой задачи выполняем в потоке Dispatchers.Main то, что взаимодействует с интерфейсом
                lambdaOKforBD()
            }
        }
    }

}