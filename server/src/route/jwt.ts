import "dotenv/config";
import Jwt from "jsonwebtoken";

const JWT_SECRET = process.env.JWT_SECRET || "123456";
const TOKEN_EXPIRE = 86400000;

function signJwt(payload: string | object | Buffer) {
    return Jwt.sign(payload, JWT_SECRET, {
        expiresIn: TOKEN_EXPIRE,
    });
}

function verifyJwt(token: string) {
    return Jwt.verify(token, JWT_SECRET);
}

export default {
    TOKEN_EXPIRE,
    signJwt,
    verifyJwt,
};
