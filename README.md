# Oltásokat kezelő alkalmazás

## Funkcionális leírás

A koronavírus elleni védekezésben az egyik leghatékonyabb eszközünk a vakcinák beadása.
Ez egy olyan alkalmazás, mely megszervezi az oltásokat.
Az alkalmazás konzolos felülettel, és relációs adatbázissal rendelkezik (MySQL).

### Az üzleti folyamat a következő lépésekből áll:

Állampolgár beregisztrálása, ami történhet egyenként, vagy tömegesen csv fájlból az adatok ellenőrzésével.

Regisztrációkor az alábbi adatokat kell megadni:

* Teljes név
* Irányítószám
* Életkor
* E-mail cím
* TAJ száma

Adatok ellenőrzési kritériumai:

* Név nem lehet üres
* Irányító szám nem lehet üres.
* Az életkor > 10 és < 150.
* Az e-mail cím legyen formaliag helyes.
* A TAJ számot CDV szerint kell ellenőrizni

A csv állomány a formátuma a következő:

```
Név;Irányítószám;Életkor;E-mail cím;TAJ szám
John Doe;2061;60;john.doe@example.com;1234567890
Jane Doe;2091;40;jane.doe@example.com;1234567881
```

Majd a regisztrált adatokból oltási terv generálása irányítószám alapján. 
Mivel az idősebbek veszélyeztetettebbek, ezért az idősebbek kerülnek előre. 
Óránként két embert lehet beoltani. Valamint azokat kell csak behívni, 
akiknek még nem volt oltása, vagy volt oltása, azonban már eltelt 15 nap.
Munkaidő 8:00-tól 16:00-ig, félórás időközökkel.

A generált fájl formátuma:

```
Időpont;Név;Irányítószám;Életkor;E-mail cím;TAJ szám
8:00;John Doe;2061;60;john.doe@example.com;1234567890
8:30;Jack Doe;2061;40;jack.doe@example.com;1234567881
```

A következő lépésben megtörténik az oltás. Oltásnál meg kell adni a taj számot és az oltás típusát. Második oltásnál ellenőrizni kell, hogy az első oltás óta eltelt már 15 nap, illetve kiírja az eddigi oltások adatait.

Azonban az oltás meg is hiúsulhat. Pl. az állampolgár visszautasítja, olyan betegsége van, várandós, stb. Ez is rögzítésre kerül a rendszerben
a TAJ szám, és indoklás megadásával.

Az alkalmazás tud riportot is készíteni, ami kiírja, hogy irányítószámonként
hány beoltatlan, egyszer és kétszer beoltott állampolgár van.

## Felhasználói felület

Konzolos alkalmazás főmenü:

```
1. Regisztráció
2. Tömeges regisztráció
3. Generálás
4. Oltás
5. Oltás meghiúsulás
6. Riport
```

* Regisztráció: Bekéri az állampolgár adatait, és elmenti az adatbázisban
* Tömeges regisztráció: bekéri egy csv fájl elérési útvonalát, beolvassa a fájlt és az adatokat feltölti az adatbázisba.
* Generálás: bekéri az irányítószámot, majd a fájl nevét, amilyen néven el akarjuk menteni, és az adatok alapján elkészíti az oltási tervet
* Oltás: bekéri a TAJ számot. Lekérdezi az adatbázisból, hogy volt-e már oldása. Ha nem volt, akkor meg kell adni a beadott vakcina típusát. Ha már volt egy,
  akkor ki kell írni, hogy mikor és milyen vakcinával történt.
* Oltás meghiúsulás: be kell írni a TAJ számot, majd a meghiúsulás okát.
* Riport: megjeleníti a riportot.