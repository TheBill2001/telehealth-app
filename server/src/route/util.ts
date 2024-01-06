import { Request, Response } from "express";
import jwt from "./jwt.js";
import routeErrorHandler from "./error.js";

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

const checkCrendital = (req: Request, res: Response) => {
    try {
        const token = req.cookies["token"];
        if (!token || !jwt.verifyJwt(token)) {
            routeErrorHandler.unauthorized(res);
            return false;
        }

        res.status(200).json({ message: "Ok" }).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
        return false;
    }

    return true;
};

export default {
    checkCrendital,
    getDecodedToken,
};
