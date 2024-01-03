import "dotenv/config";
import common from "../common.js";
import mongoose from "mongoose";

function checkIfNullAndExit(env: string | null | undefined, message: string) {
    if (!env) {
        common.throwAndExit(message);
    }
    return env;
}

const DB_URL = new URL(
    checkIfNullAndExit(
        process.env.DB_URL,
        "Environement variable DB_URL is not set, exiting...",
    ),
);

const DB_USERNAME = checkIfNullAndExit(
    process.env.DB_USERNAME,
    "Environement variable DB_USERNAME is not set, exiting...",
);

const DB_PASSWORD = checkIfNullAndExit(
    process.env.DB_PASSWORD,
    "Environement variable DB_PASSWORD is not set, exiting...",
);

DB_URL.username = DB_USERNAME;
DB_URL.password = DB_PASSWORD;

mongoose.connection.on(
    "error",
    console.error.bind(console, "MongoDB connection error:"),
);

await mongoose.connect(DB_URL.toString());

export default mongoose;
