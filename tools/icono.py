"""Dibuja el icono de la app en todos los tamaños de Android."""
import os
from PIL import Image, ImageDraw, ImageFont

S = 512
im = Image.new('RGBA', (S, S), (0, 0, 0, 0))
d = ImageDraw.Draw(im)
d.rounded_rectangle([0, 0, S - 1, S - 1], radius=110, fill=(31, 122, 77, 255))
d.polygon([(256, 90), (430, 240), (82, 240)], fill=(255, 255, 255, 255))
d.rectangle([125, 230, 387, 420], fill=(255, 255, 255, 255))
d.ellipse([186, 255, 326, 395], fill=(244, 180, 0, 255))
try:
    f = ImageFont.truetype('/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf', 70)
    d.text((256, 325), "S/", font=f, fill=(31, 122, 77, 255), anchor='mm')
except OSError:
    pass
for name, px in [('mdpi', 48), ('hdpi', 72), ('xhdpi', 96), ('xxhdpi', 144), ('xxxhdpi', 192)]:
    p = f'app/src/main/res/mipmap-{name}'
    os.makedirs(p, exist_ok=True)
    im.resize((px, px), Image.LANCZOS).save(f'{p}/ic_launcher.png')
