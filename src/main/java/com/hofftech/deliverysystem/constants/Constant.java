package com.hofftech.deliverysystem.constants;

public final class Constant {

    private Constant() {}

    public static final char EMPTY_CELL = ' ';
    public static final String HELP_TEXT = """
        Привет! Я бот для работы с посылками. Вот список доступных команд:

        /start - Приветственное сообщение и информация о боте.
        /help - Показать эту инструкцию.

        Основные команды для работы с посылками:
        -----------------------------
        /create -name "Название посылки" -form "форма посылки" -symbol "символ"
          Создаёт новую посылку с заданным названием, формой и символом.
          Пример: /create -name "Квадратное колесо" -form "xxx
        x x
        xxx" -symbol "o"

        /find "Название посылки"
          Находит и отображает информацию о посылке по её названию.
          Пример: /find "Посылка Тип 1"

        /edit -id "Старое имя" -name "Новое имя" -form "новая форма" -symbol "новый символ"
          Редактирует посылку с заданным именем.
          Пример: /edit -id "Квадратное колесо" -name "КУБ" -form "xxx
        xxx
        xxx" -symbol "%"

        /delete "Название посылки"
          Удаляет посылку по её названию.
          Пример: /delete "Посылка Тип 4"

        Для загрузки посылок:
        /load -parcels-text "Посылка 1
        Посылка 2" -trucks "3x3
        6x2" -type "Одна машина - Одна посылка" -out text
          Загрузка посылок с заданными размерами и стратегией в текстовом формате.

        Для выгрузки:
        /unload -infile "trucks.json" -outfile "parcels.csv" --withcount
          Выгружает посылки из файла с подсчетом количества.
          Пример: /unload -infile "trucks.json" -outfile "parcels-with-count.csv" --withcount

        Для работы с файлами:
        /load-file -parcels-file "parcels.csv" -trucks "3x3
        3x3
        6x2" -type "Одна машина - Одна посылка" -out json-file
          Загрузка посылок из файла в формате CSV и выгрузка в JSON.

        Примечание: Все команды следует вводить в точности, как указано, с учётом пробелов и кавычек.
        """;
}
