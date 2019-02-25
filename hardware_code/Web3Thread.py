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
        self.web3 = None
        self.running = True
        self.contractAddr = address
        self.queue = q
        self.eventFilter = None
        self.locker = None

    def getUserInfo(self):
        return self.locker.functions.getUserInfo().call()

    def init(self):
        infuraProvider = Web3.WebsocketProvider(self.infuraURL)
        self.web3 = Web3(infuraProvider)
        self.locker = self.web3.eth.contract(address=Web3.toChecksumAddress(self.contractAddr), abi=self.contractAPi)

    def getFilter(self):
        eventFilter = self.locker.events.Update.createFilter(fromBlock="latest")
        return eventFilter


    def run(self):
        self.init()
        self.eventFilter = self.getFilter()
        userInfo = self.getUserInfo()
        msg = Msg(msgType=MsgType.ETH_UPDATE, load=userInfo)
        logging.info("web3 thread put msg: " + json.dumps(msg.load, indent=2))
        self.queue.put(msg)
        logging.info("web3 thread is running...")
        while self.running:
            try:
                for event in self.eventFilter.get_new_entries():
                    self.handle_event(event)
            except Exception as e:
                logging.info(e)
                self.init()
                self.eventFilter = self.getFilter()
                userInfo = self.getUserInfo()
                msg = Msg(msgType=MsgType.ETH_UPDATE, load=userInfo)
                logging.info("web3 thread put msg: " + json.dumps(msg.load, indent=2))
                self.queue.put(msg)

            time.sleep(5)

    def handle_event(self, event):
        userInfo = self.getUserInfo()
        msg = Msg(msgType=MsgType.ETH_UPDATE, load=userInfo)
        logging.info("web3 thread put msg: " + json.dumps(msg.load, indent=2))
        self.queue.put(msg)

    def stopThread(self):
        self.running = False