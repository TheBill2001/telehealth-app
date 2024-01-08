import "dotenv/config";
import bodyParser from "body-parser";
import cookieParser from "cookie-parser";
import express from "express";

import * as routers from "./route/route.js";

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.json());
app.use(cookieParser());

app.get("/", (req, res) => {
    res.send("Hello World!");
});

app.use("/api/auth", routers.authRouter);
app.use("/api/profile", routers.profileRouter);
app.use("/api/symptom", routers.symptomRouter);
app.use("/api/covidTest", routers.convidTestRouter);
app.use("/api/vaccine", routers.vaccineRouter);

app.listen(PORT, () => {
    return console.log(`Express is listening at http://localhost:${PORT}`);
}).on("error", function (err) {
    console.error(err);
});
