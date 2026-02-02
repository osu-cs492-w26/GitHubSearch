package edu.oregonstate.cs492.githubsearch.data

/*
    {
        "name": "Hobbes",
        "type": "tiger",
        "age": 8,
        "favoriteFood": "tuna"
        "bestFriend": {
            "name": "Calvin",
            "age": 5,
            "alterEgos": ["Spaceman Spiff", "Stupendous Man"]
        }
    }
 */
data class Cat (
    val name: String,
    val type: String,
    val age: Int,
    val favoriteFood: String,
    val bestFriend: Friend
)

data class Friend (
    val name: String,
    val age: Int,
    val alterEgos: List<String>
)