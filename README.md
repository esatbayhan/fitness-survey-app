# 22A15

## How to interact with the database?
- Instantiate `XyzRepository` (replace Xyz with the Entity name)
- Call one of the public methods 

## How to insert a new accelerometer datapoint inside database?
```
    AccelerometerRepository accelerometerRepository = new AccelerometerRepository(getApplication());
    accelerometerRepository.insert(new Accelerometer(timestamp, x, y, z));
```

## Firebase Security rules:
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{uid}/{db_file} {
      allow create: if isSignedIn() && isOwner(uid);
    }
  
    function isSignedIn() {
    	return request.auth != null;
    }
    
    function isOwner(uid) {
    	return request.auth.uid == uid;
    }
  }
}
```

## Database Datasheet
### Rumination
rumination:
- I was stuck on negative thoughts and could not disengage from them
- I am thinking about my feelings
- I am thinking about my problems