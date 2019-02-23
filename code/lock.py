import hashlib
import os
import queue
import json
import logging

import requests

from cacheout import Cache
from Msg import MsgType
from ScanQRCode import ScanQRCode
from Web3Thread import Web3Thread
from RandomStr import RandomStr


class LockDB:
    def __init__(self, dbName):
        if not os.path.exists('./db.txt'):
            os.mknod(dbName)
        self.db = open(dbName, "r+", encoding="utf-8")
        logging.info("db init...")

    def getInfo(self):
        self.db.seek(0)
        dbStr = self.db.read()
        if len(dbStr) == 0:
            dbStr = "{}"
        logging.info(dbStr)
        info = json.loads(dbStr)
        logging.info("load db: " + json.dumps(info, indent=2))
        return info

    def update(self, info):
        self.db.truncate(0)
        self.db.seek(0)
        self.db.write(json.dumps(info))
        self.db.flush()

    def close(self):
        self.db.close()

DB_NAME = "db.txt"
SERVER_URL = "localhost:9999"


def decodeUserInfo(userInfo):
    userMap = {}
    names = userInfo[1].split()
    pubKeys = userInfo[2].split()
    for index, addr in enumerate(userInfo[0]):
        userMap["addr"] = [names[index], pubKeys[index]]
    return userMap



def main():
    BASE_URL = "https://172.16.0.103:9999"

    q = queue.Queue(100)
    scanQRCodeThread = ScanQRCode(q=q)
    scanQRCodeThread.start()
    randomStrThread = RandomStr(q=q)
    randomStrThread.start()
    web3Thread = None
    md5 = hashlib.md5()
    db = LockDB(DB_NAME)
    info = db.getInfo()
    cache = Cache(ttl=60*8)


    if info.get("contractAddr"):
        web3Thread = Web3Thread(address=info["contractAddr"], q=q)
        web3Thread.start()


    userMap = info.get("userMap", {})

    logging.info("main thread running...")
    while True:
        msg = q.get()
        if msg.msgType == MsgType.SCAN_QR_CODE:
            codeStr = msg.load
            bindInfo = None
            try:
                bindInfo = json.loads(str(codeStr, encoding="utf-8"))
            except BaseException as e:
                logging.info(e)

            logging.info(bindInfo)
            salt = bindInfo.get("salt")
            address = bindInfo.get("addr")
            logging.info(salt)
            logging.info(address)
            if salt and address:
                info["salt"] = salt
                info["contractAddr"] = address
                db.update(info)

                if web3Thread:
                    web3Thread.stopThread()
                    web3Thread.join()
                    q.clear()

                web3Thread = Web3Thread(address=address, q=q)
                web3Thread.start()

        elif msg.msgType == MsgType.ETH_UPDATE:
            userMap = decodeUserInfo(msg.load)
            info["userMap"] = userMap
            db.update(info)

        elif msg.msgType == MsgType.RandomStr:
            ranStr = msg.load
            if info.get("contractAddr"):
                cache.add(ranStr, None)
                salt = info["salt"]
                data = {}
                data["address"] = info.get("contractAddr", "")
                data["ranStr"] = ranStr
                data["salt"] = info.get("salt", "")
                signStr = ""
                for k in sorted(data.keys()):
                    signStr = signStr + k + "=" + data[k] + ";"
                md5 = hashlib.md5()
                md5.update(signStr.encode(encoding="utf-8"))
                data["sign"] = md5.hexdigest()
                data.pop("salt")
                requests.post(url=BASE_URL + "/api/lock/randomstr", data=data, verify=False)






if __name__ == '__main__':
    logging.basicConfig(format='%(asctime)s-%(pathname)s[line:%(lineno)d]-%(levelname)s: %(message)s',
                        level=logging.INFO)
    main()


