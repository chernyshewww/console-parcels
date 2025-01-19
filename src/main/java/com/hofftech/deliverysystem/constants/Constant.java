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
            /load -u "user" -parcels-file "parcels.csv" -trucks "3x3
            3x3
            10x10
            5x5" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
              Загрузка посылок из файла с заданными размерами, стратегией и сохранением в JSON.
              Пример: /load -u "usertest" -parcels-file "parcels.csv" -trucks "3x3\\n3x3\\n10x10\\n5x5" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
            
            Для выгрузки:
            /unload -u "user" -infile "trucks.json" -outfile "parcels-with-count.csv" --withcount
              Выгружает посылки из файла с подсчётом количества.
              Пример: /unload -u "usertest" -infile "trucks.json" -outfile "parcels-with-count.csv" --withcount
            
            Для просмотра биллинга:
            /billing -u "user" -from DD.MM.YYYY -to DD.MM.YYYY
              Отображает биллинг пользователя за указанный период.
              Пример: /billing -u "usertest" -from 11.01.2025 -to 19.01.2025
            
            Примечание: Все команды следует вводить в точности, как указано, с учётом пробелов и кавычек.
            
        """;
}
