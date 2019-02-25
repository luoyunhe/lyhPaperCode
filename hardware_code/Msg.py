class MsgType:
    ETH_UPDATE = 1
    SCAN_QR_CODE = 2
    RandomStr = 3


class Msg:
    def __init__(self, msgType, load):
        self.msgType = msgType
        self.load = load