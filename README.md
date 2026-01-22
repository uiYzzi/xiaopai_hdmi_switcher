# xiaopai_hdmi_switcher

一个简单的 Android TV 应用，用于快速切换 HDMI 输入源。

## 背景

由于小湃投影仪遥控器没有切换输入源的按钮，需要进入系统设置才能切换 HDMI。通过将自定义按钮绑定到这款应用，可以实现一键切换 HDMI 输入。

## 功能

- 启动应用后自动切换到第一个可用的 HDMI 输入源
- 无 UI 设计，启动后立即执行切换并退出
- 支持 Android TV 设备

## 构建

### 环境要求

- Android SDK
- Gradle 8.x
- Java 17

### 构建命令

```bash
./gradlew assembleRelease
```

构建产物位于：`app/build/outputs/apk/release/app-release.apk`

### 签名配置

在 `app/build.gradle` 中配置签名信息：

```groovy
signingConfigs {
    release {
        storeFile file('your-keystore.jks')
        storePassword 'your-password'
        keyAlias 'your-alias'
        keyPassword 'your-key-password'
    }
}
```

## 适配其他设备

如果需要适配非小湃设备，修改 `MainActivity.java` 中的目标应用包名：

```java
private static final String OPEN_PACKAGE_NAME = "com.your.device.tvapp";
private static final String OPEN_ACTIVITY_NAME = "com.your.device.tvapp.MainActivity";
```

## 开源协议

MIT License

Copyright (c) 2024 yuckkiko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
