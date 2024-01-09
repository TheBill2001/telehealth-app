import database from "./connection.js";

export enum VaccineType {
    Vaccine = "Vaccine",
    Booster = "Booster",
}

export interface IVaccineHistory {
    userId: database.Types.ObjectId;
    name: string;
    type: VaccineType;
    facility: string;
    date: Date;
}

const vaccineHistorySchema = new database.Schema<IVaccineHistory>({
    userId: {
        required: true,
        type: database.Schema.Types.ObjectId,
        ref: "User",
        unique: true,
    },
    name: {
        type: String,
        required: true,
    },
    type: {
        type: String,
        required: true,
        enum: VaccineType,
    },
    facility: {
        type: String,
        required: true,
    },
    date: {
        type: Date,
        required: true,
        default: Date.now,
    },
});

export const VaccineHistory = database.model<IVaccineHistory>(
    "VaccineHistory",
    vaccineHistorySchema,
);

export enum VaccineRegistractionStatus {
    Pending = "Pending",
    Canceled = "Canceled",
    Accepted = "Accepted",
    Finished = "Finished",
}

export interface IVaccineRegistraction {
    userId: database.Types.ObjectId;
    name: string;
    type: VaccineType;
    status: VaccineRegistractionStatus;
    facility?: string;
    date?: Date;
    createdAt?: Date;
    updatedAt?: Date;
}

const vaccineRegistractionSchema = new database.Schema<IVaccineRegistraction>(
    {
        userId: {
            required: true,
            type: database.Schema.Types.ObjectId,
            ref: "User",
            unique: true,
        },
        name: {
            type: String,
            required: true,
        },
        type: {
            type: String,
            required: true,
            enum: VaccineType,
        },
        status: {
            type: String,
            required: true,
            enum: VaccineRegistractionStatus,
            default: VaccineRegistractionStatus.Pending,
        },
        facility: {
            type: String,
            required: false,
        },
        date: {
            type: Date,
            required: false,
        },
    },
    { timestamps: true },
);

export const VaccineRegistraction = database.model<IVaccineRegistraction>(
    "VaccineRegistraction",
    vaccineRegistractionSchema,
);
