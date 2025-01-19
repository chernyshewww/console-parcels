## ПЕРЕД ИСПОЛЬЗОВАНИЕМ НЕОБХОДИМО ЗАПОЛНИТЬ bot.token И bot.username в application.properties

# Команды для управления посылками и грузовиками ЧЕРЕЗ ТЕЛЕГРАМ

Этот сервис позволяет управлять посылками, грузовиками и их загрузкой/разгрузкой через команды в Telegram-боте.

## Команды

### 1. **Создание посылки**
**Команда**
```text
/create -name "Посылка Тип 4" -form "xxx\nxxx\nxxx" -symbol "0"
```

**Описание**  
Создает новую посылку с заданным именем, формой и символом. Формат формы задается как строка, где `x` обозначает части посылки.

**Пример**
```csv
id(name): "Посылка Тип 4"
form:
000
000
000
```

---

### 2. **Поиск посылки**
**Команда**
```text
/find "Посылка Тип 4"

```
**Описание**  
Ищет посылку по имени и отображает ее форму.

**Пример**
```csv
Name:Посылка Тип 4
Form:
000
000
000
```

---

### 3. **Редактирование посылки**
**Команда**
```text
/edit -id "Посылка Тип 4" -name "КУБ22" -form "xxxxx\nxxxxx\nxxxx" -symbol "l"

```

**Описание**  
Редактирует существующую посылку, меняет ее имя, форму и символ.

**Пример**
```csv
Name: КУБ22
Form:
lllll
lllll
llll
Symbol: l
```
---

### 4. **Удаление посылки**
**Команда**
```text
/delete "Посылка Тип 4"
```

**Описание**  
Удаляет посылку по имени.

**Пример**
```csv
"Посылка Тип 4" удалена.
```
---

### 5. **Погрузка посылок в грузовики (вывод в текст)**
**Команда**
```text
/load -parcels-file "parcels.csv" -trucks "3x3\n10x10" -type "Максимальная вместимость" -out text
```

**Описание**  
Загружает посылки из файла в грузовики по заданной стратегии. Стратегия "Максимальная вместимость" будет пытаться заполнить грузовики до предела.

**Пример**
```csv
Кузов грузовика:
+   +
+333+
+333+
+333+
+++++
Посылка: Посылка тип 2
Координаты размещения: (0, 0), (0, 1), (0, 2), (1, 0), (1, 1), (1, 2), (2, 0), (2, 1), (2, 2)
---------------
Кузов грузовика:
+          +
+          +
+          +
+          +
+          +
+lllll     +
+lllll     +
+lllll     +
+%%%%%%%   +
+%%%%%%%   +
+%%%%%%%   +
++++++++++++
Посылка: КУБ
Координаты размещения: (7, 0), (7, 1), (7, 2), (7, 3), (7, 4), (7, 5), (7, 6), (8, 0), (8, 1), (8, 2), (8, 3), (8, 4), (8, 5), (8, 6), (9, 0), (9, 1), (9, 2), (9, 3), (9, 4), (9, 5), (9, 6)
Посылка: КУБ22
Координаты размещения: (4, 0), (4, 1), (4, 2), (4, 3), (4, 4), (5, 0), (5, 1), (5, 2), (5, 3), (5, 4), (6, 0), (6, 1), (6, 2), (6, 3), (6, 4)
---------------
```
---

### 6. **Погрузка посылок в грузовики (вывод в текст) - другая стратегия**
**Команда**
```text
/load -u "user" -parcels-file "parcels.csv" -trucks "3x3\n10x10\n5x5\n5x5" -type "Одна машина - Одна посылка" -out text
```
**Описание**  
Загружает одну посылку в один грузовик для каждой посылки. Стратегия "Одна машина - Одна посылка" размещает по одной посылке в каждом грузовике.

**Пример**
```csv
Кузов грузовика:
+   +
+333+
+333+
+333+
+++++
Посылка: Посылка тип 2
Координаты размещения: (0, 0), (0, 1), (0, 2), (1, 0), (1, 1), (1, 2), (2, 0), (2, 1), (2, 2)
---------------
Кузов грузовика:
+          +
+          +
+          +
+          +
+          +
+          +
+          +
+          +
+%%%%%%%   +
+%%%%%%%   +
+%%%%%%%   +
++++++++++++
Посылка: КУБ
Координаты размещения: (7, 0), (7, 1), (7, 2), (7, 3), (7, 4), (7, 5), (7, 6), (8, 0), (8, 1), (8, 2), (8, 3), (8, 4), (8, 5), (8, 6), (9, 0), (9, 1), (9, 2), (9, 3), (9, 4), (9, 5), (9, 6)
---------------
Кузов грузовика:
+     +
+     +
+     +
+lllll+
+lllll+
+lllll+
+++++++
Посылка: КУБ22
Координаты размещения: (2, 0), (2, 1), (2, 2), (2, 3), (2, 4), (3, 0), (3, 1), (3, 2), (3, 3), (3, 4), (4, 0), (4, 1), (4, 2), (4, 3), (4, 4)
---------------
Кузов грузовика:
+     +
+     +
+     +
+     +
+     +
+     +
+++++++

```

---

### 7. **Погрузка посылок в грузовики (вывод в json)**
**Команда**
```text
/load -u "user" -parcels-file "parcels.csv" -trucks "3x3\n3x3\n10x10\n5x5" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
```

**Описание**  
Загружает посылки и выводит результат в формате JSON в файл.

**Пример (файл trucks.json)**
```json
[
  {
    "truck_type": "3x3",
    "parcels": [
      {
        "name": "1",
        "coordinates": [
          [0, 0]
        ]
      }
    ]
  },
  {
    "truck_type": "3x3",
    "parcels": [
      {
        "name": "КУБ",
        "coordinates": [
          [0, 0],
          [0, 1],
          [0, 2],
          [1, 0],
          [1, 1],
          [1, 2],
          [2, 0],
          [2, 1],
          [2, 2]
        ]
      }
    ]
  },
  {
    "truck_type": "10x10",
    "parcels": [
      {
        "type": "4",
        "coordinates": [
          [0, 0],
          [1, 0],
          [2, 0],
          [3, 0]
        ]
      }
    ]
  }
]
```
### 8. Разгрузка посылок (с подсчетом)
**Команда**

```text
/unload -u "user"-infile "trucks.json" -outfile "parcels-with-count.csv" --withcount
````

**Описание**

Разгружает посылки из файла и выводит их в CSV-файл с подсчетом количества каждой посылки.

**Пример**

parcels-with-count.csv

```csv
"Посылка Тип 1"  
"Посылка Тип 4"  
"КУБ" 
``` 
### 9. Разгрузка посылок (без подсчета)

**Команда**

```text
/unload -u "user" -infile "trucks.json" -outfile "parcels.csv"
```
**Описание**

Разгружает посылки из файла и выводит их в CSV-файл без подсчета.

**Пример**

parcels.csv

```csv
"Посылка Тип 1"  
"Посылка Тип 4"  
"КУБ" 
``` 

### 10. Биллинг посылок

**Команда**

```text
/billing -u "usertest" -from "11.01.2025" -to "19.01.2025"
```
**Описание**

Производит расчет биллинга по пользователю

**Пример**

```csv
2025-01-19; Погрузка; 2 машин; 2 посылок; 500 рублей
2025-01-19; Погрузка; 1 машин; 2 посылок; 300 рублей
2025-01-19; Погрузка; 2 машин; 2 посылок; 500 рублей
2025-01-19; Разгрузка; 2 машин; 2 посылок; 560 рублей
2025-01-19; Разгрузка; 2 машин; 2 посылок; 560 рублей
``` 

# Есть возможность писать команды через shell. 

Через shell используется другой синтаксис команд

```bash
--u = --user
--parsels-file = --file
--parsels-text = --parcels
--out-filename = --output
--withcount = --count
```

Для остальных команд надо добавить вместо одной черты перед аргументом две.