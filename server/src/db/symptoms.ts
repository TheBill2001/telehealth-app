import database from "./connection.js";

export interface ISymptom {
    description: string;
    severity: number;
    note?: string;
    createdAt?: Date;
    updatedAt?: Date;
}

export interface IUserSymptom {
    user: database.Types.ObjectId;
    symptoms: ISymptom[];
}

type UserSymptomModelType = database.Model<IUserSymptom>;

const userSymptomSchema = new database.Schema<
    IUserSymptom,
    UserSymptomModelType
>({
    user: {
        required: true,
        type: database.Schema.Types.ObjectId,
        ref: "User",
        unique: true,
    },
    symptoms: [
        {
            type: new database.Schema<ISymptom>(
                {
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
            ),
        },
    ],
});

const Symptoms = database.model<IUserSymptom, UserSymptomModelType>(
    "Symptoms",
    userSymptomSchema,
);
export default Symptoms;
