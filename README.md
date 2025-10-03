# Android Assignment - Chirag Talsaniya

An Android app that fetches and displays **used car listings** from the  endpoint and supports **offline caching**. Built with **Kotlin**, **MVVM + Clean Architecture**, **Koin DI**, **Jetpack Compose + Navigation**, and demonstrates **both Coroutines and RxJava** for async work.

---
## Screens
![Android App](https://i.imgur.com/4MgLmXR.jpeg)
---

## Architecture

**MVVM + Clean Architecture** with clear separation:

- **Presentation** (Android):
  - Jetpack **Compose** UI, **Navigation**, **ViewModel** (state flows), strings, theming.
- **Domain** (Pure Kotlin):
  - Entities (e.g., `Vehicle`), UseCases (`ObserveVehiclesUseCase`, `RefreshVehiclesUseCase`, `ObserveVehicleUseCase`), Repository interface.
- **Data** (Android):
  - **Retrofit/Moshi** remote API
  - **Room** cache (offline first)
  - **Repository implementation** that orchestrates networking + caching
  - Mappers (DTO → Entity → Domain)

**DI**: **Koin** modules wire everything together at app start.

---

## Module & package layout

```
app/
  src/main/java/com/carfax/assignment/
    CarfaxApp.kt
    MainActivity.kt
    di/PresentationModule.kt
    presentation/
      listings/
        ListingsViewModel.kt
        ListingsScreen.kt
      details/
        VehicleDetailViewModel.kt
        VehicleDetailScreen.kt
      ui/
        nav/NavGraph.kt
        common/CallDealerButton.kt
        util/Formatters.kt
    ui/theme/*

domain/
  src/main/java/com/carfax/assignment/domain/
    model/Vehicle.kt
    repository/ListingsRepository.kt
    usecase/ObserveVehiclesUseCase.kt
    usecase/ObserveVehicleUseCase.kt
    usecase/RefreshVehiclesUseCase.kt

data/
  src/main/java/com/carfax/assignment/data/
    remote/CarfaxApi.kt
    remote/dto/ListingDtos.kt
    local/CarfaxDatabase.kt
    local/ListingDao.kt
    local/ListingEntity.kt
    mapper/ListingMappers.kt
    repository/ListingsRepositoryImpl.kt
    di/DataModule.kt
```

---

## Tech stack

- **Language**: Kotlin
- **UI**: Jetpack **Compose** (Material 3), **Navigation**
- **Architecture**: **MVVM**, **Clean Architecture**
- **DI**: **Koin**
- **Async**: **Kotlin Coroutines** + **RxJava3** (both demonstrated)
- **Networking**: Retrofit + Moshi
- **Persistence**: Room (Flow)
- **Images**: Coil (with placeholder)
- **Testing**: JUnit4, Truth, Turbine, MockWebServer, Room testing, Compose UI test
- **Build**: Gradle (AGP), Proguard/R8 (release)

---

## Tests (TDD)

Unit and instrumentation tests validate the app from DTO mapping up to ViewModel state.

### What’s covered
- **Domain**
  - `ObserveVehiclesUseCaseTest`: use case emits repo items (Turbine).
  - `RefreshVehiclesUseCaseTest`: routes to **coroutines** or **Rx** path.
- **Data**
  - `ListingMappersTest`: DTO→Entity mapping (ID resolution, image picks).
  - `ListingsRepositoryImplTest`: MockWebServer verifies **refresh()** and **refreshWithRx()** insert cached entities.
  - `ListingDaoTest` (androidTest): in-memory Room upsert + observe.
- **Presentation**
  - `ListingsViewModelTest`: state items, loading flags, error path on failed refresh.
  - `VehicleDetailViewModelTest`: `load(id)` updates selected item.
- **UI (Compose)**
  - `CallDealerButtonTest` (androidTest): clicking **CALL DEALER** shows confirmation dialog.

### Run tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires emulator or device)
./gradlew connectedAndroidTest
```

> If you introduced an Rx bridge in test fakes, include  
> `org.jetbrains.kotlinx:kotlinx-coroutines-rx3` for `Flow.asFlowable()`.

---

## Submission

1. Push the repo to **a private GitHub repository**.
3. Include this **README.md** at the root.


**Thank you for reviewing!**
