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

<table align="center">
  <tr>
    <td align="center">
      <img src="Login Page.jpg" alt="Login Page" width="250"/><br/>
      <b>Login Page</b>
    </td>
    <td align="center">
      <img src="Home Page.jpg" alt="Home Page" width="250"/><br/>
      <b>Home Page</b>
    </td>
    <td align="center">
      <img src="Create Account.jpg" alt="Create Account" width="250"/><br/>
      <b>Create Account</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="Add Task.jpg" alt="Add Task" width="250"/><br/>
      <b>Add Task</b>
    </td>
    <td align="center">
      <img src="Test Add.jpg" alt="Test Add" width="250"/><br/>
      <b>Test Add</b>
    </td>
    <td></td>
  </tr>
</table>

---

## 💰 Google AdMob Integration

| Ad Type            | Where it Appears                        | Purpose                              |
|--------------------|-----------------------------------------|--------------------------------------|
| 📍 **Banner Ads**  | Bottom of key screens                   | Non-intrusive, steady monetization   |
| 🚀 **Interstitial**| After adding/completing important tasks | Boosted engagement + revenue         |
| ⚡ **Optimized**   | Smart placement                         | Ensures smooth user experience       |

```kotlin
// Example AdMob Implementation (Banner)
val adRequest = AdRequest.Builder().build()
binding.adView.loadAd(adRequest)
