const throwAndExit = (error: unknown) => {
    console.log(error);
    process.exit(1);
};

export default {
    throwAndExit,
};
