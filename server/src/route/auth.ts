import Router from "express";
import jwt from "./jwt.js";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import User from "../db/user.js";

const router = Router();

/**
 * Authentication accepts username and password
 *
 * Request Format - JSON : {username: <login>, password: <password>}
 */
router.post("/auth/login", async (req, res) => {
    res.setHeader("Connection", "close");
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
router.post("/auth/register", async (req, res) => {
    res.setHeader("Connection", "close");
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
router.get("/auth/ping", async (req, res) => {
    res.setHeader("Connection", "close");
    routeUtil.checkCrendital(req, res);
});

export default router;
