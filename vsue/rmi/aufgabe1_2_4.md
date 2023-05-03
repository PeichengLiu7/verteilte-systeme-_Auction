## Wie viele Bytes umfasst eine "leere" VSTestMessage?

121 Bytes

## Mit welchem Byte-Wert signalisiert der ObjectOutputStram, dass ein Attribut null ist?

0x70

## Welchen Einfluss hat der Wert von integer auf die Größe der serialisierten Daten?

Scheint keinen Einfluss zu haben

## Welchen Einfluss hat ein einzelner Buchstabe in string auf die Größe der serialisierten Daten?

Vergrößerung um 3 Byte

## Welchen Einfluss hat die Array-Größe von objects auf die Größe der serialisierten Daten?

Mit null: 121 Bytes \
Mit length == 1: 226 Bytes (+105) \
Mit length == 2: 241 Bytes (+15) \
Mit length == 3: 256 Bytes (+15) \
\
Folgerung: Jedes Objekt (hier: Beispielobjekt `new VSAuction("Test", 0)`) erhöht um 15 Byte
