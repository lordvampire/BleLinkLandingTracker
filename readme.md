# 🚀 Bluetooth LE App - Jetpack Compose & Kotlin

An Android app built with **Jetpack Compose**, **Kotlin** that enables Bluetooth Low Energy (BLE) communication.
The app allows users to **scan for BLE devices**, **connect**, **explore services, characteristics, and descriptors**, and **perform read/write operations**.

## 🛠 Features

- 🔍 Scan for nearby Bluetooth LE devices
- 🔗 Connect and disconnect from BLE devices
- 📜 Discover services, characteristics, and descriptors
- 📡 Read and write data from BLE characteristics
- 🔄 Subscribe to characteristic notifications and indications
- 🔒 Uses Google Accompanist for runtime permissions
- 🏗 Modular architecture with Hilt for Dependency Injection
- 📱 Modern UI with Jetpack Compose


## 📦 Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Dependency Injection:** Hilt
- **Permissions Handling:** Google Accompanist
- **Bluetooth Communication:** Android BLE API
- **Architecture:** MVVM


## 🔌 BLE Commands & Data Format

| **Operation** | **Description** | **Example Data** |
|--------------|---------------|------------------|
| **Read** | Reads data from a characteristic | `byteArrayOf(0x00, 0x01, 0x02)` |
| **Write** | Writes data to a characteristic | `byteArrayOf(0xA1, 0xB2, 0xC3)` |
| **Notify** | Subscribes to real-time updates | Device sends periodic data updates |

## 🏗 Architecture

The app follows the **MVVM (Model-View-ViewModel) architecture**, ensuring a clean separation of concerns:

📂 app
├── 📂 di # Hilt Dependency Injection
├── 📂 ui # Jetpack Compose UI
├── 📂 bluetooth # BLE Logic
├── 📂 viewmodel # Business Logic
└── 📂 repository # Data Management