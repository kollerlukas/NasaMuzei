[![Build Status](https://travis-ci.org/kollerlukas/NasaMuzei.svg?branch=master)](https://travis-ci.org/kollerlukas/NasaMuzei)

<img src="https://github.com/kollerlukas/NasaMuzei/raw/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="Screenshot Main"
width="100">

# Muzei Source: NASA APOD
Simple [Muzei](http://muzei.co/) Source providing the [Astronomy Picture of the Day by NASA](https://apod.nasa.gov/apod/astropix.html).

<div>
<img src="https://github.com/kollerlukas/NasaMuzei/raw/master/screenshots/screenshot-main.png" alt="Screenshot Main" width="200">
<img src="https://github.com/kollerlukas/NasaMuzei/raw/master/screenshots/screenshot-source.png" alt="Screenshot Source" width="200">
</div>

Muzei Logo (used in App icon) taken from: https://github.com/romannurik/muzei/blob/master/art/ic_complication.svg

## NASA Api key
Provide your own [NASA Api key](https://api.nasa.gov/index.html#apply-for-an-api-key) by changing the resource string in [api_key.xml](app/src/main/res/values/api_key.xml)
```xml
<resources>
    <!-- NASA api key -->
    <string name="nasa_api_key" translatable="false">[YOUR_OWN_API_KEY]</string>
</resources>
```
or just use the [demo key](https://api.nasa.gov/api.html#authentication)
```xml
<resources>
    <!-- NASA api key -->
    <string name="nasa_api_key" translatable="false">DEMO_KEY</string>
</resources>
```
