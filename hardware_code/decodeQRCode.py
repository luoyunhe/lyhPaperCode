from pyzbar import pyzbar
import cv2

image = cv2.imread("image1.jpg")

barcodes = pyzbar.decode(image)

print(barcodes)