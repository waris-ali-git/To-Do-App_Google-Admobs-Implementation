# ✅ My Tasks - _A Personal Task Manager App_

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1DA1F2?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Room-Database-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/MVVM-Architecture-blueviolet?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/AdMob-Integrated-red?style=for-the-badge&logo=google"/>
</p>

---

## ✨ Overview
**My Tasks** is a simple yet powerful personal task manager app that helps users create, manage, and organize tasks effortlessly.  
Built with **Kotlin, MVVM Architecture, and Room Database**, the app ensures smooth performance and a clean UI.  
Monetization is handled through **Google AdMob Ads** for a production-ready mobile app experience.  

---

## 🎯 Features

| Feature                | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| ➕ **Add Task**         | Create tasks with **title, description, date, and priority**                |
| 📋 **View Tasks**       | Display tasks in a **RecyclerView / LazyColumn**                            |
| ✏️ **Edit & Update**    | Modify existing tasks anytime                                               |
| ❌ **Delete Tasks**     | Remove tasks with **confirmation prompt**                                   |
| ✅ **Task Status**      | Mark tasks as **completed / incomplete**                                    |
| 🎨 **Priority Levels**  | Highlight tasks with **icons or colors**                                    |
| 💾 **Offline Storage**  | Store data locally with **Room Database**                                   |
| 🔄 **Live Updates**     | Reactive changes with **LiveData / State**                                  |
| 🧭 **Navigation**       | Smooth flow between **Task List → Add/Edit → Details**                      |
| 💰 **AdMob Ads**        | Integrated **Banner & Interstitial Ads** for monetization                   |

---

## 📸 App Screenshots
<p align="center">
  <img src="Login Page.jpg" width="30%" style="border-radius:15px; margin:10px;"/>
  <img src="Home Page.jpg" width="30%" style="border-radius:15px; margin:10px;"/>
  <img src="Create Account.jpg" width="30%" style="border-radius:15px; margin:10px;"/>
</p>

<p align="center">
  <img src="Add Task.jpg" width="40%" style="border-radius:15px; margin:10px;"/>
  <img src="Test Add.jpg" width="40%" style="border-radius:15px; margin:10px;"/>
</p>

---

## 💰 Google AdMob Integration

| Ad Type            | Where it Appears                     | Purpose                              |
|--------------------|--------------------------------------|--------------------------------------|
| 📍 **Banner Ads**  | Bottom of key screens                | Non-intrusive, steady monetization   |
| 🚀 **Interstitial**| After adding/completing important tasks | Boosted engagement + revenue        |
| ⚡ **Optimized**   | Smart placement                      | Ensures smooth user experience       |

```kotlin
// Example AdMob Implementation (Banner)
val adRequest = AdRequest.Builder().build()
binding.adView.loadAd(adRequest)
