# About Android Image Gallery Sample

This repository holds the sample source code for an Android image gallery app showing images from Oracle Content Management.

Please see the complete [tutorial](https://www.oracle.com/pls/topic/lookup?ctx=cloud&id=oce-android-gallery-sample).

## Installation

Source code may be obtained from Github:

```
git clone https://github.com/oracle-samples/oce-android-gallery-sample
```

## Running the project

Import the project in Android Studio.

Open the file `strings.xml` and provide information about your Oracle Content Management instance.  This is already set with the values below by default.

```xml 
<string name="default_server_url">https://samples.mycontentdemo.com</string>
<string name="default_channel_token">e0b6421e73454818948de7b1eaddb091</string>
```

- `default_server_url` - the full URL (and optional port) for your Content Management instance
- `default_channel_token` - the token associated with the channel to which your assets were published

Once you have provided the necessary credential information, select an appropriate Android target device and click the Run button.

This sample may be run on-device or in an emulator. The functionality will be identical in both.

## Images

Sample images may be downloaded from [https://www.oracle.com/middleware/technologies/content-experience-downloads.html](https://www.oracle.com/middleware/technologies/content-experience-downloads.html) under a separate license.  These images are provided for reference purposes only and may not be hosted or redistributed by you.

## Contributing

This project welcomes contributions from the community. Before submitting a pull
request, please [review our contribution guide](./CONTRIBUTING.md).

## Security

Please consult the [security guide](./SECURITY.md) for our responsible security
vulnerability disclosure process.

## License

Copyright (c) 2023, Oracle and/or its affiliates and released under the
[Universal Permissive License (UPL)](https://oss.oracle.com/licenses/upl/), Version 1.0