import bcrypt from 'bcrypt'
import crypto from 'crypto'
import cache from 'memory-cache'
import LockModel from '../models/lcoks'
import getContractOwnerName from '../sc/lock';

function getRandomSalt() {
}
function getRanStr() {
    return bcrypt.genSaltSync(10);
}

const randomStrCache = new cache.Cache();
const importLockCache = new cache.Cache();


class LockControllers {
    async getSalt(ctx) {
        const contractAddr = ctx.request.body.contractAddr || '';
        if (contractAddr === '') {
            ctx.status = 200;
            ctx.body = {
                code: -1,
                msg: "empty address"
            }
            return;
        }
        let userName = ctx.state.user.userName;
        let contractOwnerName = await getContractOwnerName(contractAddr);
        if (userName !== contractOwnerName) {
            ctx.status = 200;
            ctx.body = {
                code: -1,
                msg: "Authentication Error"
            }
        }
        
        console.log(userName);

        const lockFound = await LockModel.find({ addr: contractAddr });
        let salt = '';
        if (lockFound.length === 0) {
            salt = getRandomSalt();
            const lock = new LockModel({ addr: contractAddr, salt });
            lock.save();
        } else {
            salt = lockFound[0].salt;
        }

        ctx.body = { salt, code: 0 };
        ctx.status = 200;
    }


    async setRandomStr(ctx) {
        console.log(ctx.request.body)
        ctx.status = 200;
        const { body } = ctx.request;
        const { address, ranStr, sign } = body;
        const lockFound = await LockModel.find({ addr: address });
        if (lockFound.length === 0) {
            ctx.body = {
                msg: "unknow address",
                code: -1
            };
            return;
        }
        const { salt } = lockFound[0];

        body.salt = salt;

        const feilds = Object.keys(body)

        feilds.sort()
        let signStr = "";
        for (let i = 0; i < feilds.length; i += 1) {
            if (feilds[i] !== "sign") {
                signStr = `${signStr + feilds[i]}=${body[feilds[i]]};`;
            }
        }
        const md5 = crypto.createHash('md5')
        const mySign = md5.update(signStr).digest("hex")
        if (mySign === sign) {
            ctx.body = {
                code: 0,
                msg: "success"
            };
            randomStrCache.put(address, ranStr, 1000 * 60 * 10)

        } else {
            ctx.body = {
                code: -1,
                msg: "error sign"
            };
        }
        console.log(mySign)
        console.log(signStr)
        console.log(feilds)

    }

    async getRandomStr(ctx) {
        ctx.status = 200;
        const { address } = ctx.request.query;
        console.log(address)
        let ranStr = "";
        if (address) {
            ranStr = randomStrCache.get(address)
        }
        if (ranStr) {
            ctx.body = {
                code: 0,
                ranStr,
                msg: "success"
            }
        } else {
            ctx.body = {
                code: -1,
                msg: "not found"
            }
        }
    }

    async addRecord(ctx) {
        ctx.status = 200;


    }

    async genImportLock(ctx) {
        ctx.status = 200;
        let key = Data.now().toString() + getRanStr();
        let value = {
            activateStr: "",
            passwd: getRanStr()
        };
        importLockCache.put(key, JSON.stringify(value), 1000 * 60 * 60 * 24);
        ctx.body = {
            code: 0,
            key,
            passwd: value.passwd
        }
    }
    async setImportLock(ctx) {
        ctx.status = 200;
        let key = ctx.request.body.key;
        let passwd = ctx.request.body.passwd;
        let activateStr = ctx.request.body.key;
        let valueStr = importLockCache.get(key)
        if (valueStr) {
            let value = JSON.parse(valueStr);
            if (value.passwd !== passwd) {
                ctx.body = {
                    code: -1,
                    msg: "passwd error"
                }
            }
            value.activateStr = activateStr;
            importLockCache.put(key, JSON.stringify(value), 1000 * 60 * 60 * 24);
            ctx.body = {
                code: 0,
                msg: "success"
            }
        } else {
            ctx.body = {
                code: -1,
                msg: "too later, value have been delete"
            }
        }

    }
    async getImportLock(ctx) {
        ctx.status = 200;
        let key = ctx.request.body.key;
        let valueStr = importLockCache.get(key);
        if (valueStr) {
            let value = JSON.parse(valueStr);
            if (value.activateStr !== '') {
                ctx.body = {
                    code: 0,
                    activateStr
                }
                return
            }
        }
        ctx.body = {
            code: -1,
            msg: "not set or error"
        }


    }
}

export default new LockControllers();