const mongoose = require('mongoose')

const { Schema } = mongoose


const connectionString = 'mongodb://111.231.244.208:27018';

mongoose.connect(connectionString)
mongoose.Promise = global.Promise;

const lockSchema = new Schema({
    addr: String,
    isImport: Boolean,
    name: String
});

const userSchema = new Schema({
    name: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    locks: [lockSchema]
});
userSchema.methods.getAllLock = function () {
    return this.locks;
};

userSchema.statics.findByName = function (name) {
    return this.findOne({ name });
};


const m = mongoose.model('User', userSchema);

(async function () {
    const lyh = await m.findByName('lyh')
    console.log(lyh)
})()