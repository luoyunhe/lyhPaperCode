import json
import logging
import threading
import time

from web3 import Web3

from Msg import Msg, MsgType


class Web3Thread(threading.Thread):
    def __init__(self, address, q):
        threading.Thread.__init__(self)
        with open('./contract/locker_sol_Locker.abi', encoding='UTF-8') as f:
            self.contractAPi = f.read()

        self.infuraURL = "wss://ropsten.infura.io/ws/v3/fc331766f3c8401d862db11cb785fc07"
        infuraProvider = Web3.WebsocketProvider(self.infuraURL)
        self.web3 = Web3(infuraProvider)
        self.running = True
        self.contractAddr = address
        self.queue = q
        self.eventFilter = None

    def getUserInfo(self, locker):
        return locker.functions.getUserInfo().call()

    def run(self):
        locker = self.web3.eth.contract(address=Web3.toChecksumAddress(self.contractAddr), abi=self.contractAPi)
        userInfo = self.getUserInfo(locker)
        msg = Msg(msgType=MsgType.ETH_UPDATE, load=userInfo)
        logging.info("web3 thread put msg: " + json.dumps(msg.load, indent=2))
        self.queue.put(msg)
        self.eventFilter = locker.events.Update.createFilter(fromBlock="latest")
        logging.info("web3 thread is running...")
        while self.running:
            try:
                for event in self.eventFilter.get_new_entries():
                    self.handle_event(event, locker)
            except Exception as e:
                logging.info(e)
                self.web3 = Web3(Web3.WebsocketProvider(self.infuraURL))
            time.sleep(5)

    def handle_event(self, event, locker):
        userInfo = self.getUserInfo(locker)
        msg = Msg(msgType=MsgType.ETH_UPDATE, load=userInfo)
        logging.info("web3 thread put msg: " + json.dumps(msg.load, indent=2))
        self.queue.put(msg)

    def stopThread(self):
        self.running = False