import "dotenv/config";
import common from "../common.js";
import validator from "validator";
import database from "./connection.js";

enum UserRole {
    Admin = 0,
    Provider = 1,
    User = 2,
}

interface IUserInfo {
    name: string;
    phone: string;
    email: string;
    dateOfBirth: Date;
    citizenID: string;
}

interface IUser {
    username: string;
    password: string;
    role: UserRole;
    userInfo?: IUserInfo;
}

type UserModelType = database.Model<IUser>;

const userSchema = new database.Schema<IUser, UserModelType>({
    username: { type: String, required: true, unique: true },
    password: { type: String, required: true },
    role: { type: Number, required: true, default: UserRole.User },
    userInfo: new database.Schema<IUserInfo>({
        name: { type: String, required: true },
        phone: {
            type: String,
            required: true,
            validate: {
                validator: (value) => {
                    return validator.isMobilePhone(value, "vi-VN");
                },
                message: (props) =>
                    `${props.value} is not a valid phone number!`,
            },
        },
        email: {
            type: String,
            validate: {
                validator: (value) => {
                    return validator.isEmail(value);
                },
                message: (props) =>
                    `${props.value} is not a valid phone number!`,
            },
        },
        dateOfBirth: { type: Date, required: true },
        citizenID: { type: String, required: true },
    }),
});

const User = database.model<IUser, UserModelType>("User", userSchema);

const ADMIN_USERNAME = process.env.ADMIN_USERNAME || "admin";
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD || "123456";

try {
    let adminRecord = await User.findOne({
        username: ADMIN_USERNAME,
        role: UserRole.Admin,
    });

    if (adminRecord) {
        if (adminRecord.password != ADMIN_PASSWORD) {
            adminRecord.password == ADMIN_PASSWORD;
            adminRecord.save();
        }
    } else {
        adminRecord = await User.create({
            username: ADMIN_USERNAME,
            password: ADMIN_PASSWORD,
            role: UserRole.Admin,
        });

        if (!adminRecord) {
            common.throwAndExit("Cannot update Admin account.");
        }
    }

    console.debug(
        `Admin account ${JSON.stringify({
            username: adminRecord.username,
            password: adminRecord.password,
        })}`,
    );
} catch (error) {
    common.throwAndExit(error);
}

export default User;
export { User, UserRole };
