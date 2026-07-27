# 💳 Net Banking App

A modern Android Net Banking application built using **Java**, **Material Design 3**, and **Firebase Realtime Database**. The app allows users to deposit and withdraw money while maintaining a minimum account balance and storing transaction history in the cloud.

---

## 📱 Features

- 💰 Deposit money
- 💸 Withdraw money
- 🏦 Live account balance
- ☁️ Firebase Realtime Database integration
- 📜 Transaction history with timestamps
- 🗑️ Delete transaction history
- ⚠️ Custom exception for insufficient funds
- 🎨 Material Design 3 UI
- 🔄 Real-time data synchronization

---

## 📸 Screenshots

> Add screenshots here after uploading them.

- Home Screen


<img width="745" height="1600" alt="image" src="https://github.com/user-attachments/assets/3fc6f136-51a8-40d6-a963-4cf7b05652c8" />


- Transaction History 


<img width="744" height="1600" alt="image" src="https://github.com/user-attachments/assets/13ebd21c-75fe-4f8d-beae-1c9a98422838" />


- Delete Dialog box


<img width="748" height="1600" alt="image" src="https://github.com/user-attachments/assets/505bd16b-0611-4168-bf7f-ddeec31c1502" />



---

## 🛠️ Technologies Used

- Java
- Android Studio
- Firebase Realtime Database
- Material Design 3
- Android SDK

---

## 📂 Project Structure

```
app/
├── java/
│   └── com.example.net_banking/
│       ├── MainActivity.java
│       ├── TransactionRecord.java
│       └── NotSufficientFund.java
│
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/
│   └── mipmap/
│
└── AndroidManifest.xml
```

---

## 🔥 Firebase Database Structure

```json
account_data
|
|-- balance: 250
|
|-- history
      |
      |-- transaction_id
            |-- type: "Deposit"
            |-- amount: 100
            |-- timestamp: "14:35:12"
```

---

## 🚀 How It Works

### Deposit

1. Enter an amount.
2. Tap **Deposit**.
3. Balance updates instantly.
4. Transaction is saved to Firebase.

### Withdraw

1. Enter an amount.
2. Tap **Withdraw**.
3. The app checks:
   - Sufficient balance
   - Minimum balance (£20) is maintained
4. Transaction is saved if successful.

---

## ⚠️ Business Rules

- Minimum account balance is **£20**.
- Negative or zero amounts are not allowed.
- Withdrawal is blocked if:
  - Insufficient balance
  - Balance falls below the minimum limit

---

## 📜 Transaction History

Every successful transaction stores:

- Transaction Type
- Amount
- Timestamp

Example:

```
Deposit: +£100 (10:42:31)

Withdrawal: -£50 (10:45:17)
```

---

## 🗑 Delete History

The delete icon allows users to:

- Remove all transaction history
- Confirmation dialog before deletion
- Balance remains unchanged

---

## ⚙️ Installation

1. Clone the repository

```bash
git clone https://github.com/your-username/net-banking-app.git
```

2. Open the project in Android Studio.

3. Connect Firebase:
   - Create a Firebase project.
   - Enable Realtime Database.
   - Download `google-services.json`.
   - Place it inside the `app/` folder.

4. Sync Gradle.

5. Run the application.

---

## 📦 Dependencies

```gradle
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.android.material:material'
implementation 'androidx.appcompat:appcompat'
implementation 'androidx.constraintlayout:constraintlayout'
```

---

## 📈 Future Improvements

- User Authentication
- Multiple Bank Accounts
- Money Transfer Between Users
- Transaction Search
- PDF Statement Download
- Dark Mode
- Charts and Analytics
- Fingerprint Authentication
- Currency Formatting
- Offline Data Support

---

## 👨‍💻 Author

**Ronak Gadara**

Computer Engineering Student

---

## ⭐ If you like this project

Give this repository a ⭐ on GitHub!

---

## 📄 License

This project is open source and available under the **MIT License**.
