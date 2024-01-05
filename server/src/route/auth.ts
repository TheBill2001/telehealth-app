import Router from "express";
import jwt from "./jwt.js";
import routeErrorHandler from "./error.js";
import User from "../db/user.js";

const router = Router();

router.use((req, res, next) => {
    res.setHeader("Connection", "close");

    if (req.path === "/auth/ping" && req.method === "POST") {
        next();
    }

    if (routeErrorHandler.unsupportedMediaType(req, res, "application/json")) {
        next();
    }
});

/**
 * Authentication accepts username and password
 *
 * Request Format - JSON : {username: <login>, password: <password>}
 */
router.post("/auth/login", async (req, res) => {
    const { username, password } = req.body;

    try {
        const user = await User.findOne({
            username: username,
            password: password,
        });

        if (!user) {
            return routeErrorHandler.unauthorized(res);
        }

        const token = jwt.signJwt(user.toObject());
        res.cookie("token", token, {
            maxAge: jwt.TOKEN_EXPIRE,
            httpOnly: true,
        }).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

/**
 * Request format - JSON:
 * {
 *     username: <username>
 *     password: <password>
 *     userInfo: {
 *         phone: <phone>
 *         email: <email>
 *         name: <name>
 *         dateOfBirth: <YYYY-mm-dd>
 *         citizenID: <id>
 *     }
 * }
 *
 * Response 409 : Username existed
 */
router.post("/auth/register", async (req, res) => {
    const { username, password, userInfo } = req.body;
    // const { phone, email, name, dateOfBirth, citizenID } = userInfo;

    try {
        if (await User.exists({ username: username })) {
            return routeErrorHandler.conflict(res, "Username existed.");
        }

        const newUser = await User.create({
            username: username,
            password: password,
            userInfo: userInfo,
        });
        const token = jwt.signJwt(newUser.toObject());
        res.cookie("token", token, {
            maxAge: jwt.TOKEN_EXPIRE,
            httpOnly: true,
        }).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

/**
 * Check if token is valid
 *
 * 200 - Valid
 * 401 - Not valid
 */
router.post("/auth/ping", async (req, res) => {
    try {
        const token = req.cookies["token"];
        if (!token || !jwt.verifyJwt(token)) {
            return routeErrorHandler.unauthorized(res);
        }

        res.status(200).json({ message: "Ok" }).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

export default router;
