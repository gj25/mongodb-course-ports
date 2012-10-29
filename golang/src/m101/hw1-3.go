package main

import (
	"fmt"
	"strconv"
	"code.google.com/p/goweb/goweb"
	"github.com/garyburd/go-mongo/mongo"
)

func getHw1(number int) string {
	conn, err := mongo.Dial("localhost")
	if err != nil {
		fmt.Println("Error trying to connect to database")
	}

	defer conn.Close()

	db := mongo.Database{conn, "m101", mongo.DefaultLastErrorCmd}

	funnynumbers := db.C("funnynumbers")

	cursor, err := funnynumbers.Find(nil).Limit(1).Skip(number).Sort(mongo.D{{"value", 1}}).Cursor()
	if err != nil {
		fmt.Println("Error trying to read collection")
	}

	defer cursor.Close()

	var value string
	if cursor.HasNext() {
		var m map[string]interface{}

		cursor.Next(&m)

		value = fmt.Sprint(m["value"])
	}

	return value
}

func main() {
	goweb.MapFunc("/hw1/{number}",
		func(c *goweb.Context) {
			number, err := strconv.ParseInt(c.PathParams["number"], 10, 32)
			if err != nil {
				fmt.Fprintln(c.ResponseWriter, "Expecting valid number")
			}
			fmt.Fprintln(c.ResponseWriter, getHw1(int(number)))
		})

	goweb.ListenAndServe("127.0.0.1:8080")
}
