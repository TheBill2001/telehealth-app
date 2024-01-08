import { Request, Response } from "express";

function errorOccurred(res: Response, code: number, reason?: string) {
    res.status(code);
    return reason ? res.json({ message: reason }).end() : res.end();
}

/**
 * Respone with Internal Server Error - Code 500
 * @param res response object
 */
function internalError(res: Response) {
    return errorOccurred(res, 500, "Internal Server Error");
}

/**
 * Respone with Unauthorized - Code 401
 * @param res response object
 */
function unauthorized(res: Response) {
    return errorOccurred(res, 401, "Unauthorized");
}

function unsupportedMediaType(req: Request, res: Response, type: string) {
    if (!req.is(type)) {
        errorOccurred(
            res,
            415,
            `Unsupported media type: expected ${type}, got ${req.get(
                "Content-Type",
            )}`,
        );
        return false;
    }
    return true;
}

/**
 * Respone with Conflicted - Code 409
 * @param res response object
 */
function conflict(res: Response, reason?: string) {
    return errorOccurred(res, 409, reason);
}

/**
 * Respone with Unprocessable Entity - Code 422
 */
function unprocessableEntity(res: Response, reason: string) {
    return errorOccurred(res, 422, reason);
}

/**
 * Respone with Not found - Code 404
 */
function notFound(res: Response, reason?: string) {
    return errorOccurred(res, 422, reason);
}

/**
 * Respone with Forbidden - Code 403
 */
function forbidden(res: Response, reason?: string) {
    return errorOccurred(res, 403, reason);
}

export default {
    internalError,
    unauthorized,
    unsupportedMediaType,
    conflict,
    unprocessableEntity,
    notFound,
    forbidden,
};
