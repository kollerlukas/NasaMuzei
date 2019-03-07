[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3d841b67a07d41d59067d58939a6e234)](https://app.codacy.com/app/kollerlukas/NasaMuzei?utm_source=github.com&utm_medium=referral&utm_content=kollerlukas/NasaMuzei&utm_campaign=Badge_Grade_Settings)
[![Build Status](https://travis-ci.org/kollerlukas/NasaMuzei.svg?branch=master)](https://travis-ci.org/kollerlukas/NasaMuzei)

<img src="https://github.com/kollerlukas/NasaMuzei/raw/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="Screenshot Main"
width="100">

# Muzei Source: NASA APOD
Simple [Muzei](http://muzei.co/) Source providing the [Astronomy Picture of the Day by NASA](https://apod.nasa.gov/apod/astropix.html).

<div>
<img src="https://github.com/kollerlukas/NasaMuzei/raw/master/screenshots/screenshot-main.png" alt="Screenshot Main" width="200">
<img src="https://github.com/kollerlukas/NasaMuzei/raw/master/screenshots/screenshot-source.png" alt="Screenshot Source" width="200">
</div>

I used the Muzei Logo in App icon for this App (taken from [link](https://github.com/romannurik/muzei/blob/master/art/ic_complication.svg)).

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
