# 🎯 Frontend Complete Restructuring Guide: leaflet-geo-FE

> **Tujuan:** Mengubah struktur folder leaflet-geo-FE agar SAMA PERSIS dengan university-frontend  
> **Estimasi waktu:** 1-2 minggu  
> **Tingkat kesulitan:** ⚠️ TINGGI - Banyak file yang harus dipindah dan diubah

---

# 📊 PERBANDINGAN STRUKTUR LENGKAP

## university-frontend (TARGET) ✅
```
src/app/
├── admin/               ← Feature module untuk admin
│   ├── components/
│   ├── guards/
│   ├── models/
│   └── services/
├── components/          ← Komponen UI publik (footer, header, home, dll)
│   ├── about/
│   ├── admissions/
│   ├── footer/
│   ├── header/
│   ├── home/
│   └── ...
├── guards/              ← Auth guards di ROOT
│   ├── auth.guard.ts
│   └── admin.guard.ts
├── interceptors/        ← HTTP interceptors di ROOT
│   └── auth.interceptor.ts
├── models/              ← Interface/types di ROOT
│   ├── student.model.ts
│   ├── event.model.ts
│   └── ...
├── services/            ← Services di ROOT
│   ├── auth.service.ts
│   └── ...
├── student/             ← Feature module untuk student
├── app.module.ts
└── app-routing.module.ts
```

## leaflet-geo-FE (SEKARANG) ❌
```
src/app/
├── account/             → KEEP sebagai feature module
├── core/                → HAPUS folder ini, pindahkan isinya
│   ├── factories/       → Pindah ke root
│   ├── guards/          → Pindah ke root
│   ├── helpers/         → Rename jadi interceptors/, pindah ke root
│   ├── models/          → Pindah ke root
│   └── services/        → Pindah ke root
├── extraspages/         → KEEP atau rename jadi pages/
├── layouts/             → Pindah ke components/ atau shared/
├── pages/               → Rename jadi features/ atau pecah per feature
│   ├── bidang/
│   ├── dashboard-pajak/
│   ├── master-data/
│   └── setting/
├── shared/              → KEEP
├── shared-modules/      → Gabung ke shared/
├── store/               → KEEP (ini NgRx, tidak ada di university-frontend)
├── app.module.ts
└── app-routing.module.ts
```

---

# 🎯 TARGET STRUKTUR AKHIR

```
src/app/
├── account/             ← Feature module (login, register)
├── components/          ← BARU: Komponen UI reusable
│   ├── layouts/         ← Dipindah dari layouts/
│   │   ├── footer/
│   │   ├── header/
│   │   ├── sidebar/
│   │   └── topbar/
│   └── shared/          ← Komponen shared lainnya
├── guards/              ← DIPINDAH dari core/guards/
│   ├── auth.guard.ts
│   └── index.ts
├── interceptors/        ← RENAMED dari core/helpers/
│   ├── error.interceptor.ts
│   ├── http.interceptor.ts
│   └── index.ts
├── models/              ← DIPINDAH dari core/models/
│   └── *.model.ts
├── services/            ← DIGABUNG dari core/services/ + shared/services/
│   └── *.service.ts
├── features/            ← RENAMED dari pages/
│   ├── bidang/
│   ├── dashboard-pajak/
│   ├── dashboard-pendapatan/
│   ├── master-data/
│   └── setting/
├── store/               ← KEEP (NgRx)
├── app.module.ts
└── app-routing.module.ts
```

---

# 🚦 PERSIAPAN AWAL

## Langkah 1: Buka Terminal
```bash
cd "/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo-FE"
```

## Langkah 2: Buat Branch Baru
```bash
git checkout -b refactor/complete-restructure
```

## Langkah 3: Pastikan Branch Benar
```bash
git branch
```

---

# ✅ PHASE 1: PINDAHKAN GUARDS KE ROOT

## Langkah 1.1: Buat Folder guards/ di Root
```bash
mkdir -p src/app/guards
```

## Langkah 1.2: Copy File Guard
```bash
cp src/app/core/guards/*.ts src/app/guards/
```

## Langkah 1.3: Buat Barrel File
Buat file `src/app/guards/index.ts`:
```typescript
export * from './auth.guard';
```

## Langkah 1.4: Update Semua Import
Cari dan ganti semua import:
```
CARI:    from './core/guards/
GANTI:   from './guards/

CARI:    from '../core/guards/
GANTI:   from '../guards/

CARI:    from '../../core/guards/
GANTI:   from '../../guards/
```

## Langkah 1.5: Test Build
```bash
ng build --configuration development
```

## Langkah 1.6: Hapus Folder Lama
```bash
rm -rf src/app/core/guards
```

## Langkah 1.7: Commit
```bash
git add .
git commit -m "refactor: move guards to root level"
```

---

# ✅ PHASE 2: PINDAHKAN MODELS KE ROOT

## Langkah 2.1: Buat Folder models/ di Root
```bash
mkdir -p src/app/models
```

## Langkah 2.2: Copy Semua File Model
```bash
cp -r src/app/core/models/* src/app/models/
```

## Langkah 2.3: Buat Barrel File
Buat file `src/app/models/index.ts`:
```typescript
export * from './auth.models';
export * from './countdown-event.model';
export * from './master.models';
export * from './pendapatan.model';
// Tambahkan file lain yang ada
```

## Langkah 2.4: Update Semua Import
```
CARI:    from './core/models/
GANTI:   from './models/

CARI:    from '../core/models/
GANTI:   from '../models/

CARI:    from '../../core/models/
GANTI:   from '../../models/
```

## Langkah 2.5: Test Build
```bash
ng build --configuration development
```

## Langkah 2.6: Hapus Folder Lama
```bash
rm -rf src/app/core/models
```

## Langkah 2.7: Commit
```bash
git add .
git commit -m "refactor: move models to root level"
```

---

# ✅ PHASE 3: RENAME helpers → interceptors

## Langkah 3.1: Buat Folder interceptors/
```bash
mkdir -p src/app/interceptors
```

## Langkah 3.2: Copy File
```bash
cp src/app/core/helpers/*.ts src/app/interceptors/
```

## Langkah 3.3: Buat Barrel File
Buat file `src/app/interceptors/index.ts`:
```typescript
export * from './error.interceptor';
export * from './http.interceptor';
```

## Langkah 3.4: Update Semua Import
```
CARI:    from './core/helpers/
GANTI:   from './interceptors/

CARI:    from '../core/helpers/
GANTI:   from '../interceptors/
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
git commit -m "refactor: rename helpers to interceptors at root level"
```

---

# ✅ PHASE 4: GABUNG SERVICES KE ROOT

## Langkah 4.1: Buat Folder services/ di Root
```bash
mkdir -p src/app/services
```

## Langkah 4.2: Copy Services dari core/services/
```bash
cp src/app/core/services/*.ts src/app/services/
```

## Langkah 4.3: Copy Services dari shared/services/
```bash
cp src/app/shared/services/*.ts src/app/services/
```

## Langkah 4.4: Buat Barrel File
Buat file `src/app/services/index.ts`:
```typescript
// Core Services
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

// Shared Services (dipindahkan)
export * from './listjs.service';
export * from './modal.service';
export * from './pagination.service';
export * from './toast-service';
export * from './utilities.service';
```

## Langkah 4.5: Update Semua Import
```
CARI:    from './core/services/
GANTI:   from './services/

CARI:    from '../core/services/
GANTI:   from '../services/

CARI:    from './shared/services/
GANTI:   from './services/

CARI:    from '../shared/services/
GANTI:   from '../services/
```

## Langkah 4.6: Test Build
```bash
ng build --configuration development
```

## Langkah 4.7: Hapus Folder Lama
```bash
rm -rf src/app/core/services
rm -rf src/app/shared/services
```

## Langkah 4.8: Commit
```bash
git add .
git commit -m "refactor: consolidate all services to root level"
```

---

# ✅ PHASE 5: PINDAHKAN FACTORIES KE ROOT

## Langkah 5.1: Buat Folder factories/
```bash
mkdir -p src/app/factories
```

## Langkah 5.2: Copy File
```bash
cp src/app/core/factories/*.ts src/app/factories/
```

## Langkah 5.3: Update Import
```
CARI:    from './core/factories/
GANTI:   from './factories/
```

## Langkah 5.4: Test Build
```bash
ng build --configuration development
```

## Langkah 5.5: Hapus Folder Lama
```bash
rm -rf src/app/core/factories
```

## Langkah 5.6: Hapus Folder core/ (Sekarang Kosong)
```bash
rm -rf src/app/core
```

## Langkah 5.7: Commit
```bash
git add .
git commit -m "refactor: move factories to root and remove empty core folder"
```

---

# ✅ PHASE 6: ORGANISASI LAYOUTS → COMPONENTS

## Langkah 6.1: Buat Folder components/
```bash
mkdir -p src/app/components
```

## Langkah 6.2: Pindahkan Layouts ke Components
```bash
mv src/app/layouts src/app/components/layouts
```

## Langkah 6.3: Update LayoutsModule Path
Edit file `src/app/components/layouts/layouts.module.ts` dan update import paths jika perlu.

## Langkah 6.4: Update Import di app.module.ts
```typescript
// SEBELUM
import { LayoutsModule } from './layouts/layouts.module';

// SESUDAH
import { LayoutsModule } from './components/layouts/layouts.module';
```

## Langkah 6.5: Test Build
```bash
ng build --configuration development
```

## Langkah 6.6: Commit
```bash
git add .
git commit -m "refactor: move layouts into components folder"
```

---

# ✅ PHASE 7: RENAME pages → features

## Langkah 7.1: Rename Folder
```bash
mv src/app/pages src/app/features
```

## Langkah 7.2: Update Semua Import
```
CARI:    from './pages/
GANTI:   from './features/

CARI:    from '../pages/
GANTI:   from '../features/
```

## Langkah 7.3: Update Routing
Edit `src/app/app-routing.module.ts` dan file routing lainnya.

## Langkah 7.4: Test Build
```bash
ng build --configuration development
```

## Langkah 7.5: Commit
```bash
git add .
git commit -m "refactor: rename pages to features"
```

---

# ✅ PHASE 8: CLEANUP & BARREL FILES

## Langkah 8.1: Gabung shared-modules ke shared
```bash
cp -r src/app/shared-modules/* src/app/shared/
rm -rf src/app/shared-modules
```

## Langkah 8.2: Buat Barrel Files untuk Semua Folder

**src/app/guards/index.ts**
**src/app/interceptors/index.ts**
**src/app/models/index.ts**
**src/app/services/index.ts**
**src/app/factories/index.ts**

## Langkah 8.3: Test Build
```bash
ng build --configuration development
```

## Langkah 8.4: Commit
```bash
git add .
git commit -m "refactor: cleanup and add barrel files"
```

---

# ✅ PHASE 9: UPDATE app.module.ts

## Langkah 9.1: Update Import Paths

```typescript
import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClient, provideHttpClient, withInterceptorsFromDi, withXsrfConfiguration } from '@angular/common/http';
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
import { LayoutsModule } from './components/layouts/layouts.module';  // UPDATED PATH
import { FeaturesModule } from './features/features.module';          // RENAMED dari PagesModule
import { SharedModule } from './shared/shared.module';

// ============================================
// INTERCEPTORS (dari interceptors/)
// ============================================
import { HttpInterceptorService, ErrorInterceptor } from './interceptors';

// ============================================
// SERVICES (dari services/)
// ============================================
import { RestApiService } from './services';

// ============================================
// FACTORIES (dari factories/)
// ============================================
import { fetchUserInitializer } from './factories/fetch-user.factory';

// ... rest of the module
```

---

# ✅ PHASE 10: FINAL TESTING

## Langkah 10.1: Full Build
```bash
ng build --configuration development
```

## Langkah 10.2: Run App
```bash
npm run start
```

## Langkah 10.3: Test di Browser
- [ ] Login works
- [ ] Dashboard loads
- [ ] Map displays correctly
- [ ] All pages accessible
- [ ] No console errors

## Langkah 10.4: Push
```bash
git push -u origin refactor/complete-restructure
```

## Langkah 10.5: Create Pull Request

---

# 📁 STRUKTUR AKHIR

Setelah semua phase selesai:

```
src/app/
├── account/             ✅
├── components/          ✅ BARU
│   └── layouts/         ✅ DIPINDAH
├── factories/           ✅ DIPINDAH dari core/
├── features/            ✅ RENAMED dari pages/
│   ├── bidang/
│   ├── dashboard-pajak/
│   └── ...
├── guards/              ✅ DIPINDAH dari core/
├── interceptors/        ✅ RENAMED dari core/helpers/
├── models/              ✅ DIPINDAH dari core/
├── services/            ✅ GABUNGAN core/ + shared/
├── shared/              ✅ 
├── store/               ✅
├── app.module.ts        ✅ UPDATED
└── app-routing.module.ts ✅ UPDATED
```

---

# 🔥 TROUBLESHOOTING

## Error: Cannot find module
Cek path import, pastikan sudah diupdate sesuai lokasi baru.

## Error: Circular dependency
Cek barrel files, mungkin ada circular import.

## Build gagal total
```bash
git checkout .
```
Mulai ulang dari phase terakhir yang berhasil.

---

# ✅ CHECKLIST AKHIR

- [ ] guards/ di root level
- [ ] interceptors/ di root level (bukan helpers)
- [ ] models/ di root level
- [ ] services/ di root level (gabungan)
- [ ] factories/ di root level
- [ ] components/layouts/ (layouts dipindah)
- [ ] features/ (renamed dari pages/)
- [ ] core/ folder sudah dihapus
- [ ] Semua barrel files ada
- [ ] app.module.ts sudah diupdate
- [ ] Build sukses
- [ ] App berjalan normal
