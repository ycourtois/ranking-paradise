# Ranking Paradise

## Launch tests

- Unit Tests
```
mvnw test
```

- Integration Tests
```
mvnw verify
```

## Start API

To target a DynamoDB table on AWS Service :
```
bash build_and_run.sh
```

To target a DynamoDB table on localstack :
```
bash build_and_run.sh --localstack
```

Application will listen on http port 8080.

Management port is set to 8081.

You can check if database is reachable by performing the following request : \
http://localhost:8081/healthcheck

## API endpoints
- ### Get player information

GET /players/{nickname}

**Parameters**

|          Name | Required |  Type   | Description              |
| -------------:|:--------:|:-------:| -------------------------|
|     `nickname` | required | string  | Player nickname         |

**Request**

GET /players/John

**Response**

Status: 200

```
{
    "nickname": "John",
    "tournament_id": 1,
    "score": 100,
    "ranking": 4
}
```

- ### Add player to tournament

POST /players

**Parameters**

|          Name | Required |  Type   | Description                        |
| -------------:|:--------:|:-------:| -----------------------------------|
|     `nickname` | required | string  | Player nickname           |
|     `score`    | optional | number  | Player score (Default 0)          |
|     `tournament_id` | optional | number  | Tournament id  (Default 1)        |


**Request**

```
{
    "nickname": "Max",
    "score": 0,
    "tournament_id": 6
}
```

**Response**

Status: 201


- ### Update player

PUT /players/{nickname}

**Parameters**

|          Name | Required |  Type   | Description                        |
| -------------:|:--------:|:-------:| -----------------------------------|
|     `nickname` | required | string  | Player nickname          |
|     `score`    | required | number  | New player score          |


**Request**

PUT /players/John

```
{
    "score": 110
}
```

**Response**

Status: 200

```
{
    "nickname": "John",
    "tournament_id": 1,
    "score": 110,
    "ranking": 4
}
```

- ### List players by tournament

GET /tournament/{id}/players

**Parameters**

|          Name | Required |  Type   | Description                        |
| -------------:|:--------:|:-------:| -----------------------------------|
|     `id` | required | number  | Tournament id          |

**Request**

GET /tournament/1/players

**Response**

Status: 200

```
[
    {
        "nickname": "Nick",
        "tournamentId": 1,
        "score": 400,
        "ranking": 1
    },
    {
        "nickname": "Bruce",
        "tournamentId": 1,
        "score": 350,
        "ranking": 2
    },
    {
        "nickname": "Mac",
        "tournamentId": 1,
        "score": 300,
        "ranking": 3
    },
    {
        "nickname": "John",
        "tournamentId": 1,
        "score": 100,
        "ranking": 4
    },
    {
        "nickname": "Mike",
        "tournamentId": 1,
        "score": 50,
        "ranking": 5
    }
]
```

- ### End tournament and delete involved players

DELETE /tournament/{id}

**Parameters**

|          Name | Required |  Type   | Description                        |
| -------------:|:--------:|:-------:| -----------------------------------|
|     `id` | required | number  | Tournament id          |

**Request**

DELETE /tournament/1

**Response**

Status: 200

## TODO

Add more tests.

## Issues

Maven assembly plugin is the recommended way to build a self-contained jar.
(https://kotlinlang.org/docs/maven.html#self-contained-jar-file)
However, it is not able to merge all SPI implementations.

At this moment, we need to use maven shade plugin which offer some transformation service 
to merge the involved SPI implementations.
