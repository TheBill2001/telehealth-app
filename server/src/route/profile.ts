import Router from "express";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import User from "../db/user.js";

const router = Router();

/**
 * Get user profile base on token
 */
router.get("/profile", async (req, res) => {
    res.setHeader("Connection", "close");

    const user = routeUtil.getDecodedToken(req);
    if (!user) {
        return routeErrorHandler.unauthorized(res);
    }

    try {
        const doc = await User.findById(user._id);

        if (!doc) return routeErrorHandler.missing(res, "User is not exist.");

        if (!doc.userInfo)
            return routeErrorHandler.missing(res, "User's profile missing.");

        const userInfo = doc.toObject().userInfo;
        delete userInfo["_id"];
        userInfo["id"] = doc._id;

        return res.json(userInfo).end();
    } catch (error) {
        console.error(error);
        return routeErrorHandler.internalError(res);
    }
});

export default router;
