import logging
import threading
import time
from imutils.video import VideoStream
from cacheout import Cache
from pyzbar import pyzbar
import imutils
from Msg import Msg, MsgType


class ScanQRCode(threading.Thread):
    def __init__(self, q):
        threading.Thread.__init__(self)
        self.running = True
        self.queue = q
        self.vs = None
        self.cache = Cache(ttl=5)

    def stopThread(self):
        self.running = False
        if not self.vs:
            self.vs.stop()

    def run(self):
        self.vs = VideoStream(usePiCamera=True).start()
        time.sleep(2)
        logging.info("scan qrcode thread is running...")
        while self.running:
            frame = self.vs.read()
            frame = imutils.resize(frame, width=400)
            barcodes = pyzbar.decode(frame)
            for barcode in barcodes:
                data = barcode.data
                if self.cache.has(data):
                    continue
                self.cache.add(data, None)
                msg = Msg(msgType=MsgType.SCAN_QR_CODE, load=barcode.data)
                logging.info("scan qrcode thread put msg: ", msg.load)
                self.queue.put(msg)