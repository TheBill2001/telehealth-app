import Router from "express";
import routeErrorHandler from "./error.js";
import routeUtil from "./util.js";
import CovidTest, { CovidTestType } from "../db/covidTest.js";

const router = Router();

router.use((req, res, next) => {
    res.setHeader("Connection", "close");

    next();
});

router.get("/", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const fromQuery = new Date(req.query.from?.toString() || 0);
    const toQuery = new Date(req.query.to?.toString() || Date.now());
    const descQuery = req.query.desc === "true" ? "desc" : "asc";

    try {
        const tests = await CovidTest.find({
            userId: userId,
            createdAt: {
                $gte: fromQuery,
                $lte: toQuery,
            },
        }).sort(descQuery);

        return res
            .json({
                userId: userId,
                tests: tests,
            })
            .end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.post("/", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const { positive } = req.body;

    try {
        const test = await CovidTest.create({
            userId: userId,
            type: CovidTestType.SelfReport,
            positive: positive,
        });

        return res.json(test.toJSON()).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.get("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const test = await CovidTest.findById(req.params.entryId);
        return test ? res.json(test).end() : routeErrorHandler.notFound(res);
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.put("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    const { positive } = req.body;

    try {
        const test = await CovidTest.findById(req.params.entryId);

        if (!test) return routeErrorHandler.notFound(res);

        if (test.type != CovidTestType.SelfReport)
            return routeErrorHandler.forbidden(res);

        test.positive = positive;
        await test.save();

        return res.json(test).end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

router.delete("/:entryId", async (req, res) => {
    const userId = await routeUtil.checkUserIdFromToken(req, res);
    if (!userId) return;

    try {
        const test = await CovidTest.findById(req.params.entryId);

        if (!test) return routeErrorHandler.notFound(res);

        if (test.type != CovidTestType.SelfReport)
            return routeErrorHandler.forbidden(res);

        await test.deleteOne();

        return res.end();
    } catch (error) {
        console.error(error);
        routeErrorHandler.internalError(res);
    }
});

export default router;
