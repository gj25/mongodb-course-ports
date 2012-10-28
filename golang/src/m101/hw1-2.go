package main

import (
    "github.com/garyburd/go-mongo/mongo"
    "fmt"
    "os"
)

func main() {
    conn,err := mongo.Dial("localhost")
    if err != nil {
        fmt.Println("Error trying to connect to database")
        os.Exit(1)
    }

    defer conn.Close()

    db := mongo.Database{conn, "m101", mongo.DefaultLastErrorCmd}

    funnynumbers := db.C("funnynumbers")

    cursor,err := funnynumbers.Find(nil).Cursor()
    if err != nil {
        fmt.Println("Error trying to read collection")
        os.Exit(1)
    }

    defer cursor.Close()

    var magic int
    for cursor.HasNext() {
        var m map[string]interface{}

        cursor.Next(&m)

        value := int(m["value"].(float64))

        if value % 3 == 0 {
            magic += int(m["value"].(float64))
        }

    }

    fmt.Println("The answer to Homework One, Problem 2 is", magic)
}
