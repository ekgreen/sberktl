# Спринт #3

## Домашняя работа Исключения в Kotlin
В задаче напишем валидацию пользовательских данных. <br>
Все ошибки должны быть собраны и проверены в тестах. <br>

### Правила валидации:
**Имя и Фамилия** - только кириллица, не более 16 символов каждое поле <br>
**Телефон** - начинается с 7 или 8ки, только цифры, 11 знаков <br>
**Email** - латиница, с валидацией @имя_домена, не более 32 символов <br>
**СНИЛС** - только цифры, 11 символов, с валидацией Контрольного числа <br>

### Требования к реализации
Особых требований нет. Должен быть протестирован функционал ClientService.saveClient <br/>
Исключения, валидаторы, их порядок и реализация может быть любой (пример можно удалить).<br/>
Примеры пользовательских данных могут быть исправлены/дополнены.<br/>

### Результат
В ClientServiceTest не менее 6 тестов. <br/>
1 - полностью валидные данные <br/>
5 - разный набор ошибок <br/>

## Домашняя работа Время в Kotlin
1) Получить сет часовых поясов, которые используют смещение от UTC не в полных часах.
2) Для заданного года вывести список, каким днем недели был последний день в месяце.
3) Для заданного года вывести количество дней, выпадающих на пятницу 13-ое.
4) Вывести заданную дату в формате "01 Aug 2021, 23:39", в котором дата локализована для вывода в США (US).

## Домашняя работа Функции в Kotlin

1) Необходимо написать функцию, которая создает функции возведения в нужную степень.
   Должен быть реализован метод `ru.sber.functional.PowFactory.buildPowFunction`

2) Есть группа студентов `ru.sber.functional.StudentsGroup`.
   Необходимо:
    * написать функцию, которая будет фильтровать по лямбде-предикату, переданной как аргумент.
    * проинициализировать этот класс студентами, у которых обязательно указывается: фамилия, имя и средний бал.
      Все остальные свойства инициализируются значением-заглушкой(пример: "Специализация отсутствует")


## Домашняя работа Genetics в Kotlin

1) Реализовать обобщенную функцию compare, которая сравнивала бы два объекта класса Pair p1 и p2 и возвращала значение Boolean.
2) Реализовать обобщенную функцию, чтобы найти количество элементов в общем массиве, которое больше, чем определенный элемент.
   int countGreaterThan принимает на вход массив и элемент, с которым нужно сравнить все остальные элементы массива.
3) Реализовать обобщенный класс Sorter с параметром Т и подходящим ограничением, который имеет свойство list:MutableList<T> и функцию fun add(value:T).
   С каждым вызовом функции передаваемое значение должно добавляться в список и список должен оставаться в отсортированном виде.
4) Написать обобщенный стек. Минимально - реализовать функции вставки, извлечения и проверки на пустоту.

