# 76DPSCalculator (Backend)

## Overview
76DPSCalculator is a prototype damage per second calculator for the game Fallout 76.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation & Setup](#installation--setup)
3. [Usage](#usage)
4. [Additional Resources](#additional-resources)
5. [Calculation Formulas](#calculation-formulas)

## Prerequisites

- [Gradle](https://gradle.org/install/)<br>
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)<br>
## Installation & Setup

### 1. Clone the Repository
```
git clone https://github.com/Teekiz/76DPSCalculator.git
```
### 2. Configure the Application
If you wish to change the `docker-compose.yml` ensure in `src\main\resources\application-docker.properties` that the credentials and address match.
```
spring.data.mongodb.uri=mongodb://root:example@mongo:27017/76dpscalculatordb?authSource=admin
```
For example, if changing the username or password: <br>
`docker-compose.yml`:
```
MONGO_INITDB_ROOT_USERNAME: admin
MONGO_INITDB_ROOT_PASSWORD: example_pa55w0rd123
```
`application-docker.properties`:
```
spring.data.mongodb.uri=mongodb://admin:example_pa55w0rd123@mongo:27017/76dpscalculatordb?authSource=admin
```
### 3. Build the Application
Run the following command to build the JAR file:
```
./gradlew build
```
### 4. Running the Application
```
docker-compose up -d --build
```
This will:
- Build and start the Spring Boot application (`spring-boot-app`)
- Start a Redis container (`redis`)
- Load the files locally or from MongoDB.
### 5. Verify the Setup

To check if Redis is running:

```sh
docker logs redis
```

To check if the application is running:

```sh
docker logs spring-boot-app
```

### 6. Access the Application

The application should be accessible at:

```
http://localhost:8080
```
For more information see [usage](#usage).

## Usage
### Example: Changing Weapon and Perk
Below are some example `curl` requests to interact with the API.
```
//Creating a new loadout with ID of 1
curl -X GET "http://localhost:8080/api/loadouts/getLoadout?loadoutID=1"

//Setting loadout 1's weapon to 10MM Pistol
curl -X POST "http://localhost:8080/api/loadouts/setWeapon?loadoutID=1&weaponID=WEAPONS2"

//Adding Perk "Gunslinger"
curl -X POST "http://localhost:8080/api/loadouts/addPerk?loadoutID=1&perkID=PERKS1"

//Changing Gunslingers Rank to 2
curl -X POST "http://localhost:8080/api/loadouts/changePerkRank?loadoutID=1&perkID=PERKS1&perkRank=2"

//Getting the DPS of loadout 1
curl -X GET "http://localhost:8080/api/services/getLoadoutDPS?loadoutID=1"
```

### Example: Creating A Second Loadout With Mutations and Consumables
```
//Creating a new loadout with ID of 2
curl -X GET "http://localhost:8080/api/loadouts/getLoadout?loadoutID=2"

//Setting loadout 2's weapon to Assaultron Blade
curl -X POST "http://localhost:8080/api/loadouts/setWeapon?loadoutID=2&weaponID=WEAPONS1"

//Setting loadout 2's player to strength level 5
curl -X POST "http://localhost:8080/api/loadouts/changeSpecial?loadoutID=2&special=STRENGTH&value=5"

//Adding adrenal reaction
curl -X POST "http://localhost:8080/api/loadouts/addMutation?loadoutID=2&mutationID=MUTATIONS1"

//Adding Fury
curl -X POST "http://localhost:8080/api/loadouts/addConsumable?loadoutID=1&consumableID=CONSUMABLES3"

//Getting the DPS for both loadouts
curl -X GET "http://localhost:8080/api/services/getAllLoadoutsDPS"
```
## Additional Resources
- <b>Full API List</b>: See [API List](github/api_list.txt) <br>
- <b>Web-based GUI</b>: Available [here](https://github.com/Teekiz/76DPSCalculatorUI).
## Calculation Formulas
Ranged Damage Per Second ([usage](https://github.com/Teekiz/76DPSCalculator/blob/master/src/main/java/Tekiz/_DPSCalculator/_DPSCalculator/services/calculation/DamagePerSecond/ReloadDamageCalculator.java)):
```
shotsPerSecond = fireRate / 10
timeToEmptyMagazine = magazineSize / shotsPerSecond
cycleTime = timetoEmptyMagazine + reloadTime
damagePerCycle = damagePerShot * magazineSize
damagePerSecond = (damagePerCycle + dotDamageDuringReload) / totalCycleTime
```
