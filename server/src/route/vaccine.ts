import Router from "express";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import {
    VaccineHistory,
    VaccineRegistraction,
    VaccineRegistrationStatus,
} from "../db/vaccine.js";

const router = Router();

router.use((req, res, next) => {
    res.setHeader("Connection", "close");

    next();
});

router.get("/history", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const fromQuery = new Date(req.query.from?.toString() || 0);
    const toQuery = new Date(req.query.to?.toString() || Date.now());
    const descQuery = req.query.desc === "true" ? "desc" : "asc";

    try {
        const history = await VaccineHistory.find({
            userId,
            date: {
                $gte: fromQuery,
                $lte: toQuery,
            },
        }).sort({ date: descQuery });

        return res.json(history.map((item) => item.toObject())).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.get("/registration", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const fromQuery = new Date(req.query.from?.toString() || 0);
    const toQuery = new Date(req.query.to?.toString() || Date.now());
    const descQuery = req.query.desc === "true" ? "desc" : "asc";

    try {
        const registration = await VaccineRegistraction.find({
            userId,
            date: {
                $gte: fromQuery,
                $lte: toQuery,
            },
        }).sort({ date: descQuery });

        return res.json(registration.map((item) => item.toObject())).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.post("/registration", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    if (!routeErrorHandler.unsupportedMediaType(req, res, "application/json"))
        return;

    const { name, type } = req.body;

    try {
        const registration = await VaccineRegistraction.create({
            userId,
            name,
            type,
        });

        return res.json(registration.toObject()).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.get("/registration/:id", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const registration = await VaccineRegistraction.findOne({
            _id: req.params.id,
            userId,
        });

        return registration
            ? res.json(registration.toObject()).end()
            : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.put("/registration/:id/cancel", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const registration = await VaccineRegistraction.findByIdAndUpdate(
            {
                userId,
                _id: req.params.id,
            },
            {
                status: VaccineRegistrationStatus.Canceled,
            },
        );

        return registration
            ? res.json(registration.toObject()).end()
            : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

export default router;
