import Router from "express";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import Symptoms, { ISymptom } from "../db/symptoms.js";

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
    const descQuery = req.query.desc === "true" ? true : false;

    try {
        const symptoms = await Symptoms.findOne({ user: userId });

        const userSymptoms: ISymptom[] =
            symptoms?.symptoms
                .filter(
                    (item) =>
                        item.createdAt >= fromQuery &&
                        item.createdAt <= toQuery,
                )
                .sort((a, b) => {
                    let order = 0;
                    if (a.createdAt < b.createdAt) order = -1;
                    else if (a.createdAt > b.createdAt) order = 1;
                    return descQuery ? order * -1 : order;
                }) || [];

        return res
            .json({
                user: userId,
                symptoms: userSymptoms,
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

    const { description, severity, note } = req.body;

    try {
        const symptoms = await Symptoms.findOneAndUpdate(
            {
                user: userId,
            },
            {},
            { upsert: true, new: true, setDefaultsOnInsert: true },
        );

        const index =
            symptoms.symptoms.push({ description, severity, note }) - 1;
        await symptoms.save();

        return res.json(symptoms.symptoms.at(index)).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.get("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const symptoms = await Symptoms.findOne({
            user: userId,
            "symptoms._id": req.params.entryId,
        });
        return symptoms?.symptoms?.at(0)
            ? res.json(symptoms.symptoms[0]).end()
            : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.put("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const { description, severity, note } = req.body;

    try {
        const symptoms = await Symptoms.findOneAndUpdate(
            {
                user: userId,
                "symptoms._id": req.params.entryId,
            },
            {
                $set: {
                    "symptoms.$": { description, severity, note },
                },
            },
        );
        return symptoms?.symptoms?.at(0)
            ? res.json(symptoms.symptoms[0]).end()
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
        const symptoms = await Symptoms.findOneAndUpdate(
            {
                user: userId,
            },
            {
                $pull: {
                    symptoms: { _id: req.params.entryId },
                },
            },
        );
        return symptoms?.symptoms?.at(0)
            ? res.end()
            : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

export default router;
