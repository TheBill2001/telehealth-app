import database from "./connection.js";

export interface ISymptom {
    userId: database.Types.ObjectId;
    description: string;
    severity: number;
    note?: string;
    createdAt?: Date;
    updatedAt?: Date;
}

const symptomSchema = new database.Schema<ISymptom>(
    {
        userId: {
            required: true,
            type: database.Schema.Types.ObjectId,
            ref: "User",
        },

        description: {
            type: String,
            required: true,
        },
        severity: {
            type: Number,
            min: 1,
            max: 10,
            required: true,
        },
        note: {
            type: String,
        },
    },
    { timestamps: true },
);

const Symptoms = database.model<ISymptom>("Symptoms", symptomSchema);
export default Symptoms;
