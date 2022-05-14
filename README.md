# 22A15

## How to interact with the database?
- Instantiate `XyzRepository` (replace Xyz with the Entity name)
- Call one of the public methods 

## How to insert a new accelerometer datapoint inside database?
```
    AccelerometerRepository accelerometerRepository = new AccelerometerRepository(getApplication());
    accelerometerRepository.insert(new Accelerometer(timestamp, x, y, z));
```

## How to export database inside android studio?
- Go to View -> Tool Windows -> Device File Explorer
- Then go to data -> data -> de.rub.selab22a15 -> databases
- Select tracking_database, tracking_database-shm & tracking_database-wal
- Right click & click save to
- save to a place e.g. your Desktop

## How to open database outside android?
- Download sqlitebrowser from (https://sqlitebrowser.org/)[https://sqlitebrowser.org/]
- Open sqlitebrowser
- Drag & Drop tracking_database to sqlitebrowser