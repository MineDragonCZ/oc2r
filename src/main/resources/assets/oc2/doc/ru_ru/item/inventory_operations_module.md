# Модуль управления инвентарем
![Что твое, то мое](item:oc2:inventory_operations_module)

Модуль управления инвентарем позволяет [роботам](robot.md) брать и выкидывать предметы из своего инвентаря. Это касается как предметов, так и блоков.

## API
Название устройства: `inventory_operations`

Это устройство с высокоуровневым API. Оно может управляться через Lua в стандартном дистрибутиве Linux. Например:  
`local d = require("devices")`  
`local m = d:find("inventory_operations")`  
`m:drop(1, "front")`

### Стороны
Стороны указываются относительно лицевой стороны робота. Допускаются значения: `front`, `up` и `down`.

### Методы
`move(fromSlot:number,intoSlot:number,count:number)` пробует переместить определенное количество предметов из одного слота робота в другой.
- `fromSlot` - номер слота, откуда берутся предметы.
- `intoSlot` - номер слота, куда ложатся предметы.
- `count` - количество предметов для перемещения.

`drop(count:number[,side]):number` пробует выкинуть определенное количество предметов из текущего слота в указанном направлении. Предметы выбросятся либо в хранилище (если в указанном направлении есть, например, сундук), либо в игровой мир.
- `count` - количество предметов для выкидывания.
- `side` - сторона, куда будут выкинуты предметы. Опциональное значение, по умолчанию равно `front`. Смотрите секцию "Стороны".
- Возвращает количество выкинутых предметов.

`dropInto(intoSlot:number,count:number[,side]):number` пробует выкинуть предметы с текущего слота в указанный слот хранилища на указанной стороне. Выкидывает предметы только если есть хранилище.
- `intoSlot` - номер слота, куда будут положены предметы.
- `count` - количество предметов для выкидывания.
- `side` - сторона, куда будут выкинуты предметы. Опциональное значение, по умолчанию равно `front`. Смотрите секцию "Стороны".
- Возвращает количество выкинутых предметов.

`take(count:number[,side]):number`  пробует взять определенное количество предметов в текущий слот из указанного направления. Предметы возьмутся либо из хранилища (если в указанном направлении есть, например, сундук), либо из игрового мира.
- `count` - количество предметов для подбирания.
- `side` - сторона, откуда будут взяты предметы. Опциональное значение, по умолчанию равно `front`. Смотрите секцию "Стороны".
- Возвращает количество подобранных предметов.

`takeFrom(fromSlot:number,count:number[,side]):number` пробует взять предметы в текущий слот из указанного слота хранилища на указанной стороне. Берет предметы только если есть хранилище.
- `intoSlot` - номер слота, откуда будут взяты предметы.
- `count` - количество предметов для подбирания.
- `side` - сторона, откуда будут взяты предметы. Опциональное значение, по умолчанию равно `front`. Смотрите секцию "Стороны".
- Возвращает количество подобранных предметов.