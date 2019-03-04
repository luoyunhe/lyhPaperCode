import mongoose from 'mongoose';

const { Schema } = mongoose;

mongoose.Promise = global.Promise;

const userSchema = new Schema({
    addr: {
        type: String,
        required: true
    },
    salt: {
        type: String,
        required: true
    }
});

export default mongoose.model('Lock', userSchema);