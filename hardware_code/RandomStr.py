import random
import string
import threading
import time

import logging

from Msg import MsgType, Msg


class RandomStr(threading.Thread):
    def __init__(self, q):
        threading.Thread.__init__(self)
        self.queue = q
        self.running = True

    def run(self):
        logging.info("random str thread running...")
        while self.running:
            ranStr = str(int(time.time())) + ":" + "".join(random.sample(string.ascii_letters + string.digits, 8))
            msg = Msg(msgType=MsgType.RandomStr, load=ranStr)
            logging.info("random str thread put: " + ranStr)
            self.queue.put(msg)
            time.sleep(60)

    def stopThread(self):
        self.running = False
