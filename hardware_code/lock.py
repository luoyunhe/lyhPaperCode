import base64
import hashlib
import os
import queue
import json
import logging
import grpc
import lock_pb2
import lock_pb2_grpc

import requests
from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15

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
        userMap[addr.lower()] = [names[index], pubKeys[index]]
    return userMap

def rsa_signverify(message,signature):
    public_key = RSA.importKey(open("my_rsa_public.pem").read())
    try:
        pkcs1_15.new(public_key).verify(message, base64.b64decode(signature))
        print('The signature is valid.')
        return True
    except (ValueError,TypeError):
        print('The signature is invalid.')

# def decryptSign(rpc, pubKey, sign):
#     logging("rpc call decrypt begin...")
#     resp = rpc.decrypt(lock_pb2.Request(pubKey=pubKey, sign=sign))
#     logging("rpc call decrypt end...")
#     return resp.ranStr

def openDoor():
    logging.error("open door...")

def main():
    BASE_URL = "https://172.16.0.103:9999"

    q = queue.Queue(100)
    scanQRCodeThread = ScanQRCode(q=q)
    scanQRCodeThread.start()
    randomStrThread = RandomStr(q=q)
    randomStrThread.start()
    web3Thread = None
    db = LockDB(DB_NAME)
    info = db.getInfo()
    cache = Cache(ttl=60*5)
    channel = grpc.insecure_channel("localhost:50000")
    stub = lock_pb2_grpc.LockStub(channel)




    if info.get("contractAddr"):
        web3Thread = Web3Thread(address=info["contractAddr"], q=q)
        web3Thread.start()


    userMap = info.get("userMap", {})

    logging.info("main thread running...")
    while True:
        msg = q.get()
        if msg.msgType == MsgType.SCAN_QR_CODE:
            codeStr = msg.load
            codeInfo = None
            try:
                codeInfo = json.loads(str(codeStr, encoding="utf-8"))
            except BaseException as e:
                logging.info(e)
                continue
            if not isinstance(info, dict):
                logging.info("continue qr code deal")
                continue
            logging.info(codeInfo)
            salt = codeInfo.get("salt")
            address = codeInfo.get("addr")
            sign = codeInfo.get("sign")
            if salt and address:
                info["salt"] = salt
                info["contractAddr"] = address
                db.update(info)

                if web3Thread:
                    web3Thread.stopThread()
                    web3Thread.join()
                    # q.clear()
                web3Thread = Web3Thread(address=address, q=q)
                web3Thread.start()
            elif sign and address:
                logging.info(userMap)
                infoList = userMap.get(address)
                logging.info(infoList)
                if infoList:
                    logging.warning("here")
                    pubKey = infoList[1]
                    decryptStr = ""
                    resp = ""
                    try:
                        resp = stub.decrypt(lock_pb2.Request(pubKey=pubKey, sign=sign))
                    except Exception as e:
                        logging.info(e)
                        continue
                    scanRanStr = resp.ranStr
                    logging.info("decrypt str: " + scanRanStr)
                    if cache.has(scanRanStr):
                        openDoor()




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


