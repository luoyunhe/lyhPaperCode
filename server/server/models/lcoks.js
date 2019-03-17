import mongoose from 'mongoose';

const { Schema } = mongoose;

mongoose.Promise = global.Promise;

const recordSchema = new Schema({
    name: {
        type: String,
        required: true
    },
    timestamp: {
        type: Number,
        required: true
    }
});


const userSchema = new Schema({
    addr: {
        type: String,
        required: true
    },
    salt: {
        type: String,
        required: true
    },
    records: [recordSchema]

});

export default mongoose.model('Lock', userSchema);