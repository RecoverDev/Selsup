# CrptApi - проект по работе с API сайта "Честный знак"

## Описание

Конструктор принимает два параметра
* __TimeUnit__ - интервал времени, в котором действует огрпничение на количество потоков
* __int__ - максимальное количество потоков

### Класс __CrptApi__ содержит методы

* __startTask(Document *document*, String *signature*)__ - добавляет в пул новый поток, где *document* - передаваемый документ, *signature* - электронная подпись
* __getResult()__ - получает результат работы потоков
* __closePool()__ - завершает работу пула потоков

### Функционал реализован с использованием следующих классов

Класс __Request__ реализует интерфейс __Callable<String>__ и содержит метод выполняющий __HTTP__ запрос методом __POST__. В качестве тела запроса передается документ и электронная подпись. Документ, электронная подпись и адрес __(URL)__ передаются в класс в качестве параметров конструктора.

Класс __JsonSerialization__ преобразует объекты в __JSON__ и обратно. Использует __ObjectMapper__ из пакета __Jackson__. 
Методы:
* __String serialization(T object)__ - создает __JSON__s на основе переданного объекта
* __T deSerialization(String json,Class<T> value)__ - создает объект заданного класса на основе __JSON__

Класс __ManagerPool__ создает пул потоков объектов класса __Request__.
Методы:
* __addTask(Callable<> task)__ - добавляет новый поток, параметр *task* класс, реализующий интерфейс __Callable__
* __giveRsult(Visitor<> visitor)__ - получает результат работы потоков, для получения результатов реализован шаблон __Посетитель__
* __close()__ - завершает работу ппула потоков.

Интерфейс __Visitor__ реализует шаблон __Посетитель__. Используется для получения результатов работы потоков из класса __ManagerPool__.

Класс __ConsoleVisitorResult__ реализует интерфейс __Visitor__. Выводит в консоль результат выполнения потоков. Передается в качестве параметра в метод __giveResult__ класса __ManagerPool__.

Классы __Document__, __Description__, __Product__ описывают структуру документа, передаваемого для регистрации на сайт "Честный знак".

Проект собран с использованием __Maven__



