import database from "./connection.js";

export enum CovidTestType {
    SelfReport = 0,
    TestingFacility = 1,
}

export interface ICovidTestResult {
    userId: database.Types.ObjectId;
    positive: boolean;
    testingFacility?: string;
    type: CovidTestType;
    createdAt?: Date;
    updatedAt?: Date;
}

const covidTestResultSchema = new database.Schema<ICovidTestResult>(
    {
        userId: {
            type: database.Schema.Types.ObjectId,
            required: true,
            ref: "User",
        },
        positive: {
            required: true,
            type: Boolean,
            default: false,
        },
        testingFacility: {
            type: String,
            required: false,
        },
        type: {
            type: Number,
            enum: CovidTestType,
            default: CovidTestType.SelfReport,
        },
    },
    { timestamps: true },
);

const CovidTest = database.model<ICovidTestResult>(
    "CovidTest",
    covidTestResultSchema,
);

export default CovidTest;
