import Router from "express";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import Symptoms from "../db/symptoms.js";

const router = Router();

// Middleware
router.use((req, res, next) => {
    res.setHeader("Connection", "close");

    next();
});

/**
 * Get user's symptoms using token.
 * Return:
 * {
 *    user: <userId>
 *    symptoms: ISymptom[]
 * }
 */
router.get("/", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const fromQuery = new Date(req.query.from?.toString() || 0);
    const toQuery = new Date(req.query.to?.toString() || Date.now());
    const descQuery = req.query.desc === "true" ? "desc" : "asc";

    try {
        const symptoms = await Symptoms.find({
            userId,
            createdAt: {
                $gte: fromQuery,
                $lte: toQuery,
            },
        }).sort(descQuery);

        const data = symptoms ? symptoms.map((item) => item.toObject()) : [];
        data.forEach((item) => {
            delete item.userId;
        });

        return res
            .json({
                userId: userId,
                symptoms: data,
            })
            .end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

/**
 * {
 *     description: string,
 *     severity: 1..10,
 *     note?: string
 * }
 */
router.post("/", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    if (!routeErrorHandler.unsupportedMediaType(req, res, "application/json"))
        return;

    const { description, severity, note } = req.body;

    try {
        const symptoms = await Symptoms.create({
            userId,
            description,
            severity,
            note,
        });
        return res.json(symptoms.toObject()).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.get("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const symptoms = await Symptoms.findById(req.params.entryId);
        return symptoms
            ? res.json(symptoms.toObject()).end()
            : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.put("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    if (!routeErrorHandler.unsupportedMediaType(req, res, "application/json"))
        return;

    const { description, severity, note } = req.body;

    try {
        const symptoms = await Symptoms.findByIdAndUpdate(req.params.entryId, {
            description,
            severity,
            note,
        });
        return symptoms
            ? res.json(symptoms.toObject()).end()
            : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.delete("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const symptoms = await Symptoms.findByIdAndDelete(req.params.entryId);
        return symptoms ? res.end() : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

export default router;
