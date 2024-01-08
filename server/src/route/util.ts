import { Request, Response } from "express";
import jwt from "./jwt.js";
import routeErrorHandler from "./error.js";
import User from "../db/user.js";

const getDecodedToken = (req) => {
    let decodedToken = null;
    try {
        const token = req.cookies["token"];
        if (!token) {
            return null;
        }

        decodedToken = jwt.verifyJwt(token);
        if (!decodedToken) {
            return null;
        }
    } catch (error) {
        console.error(error);
        return null;
    }

    return decodedToken;
};

const checkUserIdFromToken = async (
    req: Request,
    res: Response,
): Promise<string> => {
    const userToken = getDecodedToken(req);
    if (!userToken) {
        routeErrorHandler.unauthorized(res);
        return null;
    }

    try {
        const user = await User.findById(userToken._id);
        if (!user) {
            routeErrorHandler.unauthorized(res);
            return null;
        }
        return user._id.toString();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }

    return null;
};

export default {
    getDecodedToken,
    checkUserIdFromToken,
};
