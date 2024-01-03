import "dotenv/config";
import bodyParser from "body-parser";
import express from "express";

import * as routers from "./route/route.js";

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.json());

app.get("/", (req, res) => {
    res.send("Hello World!");
});

app.use("/api", routers.authRouter);

app.listen(PORT, () => {
    return console.log(`Express is listening at http://localhost:${PORT}`);
}).on("error", function (err) {
    console.error(err);
});
