const express = require("express");
const app = express();
const port = 3000;

// Get the client
const mysql = require("mysql2");
const cors = require("cors");
const bodyParser = require("body-parser");

app.use(cors({ origin: "*" }));
app.use(bodyParser.json());

////////////////////////////////////////////////////////////////////////////////////////////
const { MongoClient, ObjectId } = require("mongodb");
const client = new MongoClient(
  "mongodb+srv://replituser:replitpass@cluster0.yjuym.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0",
);

async function run() {
  //run mongodb connection
  try {
    await client.connect();
  } finally {
    await client.close();
  }
}

//gets movies from database with option for query parameters to filter on any column
app.get("/api/v1/movies", async (req, res) => {
  try {
    await client.connect();

    const query = {};
    for (const [key, value] of Object.entries(req.query)) {
      if (!isNaN(value)) {
        //If entry is a number, convert to float
        query[key] = parseFloat(value);
      } else {
        query[key] = value;
      }
    }

    //gets movies using query
    const movies = await client
      .db("sample_mflix")
      .collection("movies")
      .find(query)
      .limit(10)
      .toArray();

    res.status(200).json(movies); //Success message
  } catch (err) {
    //error happens
    console.error(err);
    res.status(500).json(["An error has occurred."]); //error message
  } finally {
    await client.close(); //close connection
  }
});

//return all fields for all documents in colors
app.get("/api/v1/colors", async (req, res) => {
  try {
    await client.connect();
    const colors = await client //gets all colors
      .db("cs480-project2")
      .collection("colors")
      .find({})
      .toArray();

    res.status(200).json(colors);
  } catch (err) {
    console.error(err);
    res.status(500).json(["An error has occurred."]);
  } finally {
    await client.close();
  }
});

//insert a new color and return result
app.post("/api/v1/colors", async (req, res) => {
  try {
    await client.connect();

    const newC = req.query.color;

    const result = await client
      .db("cs480-project2")
      .collection("colors")
      .insertOne({ color: newC }); //insert new color

    res.status(201).send(result);
  } catch (err) {
    console.error(err);
    res.status(500).send("Error inserting new color");
  } finally {
    await client.close();
  }
});

//gets all fields for a spcific color id
app.get("/api/v1/colors/:id", async (req, res) => {
  try {
    await client.connect();
    const { id } = req.params;
    const color = await client //gets color with id that is provided
      .db("cs480-project2")
      .collection("colors")
      .findOne({ _id: new ObjectId(id) });

    if (color) {
      //if color is find, send it else send empty array
      res.status(200).json(color);
    } else {
      res.status(404).json([]);
    }
  } catch (err) {
    console.error(err);
    res.status(500).json(["An error has occurred."]);
  } finally {
    await client.close();
  }
});

//updates a color with the id provided
app.put("/api/v1/colors/:id", async (req, res) => {
  try {
    await client.connect();

    const result = await client
      .db("cs480-project2")
      .collection("colors")
      .updateOne(
        { _id: new ObjectId(req.params.id) },
        { $set: { color: req.body.color } }, //sets color to new name
      );
    if (result.matchedCount) {
      //returns result if color is found and updated
      res.status(200).json(result);
    } else {
      //return empy array if not found
      res.status(404).json([]);
    }
  } catch (err) {
    res.status(500).json(["An error has occurred."]);
  } finally {
    await client.close();
  }
});

//deletes a color with the given id
app.delete("/api/v1/colors/:id", async (req, res) => {
  try {
    await client.connect();

    const result = await client
      .db("cs480-project2")
      .collection("colors")
      .deleteOne({ _id: new ObjectId(req.params.id) }); //delets color with given id
    if (result.deletedCount) {
      res.status(200).json(result);
    } else {
      res.status(404).json([]);
    }
  } catch (err) {
    res.status(500).json(["An error has occurred."]);
  } finally {
    await client.close();
  }
});

run().catch(console.dir);

///////////////////////////////////////////////////////////////////////////////////////////

// Create the connection to database
const connection = mysql.createConnection({
  //connects to mysql workbench with select privileges
  host: "34.132.211.92",
  user: "replit",
  password: "replitpass!",
});

//gets all actors from the database
app.get("/api/v1/actors", (req, res) => {
  connection.query(
    "SELECT * FROM sakila.actor;",
    function (err, results, fields) {
      if (err) {
        res.status(500).json(["An error has occurred."]);
      } else {
        res.json(results);
      }
    },
  );
});

//gets all movies from the database with optional title query
app.get("/api/v1/films", (req, res) => {
  let sql = "SELECT * FROM sakila.film WHERE 1=1"; //default query
  const params = []; //incase new parameters are added

  if (req.query.query) {
    //if query is not empty
    sql += " AND LOWER(title) LIKE ?"; //add query to sql
    params.push(`%${req.query.query.toLowerCase()}%`); //add query to params
  }

  connection.execute(sql, params, function (err, results, fields) {
    if (err) {
      res.status(500).json(["An error has occurred."]);
    } else {
      res.json(results);
    }
  });
});

//gets all customers from the database
app.get("/api/v1/customers", (req, res) => {
  connection.query(
    "SELECT * FROM sakila.customer;",
    function (err, results, fields) {
      if (err) {
        res.json(["An error has occurred."]);
      } else {
        res.json(results);
      }
    },
  );
});

//gets all stores from the database
app.get("/api/v1/stores", (req, res) => {
  connection.query(
    "SELECT * FROM sakila.store;",
    function (err, results, fields) {
      if (err) {
        res.json(["An error has occurred."]);
      } else {
        res.json(results);
      }
    },
  );
});

//gets actor with a specific id
app.get("/api/v1/actors/:id", (req, res) => {
  connection.execute(
    "SELECT * FROM sakila.actor WHERE actor_id = ?",
    [req.params.id],
    function (err, results, fields) {
      if (err) {
        res.json(["An error has occurred."]);
      } else if (results.length === 0) {
        res.json([]); //No actor was found so return empty array
      } else {
        res.json(results);
      }
    },
  );
});

//gets film with a specific id
app.get("/api/v1/films/:id", (req, res) => {
  connection.execute(
    "SELECT * FROM sakila.film WHERE film_id = ?",
    [req.params.id],
    function (err, results, fields) {
      if (err) {
        res.json(["An error has occurred."]);
      } else if (results.length === 0) {
        res.json([]); //No film was found so return empty array
      } else {
        res.json(results);
      }
    },
  );
});

//gets store with a specific id
app.get("/api/v1/stores/:id", (req, res) => {
  connection.execute(
    "SELECT * FROM sakila.store WHERE store_id = ?",
    [req.params.id],
    function (err, results, fields) {
      if (err) {
        res.json(["An error has occurred."]);
      } else if (results.length === 0) {
        res.json([]); //No store was found
      } else {
        res.json(results);
      }
    },
  );
});

//gets customer with a specific id
app.get("/api/v1/customers/:id", (req, res) => {
  connection.execute(
    "SELECT * FROM sakila.customer WHERE customer_id = ?",
    [req.params.id],
    function (err, results, fields) {
      if (err) {
        res.json(["An error has occurred."]);
      } else if (results.length === 0) {
        res.json([]); //No customer was found
      } else {
        res.json(results);
      }
    },
  );
});

//gets all films for a specific actor id
app.get("/api/v1/actors/:id/films", (req, res) => {
  const sql = `
    SELECT f.*
    FROM sakila.film f
    JOIN sakila.film_actor fa ON f.film_id = fa.film_id
    WHERE fa.actor_id = ?`; //sql to get all films for an actor

  connection.execute(sql, [req.params.id], (err, results) => {
    if (err) {
      res.json(["An error has occurred."]);
    } else {
      res.json(results);
    }
  });
});

//gets all actors for a specific film id
app.get("/api/v1/films/:id/actors", (req, res) => {
  const sql = `
    SELECT a.*
    FROM sakila.actor a
    JOIN sakila.film_actor fa ON a.actor_id = fa.actor_id
    WHERE fa.film_id = ?`; //sql to get all actors for a film

  connection.execute(sql, [req.params.id], (err, results) => {
    if (err) {
      res.json(["An error has occurred."]);
    } else {
      res.json(results);
    }
  });
});

//gets row from view film_list based on FID
app.get("/api/v1/films/:id/detail", (req, res) => {
  const sql = `
    SELECT *
    FROM sakila.film_list
    WHERE FID = ?`; //sql to get film_list based on FID

  connection.execute(sql, [req.params.id], (err, results) => {
    if (err) {
      res.json(["An error has occurred."]);
    } else if (results.length === 0) {
      res.json([]); //No film details were found
    } else {
      res.json(results);
    }
  });
});

//gets row from view customer_list based on ID
app.get("/api/v1/customers/:id/detail", (req, res) => {
  const sql = `
    SELECT *
    FROM sakila.customer_list
    WHERE ID = ?`; //sql to get customer_list based on ID

  connection.execute(sql, [req.params.id], (err, results) => {
    if (err) {
      res.json(["An error has occurred."]);
    } else if (results.length === 0) {
      res.json([]); //No customer details were found
    } else {
      res.json(results);
    }
  });
});

//gets row from view actor_info based on actor_id
app.get("/api/v1/actors/:id/detail", (req, res) => {
  const sql = `
    SELECT *
    FROM sakila.actor_info
    WHERE actor_id = ?`; //sql to get actor_info based on actor_id

  connection.execute(sql, [req.params.id], (err, results) => {
    if (err) {
      res.json(["An error has occurred."]);
    } else if (results.length === 0) {
      res.json([]); //No actor info were found
    } else {
      res.json(results);
    }
  });
});

//return the inventory ids for a specific film id and store id
app.get("/api/v1/inventory-in-stock/:film_id/:store_id", (req, res) => {
  const sql = "CALL sakila.film_in_stock(?, ?, @count)";

  connection.execute(
    sql,
    [req.params.film_id, req.params.store_id],
    (err, results) => {
      if (err) {
        res.json(["An error has occurred."]);
      } else {
        res.json(results[0]); //Only outputs the inventory ids
      }
    },
  );
});

app.listen(port, "0.0.0.0", () => {
  console.log("Server running on port 3000");
});

// app.get("/color", (req, res) => {
//   connection.execute(
//     "SELECT * FROM data.color;",
//     function (err, results, fields) {
//       res.json(results);
//     },
//   );
// });

// app.post("/color", (req, res) => {
//   console.log("in POST route");
//   connection.execute(
//     "INSERT INTO data.color (name) VALUES (?)",
//     [req.body.name],
//     // "INSERT INTO data.color (name) VALUES ('blue')",
//     function (err, results, fields) {
//       console.log(err);
//       res.json(results);
//     },
//   );
// });

// app.get("/chi_emp", (req, res) => {
//   let endpoint = "https://data.cityofchicago.org/resource/xzkq-xp2w.json";
//   fetch(endpoint)
//     .then((response) => response.json())
//     .then((data) => {
//       res.send(JSON.stringify(data));
//     });
// });
