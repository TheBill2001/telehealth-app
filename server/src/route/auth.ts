import Router from "express";
import jwt from "./jwt.js";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import User from "../db/user.js";

const router = Router();

/**
 * Middleware for this `/api/auth` router
 */
router.use((req, res, next) => {
    res.setHeader("Connection", "close");

    // `/api/auth/ping doesn's need JSON body`
    if (req.path === "/ping") next();

    // Verify request has the right body
    if (routeErrorHandler.unsupportedMediaType(req, res, "application/json"))
        next();
});

/**
 * Authentication accepts username and password
 *
 * Request Format - JSON : {username: <login>, password: <password>}
 */
router.post("/login", async (req, res) => {
    if (!routeErrorHandler.unsupportedMediaType(req, res, "application/json"))
        return;

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
 * Response 422 : Incorrect data
 */
router.post("/register", async (req, res) => {
    if (!routeErrorHandler.unsupportedMediaType(req, res, "application/json"))
        return;

    const { username, password, userInfo } = req.body;
    let newUser;

    try {
        if (await User.exists({ username: username })) {
            return routeErrorHandler.conflict(res, "Username existed.");
        }

        newUser = await User.create({
            username: username,
            password: password,
            userInfo: userInfo,
        });
    } catch (error) {
        routeErrorHandler.unprocessableEntity(res, error.message);
        return;
    }

    try {
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
router.get("/ping", async (req, res) => {
    await routeUtil.checkUserIdFromToken(req, res);
});

export default router;
