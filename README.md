# Lovely Pets

Hello world! This is an online store mobile application created in Java. This is the final project within the Samsung Innovation Campus education. At the end of the course, it won 2nd place. In the app you can: authenticate, add items to cart, make a purchase [without actually debiting the account], search for items by name using filters, see details of purchase histories and much more.   

## Table of Contents

- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Roadmap](#roadmap)
- [Authors](#authors)

## Tech Stack

- Java
- XML
- Material 2
- Gradle
- Firebase: realtime database, authentication 

## Installation

Follow these steps to install and set up the application:

```bash
# Download Android Studio if you don't have it.
link for it: https://developer.android.com/studio?hl=en

# Clone the repository
git clone https://github.com/Khatep/LovelyPets.git

# Open project in Android Studio
1. Start Android Studio.
2. Click “Open an existing Android Studio project”.
3. Specify the path to the cloned repository (LovelyPets).

# Synchronize and build the project
1. Wait for the Gradle synchronization process to complete (this may take some time).
2. Make sure all project dependencies are loaded.

```

## Usage

Follow these steps to run the application:

```bash
# Run project
Click the “Run” button (green triangle) to build and run the application on the emulator or a connected Android device.
```

## Project Structure

```bash
LovelyPets/
├── app/
│   ├── src/
│   │   ├── main
│   │   │   ├── java/com/example/lovelypets/
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── CategoryAdapter.java
│   │   │   │   │   ├── OrderAdapter.java
│   │   │   │   │   ├── PartnersImageAdapter.java
│   │   │   │   │   ├── ProductAdapterForCartFragment.java
│   │   │   │   │   └── ProductAdapterForCategoryDetailFragment.java  
│   │   │   │   ├── authentications/
│   │   │   │   │   ├── InputDataForUserActivity.java
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── OnUserExistsListener.java
│   │   │   │   │   ├── PhoneNumberTextWather.java
│   │   │   │   │   ├── SignupActivity.java
│   │   │   │   │   └── UserExistenceChecker.java  
│   │   │   │   ├── dtos/
│   │   │   │   │   └── FirebaseAuthUserDTO.java
│   │   │   │   ├── emailconfirmations/
│   │   │   │   │   ├── EmailConfirmActivity.java
│   │   │   │   │   └── GenericTextWatcher.java
│   │   │   │   ├── emailsenders/
│   │   │   │   │   ├── confirmcodegenerate/
│   │   │   │   │   │   ├── SendCodeToEmailTask.java  
│   │   │   │   │   │   └── VerificationCodeGeneratedListener.java
│   │   │   │   │   ├── receiptgenerate/
│   │   │   │   │   │   ├── ReceiptGeneratedListener.java
│   │   │   │   │   │   └── SendReceiptToEmail.java
│   │   │   │   ├── enums/
│   │   │   │   │   ├── AuthProvider.java
│   │   │   │   │   ├── Gender.java
│   │   │   │   │   ├── OrderStatus.java
│   │   │   │   │   └── ProductType.java
│   │   │   │   ├── eventlisteners/
│   │   │   │   │   ├── OnBackPressedListener.java
│   │   │   │   │   ├── OnDeleteIconClickListener.java
│   │   │   │   │   ├── OnOrderClickListener.java
│   │   │   │   │   └── OnProductClickListener.java
│   │   │   │   ├── exitalertdialog/
│   │   │   │   │   └── ExitDialogActivity.java
│   │   │   │   ├── fragments/
│   │   │   │   │   ├── aboutus/
│   │   │   │   │   │   └── AboutUsFragments.java
│   │   │   │   │   ├── cart/
│   │   │   │   │   │   ├── CartFragment.java
│   │   │   │   │   │   └── CartProductListProvider.java
│   │   │   │   │   ├── category/
│   │   │   │   │   │   ├── CategoryDetailFragment.java
│   │   │   │   │   │   └── CaregoryFragment.java
│   │   │   │   │   ├── home/
│   │   │   │   │   │   ├── HomeFragment.java
│   │   │   │   │   │   └── SpacesItemDecoration.java
│   │   │   │   │   ├── orderhistory/
│   │   │   │   │   │   ├── OrderHistoryDetailFragment.java
│   │   │   │   │   │   └── OrderHistoryFragment.java
│   │   │   │   │   ├── productdetail/
│   │   │   │   │   │   └── ProductDetailFragment.java
│   │   │   │   │   ├── profile/
│   │   │   │   │   │   └── ProfileFragment.java
│   │   │   │   │   ├── search/
│   │   │   │   │   │   └── SearchFragment.java
│   │   │   │   ├── models/
│   │   │   │   │   ├── Category.java
│   │   │   │   │   ├── Order.java
│   │   │   │   │   ├── Product.java
│   │   │   │   │   └── User.java
│   │   │   │   ├── passwordreset/
│   │   │   │   │   └── PasswordResetActivity.java
│   │   │   │   ├── payment/
│   │   │   │   │   ├── CreditCardTextWatcher.java
│   │   │   │   │   └── PaymentDialogActivity.java
│   │   │   │   ├── viewholders/
│   │   │   │   │   ├── CategoryViewHolder.java
│   │   │   │   │   ├── OrderViewHolder.java
│   │   │   │   │   ├── ProductViewHolderForCartFragment.java
│   │   │   │   │   └── ProductViewHolderForCategoryDetailFragment.java 
│   │   │   │   ├── viewmodels/
│   │   │   │   │   └── SearchViewModel.java
│   │   │   │   ├── welcomepage/
│   │   │   │   │   └── WelcomeActivity.java
│   │   │   │   └── LovelyPetsApplicationsActivity.java
│   │   │   ├── res/...
│   │   │   ├── AndroidManifest.xml
│   │   │   └── ic_launcher-playstore.png
└── README.md
```


## Roadmap

Here are some features and improvements planned for future releases:

Do you have ideas or suggestions for other features? Feel free to [open an issue](https://github.com/your-username/your-project/issues) or submit a pull request! or send me email nurgali.khatep@gmail.com 


## Authors

#### Khatep Nurgali
#### Kalymova Zhansaya 

