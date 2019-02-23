import hashlib

import requests



print(requests.post(url="https://localhost:9999/api/lock/randomstr", data={"address": "0xjdfjfj", "ranStr":"jdsfj", "aa" :"lsdfjol", "sign": "lsjfj"}, verify=False))

hashlib.md5().update("slfjj".encode(encoding="utf-8")).hexdigest

