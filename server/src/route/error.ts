import { Request, Response } from "express";

/**
 * Respone with Internal Server Error - Code 500
 * @param res response object
 */
function internalError(res: Response) {
    return res.status(500).json({ message: "Internal Server Error" }).end();
}

/**
 * Respone with Unauthorized - Code 401
 * @param res response object
 */
function unauthorized(res: Response) {
    return res.status(401).json({ message: "Unauthorized" }).end();
}

function unsupportedMediaType(req: Request, res: Response, type: string) {
    if (!req.is(type)) {
        res.status(415)
            .json({
                message: `Unsupported media type: expected ${type}, got ${req.get(
                    "Content-Type",
                )}`,
            })
            .end();
        return false;
    }
    return true;
}

/**
 * Respone with Conflicted - Code 409
 * @param res response object
 */
function conflict(res: Response, reason: string) {
    return res.status(409).json({ message: reason }).end();
}

/**
 * Respone with Missing - Code 409
 */
function missing(res: Response, reason: string) {
    return res.status(404).json({ message: reason }).end();
}

/**
 * Respone with Unprocessable Entity - Code 422
 */
function unprocessableEntity(res: Response, reason: string) {
    return res.status(422).json({ message: reason }).end();
}

export default {
    internalError,
    unauthorized,
    unsupportedMediaType,
    conflict,
    missing,
    unprocessableEntity,
};
