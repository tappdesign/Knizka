 ![icon](Assets\logo.png)

Knižka
==========

<b>open-source</b> software aimed to have useful prayer book application.

The project is based on Omni-Notes project developed by Federico Iosue. More info about Omni-Notes can be found https://github.com/federicoiosue/Omni-Notes

The project was inspired by the absence of such applications. It aims to provide an attractive look and follow the most recent design guidelines of the Google operating system.

**Follow the developments and post your comments and advice on Github**

## Features

Currently the following functions are implemented:

* Material Design interface
  *Basic add, modify, archive, trash and delete prayers actions
  
* Share, merge and search prayers

* Image, audio and generic file attachments

* Manage your notes using tags and categories

* Sketch-prayer mode

* Notes shortcut on home screen

* Export/import notes to backup

* Google Now integration: just tell "write a prayer" followed by the content

* Multiple widgets, DashClock extension, Android 4.2 lockscreen compatibility

  

You can find a complete changelog inside the application settings menu!

## User guide

Look into the wiki for GIFs-based tutorials: **[TBD]**

## Build

**[TBD]** Watch the following terminal session recording on how to compile distributable files
[![asciicast](https://asciinema.org/a/102898.png)](https://asciinema.org/a/102898)

To be sure that build environment is fully compliant with the project the following command creates a container with all the needed tools to compile the code:

```
cd {project-folder}; rm local.properties; docker rm android; docker run -v $PWD:/workspace --name android tabrindle/min-alpine-android-sdk:latest bash -c "yes | sdkmanager --update && yes | sdkmanager --licenses && cd /workspace && ./gradlew build --stacktrace -Dorg.gradle.daemon=true -Pandroid.useDeprecatedNdk=true"

```

## Test

To execute all tests included into the project connect a device or emulator, then run the following command:

```shell
./gradlew connectedAndroidTest
```

## Contributing

Due to the fact that I'm using [gitflow](https://github.com/nvie/gitflow) as code versioning methodology, you as developer should **always** start working on [develop branch](https://github.com/federicoiosue/Omni-Notes/tree/develop) that contains the most recent changes.

There are many features/improvements that are not on **my** roadmap but someone else could decide to work on them anyway: hunt for issues tagged as [Help Wanted](https://github.com/federicoiosue/Omni-Notes/issues?utf8=✓&q=label%3A"Help+wanted") to find them!

Feel free to add yourself to [contributors.md](https://github.com/federicoiosue/Omni-Notes/blob/develop/contributors.md) file.

### New feature or improvements contributions

This kind of contributions **must** have screenshots or screencast as demonstration of the new additions.

### Code style

If you plan to manipulate the code then you'll have to do it by following a [specific code style](https://gist.github.com/federicoiosue/dee53e882b3c70d544f8608769eb02fc).
Also pay attention if you're using any plugin that automatically formats/cleans/rearrange your code and set it to only change that code that you touched and not the whole files.

### Test your code contributions!

All code changes and additions **must** be tested.
See the [related section](#test) for more information or this two pull requests comments: [one](https://github.com/federicoiosue/Omni-Notes/pull/646#pullrequestreview-187973443) and [two](https://github.com/federicoiosue/Omni-Notes/pull/683#issuecomment-506206689)

### Forking project

When forking the project you'll have to modify some files that are strictly dependent from my own development / build / third-party-services environment. Files that need some attention are the following:

  - *gradle.properties*: this is overridden by another file with the same name inside the *Knizka* module. You can do the same or leave as it is, any missing property will let the app gracefully fallback on a default behavior.

## Dependencies

They're all listed into the [build.gradle](https://github.com/federicoiosue/Omni-Notes/blob/develop/omniNotes/build.gradle) file but due to the fact that many of the dependences have been customized by me I'd like to say thanks here to the original developers of these great libraries:

* https://github.com/RobotiumTech/robotium
* https://github.com/LarsWerkman/HoloColorPicker
* https://github.com/keyboardsurfer/Crouton
* https://github.com/romannurik/dashclock/wiki/API
* https://github.com/ACRA/acra
* https://github.com/Shusshu/Android-RecurrencePicker
* https://github.com/gabrielemariotti/changeloglib
* https://github.com/greenrobot/EventBus
* https://github.com/futuresimple/android-floating-action-button
* https://github.com/keyboardsurfer
* https://github.com/bumptech/glide
* https://github.com/neopixl/PixlUI
* https://github.com/afollestad/material-dialogs
* https://github.com/ical4j
* https://github.com/square/leakcanary
* https://github.com/pnikosis/materialish-progress
* https://github.com/apl-devs/AppIntro
* https://github.com/ReactiveX/RxAndroid
* https://github.com/artem-zinnatullin/RxJavaProGuardRules
* https://github.com/tbruyelle/RxPermissions
* https://github.com/ocpsoft/prettytime
* https://github.com/piwik/piwik-sdk-android
* https://github.com/mrmans0n/smart-location-lib

## Developed by


* TappDesign Studios
* Federico Iosue - [Website](https://federico.iosue.it)



## License


    Copyright (C) 2020 TappDesign Studios 
    Copyright (C) 2013-2020 Federico Iosue (federico@iosue.it)
    
    This software is based on Omni-Notes project developed by Federico Iosue
    https://github.com/federicoiosue/Omni-Notes
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

