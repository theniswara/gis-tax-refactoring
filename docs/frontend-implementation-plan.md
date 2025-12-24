# 🎯 Frontend Refactoring Guide: leaflet-geo-FE

> **Untuk siapa?** Frontend Developer  
> **Tujuan:** Merapikan struktur kode `leaflet-geo-FE` mengikuti pola `university-frontend`  
> **Estimasi waktu:** 3-5 hari kerja

---

# 📋 DAFTAR ISI

1. [Perbandingan Struktur](#-perbandingan-struktur)
2. [Persiapan Awal](#-persiapan-awal-wajib)
3. [Phase 1: Pindahkan Guards ke Root](#-phase-1-pindahkan-guards-ke-root)
4. [Phase 2: Pindahkan Models ke Root](#-phase-2-pindahkan-models-ke-root)
5. [Phase 3: Rename helpers → interceptors](#-phase-3-rename-helpers--interceptors)
6. [Phase 4: Buat Barrel Files](#-phase-4-buat-barrel-files)
7. [Phase 5: Rapikan app.module.ts](#-phase-5-rapikan-appmodulets)
8. [Phase 6: Testing & Push](#-phase-6-testing--push)
9. [Troubleshooting](#-troubleshooting)

---

# 📊 PERBANDINGAN STRUKTUR

## Struktur university-frontend (YANG BENAR) ✅

```
src/app/
├── components/          ← Komponen UI
├── guards/              ← Auth guards (DI ROOT!)
│   ├── auth.guard.ts
│   └── admin.guard.ts
├── interceptors/        ← HTTP interceptors (DI ROOT!)
│   └── auth.interceptor.ts
├── models/              ← Interface/Type definitions (DI ROOT!)
│   ├── student.model.ts
│   ├── event.model.ts
│   └── ...
├── services/            ← Services (DI ROOT!)
│   ├── auth.service.ts
│   └── ...
├── admin/               ← Admin feature module
├── student/             ← Student feature module
├── app.module.ts
└── app-routing.module.ts
```

## Struktur leaflet-geo-FE (YANG PERLU DIUBAH) ❌

```
src/app/
├── core/                ← FOLDER TAMBAHAN (tidak ada di reference)
│   ├── guards/          ← Harusnya di root!
│   ├── helpers/         ← Harusnya "interceptors" di root!
│   ├── models/          ← Harusnya di root!
│   ├── services/        ← Harusnya di root!
│   └── factories/
├── shared/
│   ├── services/        ← Duplikat! Harusnya gabung dengan services di root
│   └── ...
├── layouts/
├── pages/
├── store/
├── app.module.ts
└── app-routing.module.ts
```

## Perubahan yang Akan Dilakukan

| Dari (Sekarang) | Ke (Target) |
|-----------------|-------------|
| `core/guards/` | `guards/` (pindah ke root) |
| `core/helpers/` | `interceptors/` (rename + pindah) |
| `core/models/` | `models/` (pindah ke root) |
| `core/services/` + `shared/services/` | `services/` (gabung di root) |

---

# 🚦 PERSIAPAN AWAL (WAJIB!)

## Langkah 1: Buka Terminal di VS Code

1. Klik menu `Terminal` → `New Terminal`

## Langkah 2: Pindah ke Folder Frontend

```bash
cd "/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo-FE"
```

## Langkah 3: Buat Branch Baru

> ⚠️ **WAJIB!** Jangan skip langkah ini!

```bash
git checkout -b refactor/restructure-folders
```

**Output yang diharapkan:**
```
Switched to a new branch 'refactor/restructure-folders'
```

## Langkah 4: Pastikan Branch Sudah Benar

```bash
git branch
```

**Output (harus ada bintang di branch baru):**
```
  main
* refactor/restructure-folders
```

---

# ✅ PHASE 1: PINDAHKAN GUARDS KE ROOT

## Apa yang Dilakukan?
Memindahkan folder `core/guards/` ke `src/app/guards/` (sejajar dengan app.module.ts)

## Langkah 1.1: Buat Folder guards di Root

```bash
mkdir -p src/app/guards
```

## Langkah 1.2: Copy File Guard

```bash
cp src/app/core/guards/auth.guard.ts src/app/guards/
```

## Langkah 1.3: Edit Path Import di File Guard

Buka file `src/app/guards/auth.guard.ts` dan update import path:

**CARI baris seperti ini:**
```typescript
import { ... } from '../services/...';
```

**GANTI menjadi:**
```typescript
import { ... } from '../core/services/...';
```

> 💡 **Catatan:** Karena file sekarang di root, path ke services berubah.

## Langkah 1.4: Update Semua File yang Import Guard

**Cari file yang memakai auth.guard:**
```bash
grep -r "core/guards/auth.guard" src/app --include="*.ts"
```

**Untuk setiap file yang ditemukan, ganti:**
```typescript
// SEBELUM
import { AuthGuard } from './core/guards/auth.guard';
// atau
import { AuthGuard } from '../core/guards/auth.guard';

// SESUDAH
import { AuthGuard } from './guards/auth.guard';
// atau
import { AuthGuard } from '../guards/auth.guard';
```

## Langkah 1.5: Test Build

```bash
ng build --configuration development
```

**Jika berhasil, lanjut. Jika error, baca bagian Troubleshooting.**

## Langkah 1.6: Hapus Folder Lama (Setelah Build Sukses!)

```bash
rm -rf src/app/core/guards
```

## Langkah 1.7: Test Build Lagi

```bash
ng build --configuration development
```

## Langkah 1.8: Commit

```bash
git add .
git commit -m "refactor: move guards to root following university-frontend pattern"
```

---

# ✅ PHASE 2: PINDAHKAN MODELS KE ROOT

## Apa yang Dilakukan?
Memindahkan folder `core/models/` ke `src/app/models/`

## Langkah 2.1: Buat Folder models di Root

```bash
mkdir -p src/app/models
```

## Langkah 2.2: Copy Semua File Model

```bash
cp -r src/app/core/models/* src/app/models/
```

## Langkah 2.3: Update Semua Import Path

**Cari semua file yang import dari core/models:**
```bash
grep -r "core/models" src/app --include="*.ts"
```

**Untuk setiap file, ganti:**
```typescript
// SEBELUM
import { ... } from './core/models/...';
import { ... } from '../core/models/...';
import { ... } from '../../core/models/...';

// SESUDAH
import { ... } from './models/...';
import { ... } from '../models/...';
import { ... } from '../../models/...';
```

> 💡 **Tips VS Code:** Gunakan `Ctrl + Shift + H` untuk Find and Replace di semua file.
> - Find: `core/models`
> - Replace: `models`

## Langkah 2.4: Test Build

```bash
ng build --configuration development
```

## Langkah 2.5: Hapus Folder Lama

```bash
rm -rf src/app/core/models
```

## Langkah 2.6: Test Build Lagi

```bash
ng build --configuration development
```

## Langkah 2.7: Commit

```bash
git add .
git commit -m "refactor: move models to root following university-frontend pattern"
```

---

# ✅ PHASE 3: RENAME helpers → interceptors

## Apa yang Dilakukan?
1. Rename folder `core/helpers` → `interceptors` (di root)
2. Ini mengikuti penamaan di university-frontend

## Langkah 3.1: Buat Folder interceptors di Root

```bash
mkdir -p src/app/interceptors
```

## Langkah 3.2: Copy File Interceptor

```bash
cp src/app/core/helpers/error.interceptor.ts src/app/interceptors/
cp src/app/core/helpers/http.interceptor.ts src/app/interceptors/
```

## Langkah 3.3: Update Import di File Interceptor

Buka setiap file di `src/app/interceptors/` dan update path import jika ada.

## Langkah 3.4: Update Semua File yang Import Interceptor

**Cari file yang import dari core/helpers:**
```bash
grep -r "core/helpers" src/app --include="*.ts"
```

**Ganti semuanya:**
```typescript
// SEBELUM
import { ... } from './core/helpers/error.interceptor';
import { ... } from './core/helpers/http.interceptor';

// SESUDAH
import { ... } from './interceptors/error.interceptor';
import { ... } from './interceptors/http.interceptor';
```

## Langkah 3.5: Test Build

```bash
ng build --configuration development
```

## Langkah 3.6: Hapus Folder Lama

```bash
rm -rf src/app/core/helpers
```

## Langkah 3.7: Commit

```bash
git add .
git commit -m "refactor: rename core/helpers to interceptors at root level"
```

---

# ✅ PHASE 4: BUAT BARREL FILES

## Apa itu Barrel File?
File `index.ts` yang mengexport semua dari folder tersebut.

## Langkah 4.1: Buat Barrel File untuk Guards

Buat file `src/app/guards/index.ts`:

```typescript
/**
 * Guards Barrel File
 * Export semua guards dari sini
 */
export * from './auth.guard';
```

## Langkah 4.2: Buat Barrel File untuk Models

Buat file `src/app/models/index.ts`:

```typescript
/**
 * Models Barrel File
 * Export semua models/interfaces dari sini
 */
export * from './auth.models';
export * from './countdown-event.model';
export * from './master.models';
export * from './pendapatan.model';
// Tambahkan export lain sesuai file yang ada
```

## Langkah 4.3: Buat Barrel File untuk Interceptors

Buat file `src/app/interceptors/index.ts`:

```typescript
/**
 * Interceptors Barrel File
 * Export semua HTTP interceptors dari sini
 */
export * from './error.interceptor';
export * from './http.interceptor';
```

## Langkah 4.4: Buat Barrel File untuk Core Services

Buat file `src/app/core/services/index.ts`:

```typescript
/**
 * Core Services Barrel File
 */
export * from './auth.service';
export * from './bidang.service';
export * from './bprd-api.service';
export * from './csrf.service';
export * from './event.service';
export * from './language.service';
export * from './master.service';
export * from './pendapatan.service';
export * from './remote-config.service';
export * from './rest-api.service';
export * from './translation-sync.service';
```

## Langkah 4.5: Buat Barrel File untuk Shared Services

Buat file `src/app/shared/services/index.ts`:

```typescript
/**
 * Shared Services Barrel File
 */
export * from './event.service';
export * from './listjs.service';
export * from './modal.service';
export * from './pagination.service';
export * from './toast-service';
export * from './utilities.service';
```

## Langkah 4.6: Test Build

```bash
ng build --configuration development
```

## Langkah 4.7: Commit

```bash
git add .
git commit -m "feat: add barrel files (index.ts) for cleaner imports"
```

---

# ✅ PHASE 5: RAPIKAN app.module.ts

## Langkah 5.1: Buka File app.module.ts

Lokasi: `src/app/app.module.ts`

## Langkah 5.2: Ganti Seluruh Isi File

**Hapus semua isi, ganti dengan ini:**

```typescript
import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  HttpClient,
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
  withXsrfConfiguration,
} from '@angular/common/http';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Router } from '@angular/router';

// ============================================
// ROUTING
// ============================================
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// ============================================
// FEATURE MODULES
// ============================================
import { LayoutsModule } from './layouts/layouts.module';
import { PagesModule } from './pages/pages.module';
import { SharedModule } from './shared/shared.module';

// ============================================
// INTERCEPTORS (dari folder interceptors/)
// ============================================
import { HttpInterceptorService } from './interceptors/http.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';

// ============================================
// TRANSLATION / LANGUAGE
// ============================================
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core';

// ============================================
// NGRX STORE (STATE MANAGEMENT)
// ============================================
import { Store, StoreModule } from '@ngrx/store';
import { rootReducer } from './store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { EffectsModule } from '@ngrx/effects';

// ============================================
// THIRD PARTY MODULES
// ============================================
import { NgPipesModule } from 'ngx-pipes';
import { NgxSpinnerModule } from 'ngx-spinner';

// ============================================
// CORE SERVICES & FACTORIES
// ============================================
import { fetchUserInitializer } from './core/factories/fetch-user.factory';
import { RestApiService } from './core/services/rest-api.service';

// ============================================
// ENVIRONMENT
// ============================================
import { environment } from '../environments/environment';

/**
 * Translation Initializer
 * Memuat file terjemahan saat aplikasi dibuka
 */
export function translationInitializer(translate: TranslateService) {
  return () => new Promise<void>((resolve) => {
    translate.setDefaultLang('en');
    translate.use('en').subscribe(() => {
      console.log('Translations loaded!');
      resolve();
    });
  });
}

/**
 * Translation Loader Factory
 */
export function createTranslateLoader(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

@NgModule({
  // ============================================
  // DECLARATIONS
  // ============================================
  declarations: [
    AppComponent
  ],

  // ============================================
  // BOOTSTRAP
  // ============================================
  bootstrap: [AppComponent],

  // ============================================
  // IMPORTS
  // ============================================
  imports: [
    // --- Angular Core ---
    BrowserModule,
    BrowserAnimationsModule,
    
    // --- Routing ---
    AppRoutingModule,
    
    // --- Feature Modules ---
    LayoutsModule,
    PagesModule,
    SharedModule,
    
    // --- Translation ---
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient],
      },
    }),
    
    // --- State Management ---
    StoreModule.forRoot(rootReducer),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production,
    }),
    EffectsModule.forRoot(),
    
    // --- Third Party ---
    NgPipesModule,
    NgxSpinnerModule,
  ],

  // ============================================
  // SCHEMAS
  // ============================================
  schemas: [CUSTOM_ELEMENTS_SCHEMA],

  // ============================================
  // PROVIDERS
  // ============================================
  providers: [
    // --- Pipes ---
    DecimalPipe,
    DatePipe,
    
    // --- Interceptors ---
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true },
    
    // --- Initializers ---
    {
      provide: APP_INITIALIZER,
      useFactory: translationInitializer,
      deps: [TranslateService],
      multi: true,
    },
    {
      provide: APP_INITIALIZER,
      useFactory: fetchUserInitializer,
      deps: [Store, RestApiService, Router],
      multi: true,
    },
    
    // --- HTTP Configuration ---
    provideHttpClient(
      withInterceptorsFromDi(),
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      })
    ),
  ],
})
export class AppModule {}
```

## Langkah 5.3: Test Build

```bash
ng build --configuration development
```

## Langkah 5.4: Commit

```bash
git add .
git commit -m "refactor: organize app.module.ts with section comments"
```

---

# ✅ PHASE 6: TESTING & PUSH

## Langkah 6.1: Jalankan Aplikasi

```bash
npm run start
```

## Langkah 6.2: Test di Browser

Buka: `http://localhost:4200`

## Langkah 6.3: Checklist Testing

- [ ] Halaman login muncul
- [ ] Login berhasil
- [ ] Dashboard muncul
- [ ] Peta (map) bisa dimuat
- [ ] Menu sidebar berfungsi
- [ ] Tidak ada error di console (F12)

## Langkah 6.4: Push ke GitHub

```bash
git push -u origin refactor/restructure-folders
```

## Langkah 6.5: Buat Pull Request

1. Buka: https://github.com/theniswara/gis-tax-refactoring
2. Klik "Compare & pull request"
3. Judul: `Frontend: Restructure folders following university-frontend pattern`
4. Deskripsi:
   ```
   Perubahan:
   - Pindahkan guards ke src/app/guards/
   - Pindahkan models ke src/app/models/
   - Rename helpers → interceptors
   - Tambah barrel files (index.ts)
   - Rapikan app.module.ts dengan komentar section
   ```
5. Klik "Create pull request"

---

# 🔥 TROUBLESHOOTING

## Error: Cannot find module '...'

**Penyebab:** Path import salah setelah pindah folder.

**Solusi:**
1. Baca error message, lihat file mana yang salah
2. Buka file tersebut
3. Cek path import dan perbaiki

## Error: Build gagal setelah edit

**Solusi:** Kembalikan file ke versi sebelumnya:
```bash
git checkout <nama-file>
```

## Error: Semua rusak, mau mulai ulang

```bash
git checkout .
```

## Error: Mau kembali ke main

```bash
git checkout main
```

---

# 📁 STRUKTUR AKHIR (Target)

Setelah semua phase selesai, struktur folder akan menjadi:

```
src/app/
├── guards/              ← ✅ Di root
│   ├── auth.guard.ts
│   └── index.ts
├── interceptors/        ← ✅ Di root (renamed dari helpers)
│   ├── error.interceptor.ts
│   ├── http.interceptor.ts
│   └── index.ts
├── models/              ← ✅ Di root
│   ├── auth.models.ts
│   ├── ...
│   └── index.ts
├── core/
│   ├── factories/       ← Tetap di core (khusus untuk factories)
│   └── services/        ← Tetap di core untuk saat ini
│       └── index.ts
├── shared/
├── layouts/
├── pages/
├── store/
├── app.module.ts        ← ✅ Dengan section comments
└── app-routing.module.ts
```

---

# ✅ CHECKLIST AKHIR

Pastikan semua sudah dilakukan:

- [ ] Branch baru dibuat: `refactor/restructure-folders`
- [ ] Guards dipindahkan ke `src/app/guards/`
- [ ] Models dipindahkan ke `src/app/models/`
- [ ] helpers di-rename menjadi `interceptors/`
- [ ] Barrel files dibuat untuk setiap folder
- [ ] app.module.ts sudah dirapikan
- [ ] `ng build` berhasil
- [ ] Aplikasi berjalan di browser
- [ ] Semua sudah di-push ke GitHub
- [ ] Pull Request sudah dibuat
