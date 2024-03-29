import mongoose from 'mongoose';

const { Schema } = mongoose;

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


export default mongoose.model('User', userSchema);