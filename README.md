Постановка задачи

В среде IntelliJ IDEA Community Edition надо создать проект HW3, использующий Java 20, zip-архив которого надо загрузить в данное задание. 
Проект должен содержать статическую функцию класса Main, которая читает текстовый файл input.csv, содержащий список предпочтений голосующих,  находящийся в папке проекта, создает разработанную студентом структуру классов и при помощи ее по списку предпочтений вариантов голосования составляет общественный порядок предпочтений вариантов голосования в соответствии с описанной в прилагающемся к заданию файле процедурой подсчета результатов голосования по методу Борда (Программа должна расположить варианты голосования по "занятым ими местам").
Описание входящих данных

Входящие данные находятся в файле input.csv, который будет находиться в папке проекта. Файл содержит строки. Каждая строка соответствует одному голосующему. В каждой строке файла через запятую перечисляются номера вариантов голосования в порядке уменьшения предпочтений данного голосующего (слева - направо). В одной строке могут быть перечислены не все участвующие в голосовании варианты, но хотя бы одно число обязательно есть.

Пример файла приложен к данному заданию.

Описание результата работы программы

Результатом работы программы должен быть файл result.txt (в папке проекта), в котором должна быть только одна строка. В строке должен содержаться построенный общественный порядок предпочтений вариантов голосования, который представляет собой последовательность номеров голосования, распределенных по занятым ими местам. В строке через запятую перечисляются номера вариантов голосования слева-направо по убыванию занимаемого места. Если определенное место заняли сразу несколько вариантов голосования, то эти варианты помещаются в строку в квадратных скобках (в порядке - по возрастанию номеров вариантов), при этом между собой они разделяются запятыми. Пример возможного общественного порядка предпочтений вариантов голосования:
4,9,5,2,[10,12],13,3,7,[1,8,9],11,14
