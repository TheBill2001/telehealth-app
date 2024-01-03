import "dotenv/config";
import Router from "express";
import Jwt from "jsonwebtoken";
import routeErrorHandler from "./error.js";
import User from "../db/user.js";

const router = Router();
const JWT_SECRET = process.env.JWT_SECRET || "123456";
const TOKEN_EXPIRE = 86400000;

function signJwt(payload: string | object | Buffer) {
    return Jwt.sign(payload, JWT_SECRET, {
        expiresIn: TOKEN_EXPIRE,
    });
}

router.use((req, res, next) => {
    res.setHeader("Connection", "close");

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

        const token = signJwt(user.toObject());
        res.cookie("token", token, { maxAge: TOKEN_EXPIRE, httpOnly: true });
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
        const token = signJwt(newUser.toObject());
        res.cookie("token", token, { maxAge: TOKEN_EXPIRE, httpOnly: true });
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

export default router;
