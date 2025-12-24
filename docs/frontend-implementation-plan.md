# 🎯 Frontend Complete Restructuring Guide: leaflet-geo-FE

> **Tujuan:** Mengubah struktur folder leaflet-geo-FE agar SAMA PERSIS dengan university-frontend  
> **Estimasi waktu:** 1-2 minggu  
> **Tingkat kesulitan:** ⚠️ TINGGI

---

# ⚠️ PENTING: PENDEKATAN "MOVE ALL AT ONCE"

**JANGAN pindahkan folder satu per satu!** Ini akan menyebabkan error import.

**Pendekatan yang benar:**
1. ✅ Buat SEMUA folder baru dulu
2. ✅ Copy SEMUA file ke lokasi baru
3. ✅ Update SEMUA import sekaligus
4. ✅ Test build
5. ✅ Hapus folder lama

---

# 📊 PERBANDINGAN STRUKTUR

## SEBELUM (Sekarang) ❌
```
src/app/
├── core/
│   ├── factories/
│   ├── guards/
│   ├── helpers/        ← Harusnya "interceptors"
│   ├── models/
│   └── services/
├── shared/
│   └── services/       ← Duplikat, harus digabung
├── layouts/
├── pages/
└── ...
```

## SESUDAH (Target) ✅
```
src/app/
├── factories/          ← Dipindah dari core/
├── guards/             ← Dipindah dari core/
├── interceptors/       ← Renamed dari core/helpers/
├── models/             ← Dipindah dari core/
├── services/           ← Gabungan core/ + shared/
├── components/
│   └── layouts/        ← Dipindah dari layouts/
├── features/           ← Renamed dari pages/
└── ...
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

## Langkah 3: Verifikasi Branch
```bash
git branch
```

---

# ✅ PHASE 1: BUAT SEMUA FOLDER BARU

```bash
# Buat folder-folder baru di root
mkdir -p src/app/factories
mkdir -p src/app/guards
mkdir -p src/app/interceptors
mkdir -p src/app/models
mkdir -p src/app/services
mkdir -p src/app/components/layouts
mkdir -p src/app/features
```

**Jangan hapus folder lama dulu!**

---

# ✅ PHASE 2: COPY SEMUA FILE KE LOKASI BARU

```bash
# Copy factories
cp src/app/core/factories/*.ts src/app/factories/

# Copy guards
cp src/app/core/guards/*.ts src/app/guards/

# Copy helpers → interceptors (rename)
cp src/app/core/helpers/*.ts src/app/interceptors/

# Copy models (termasuk subfolder)
cp -r src/app/core/models/* src/app/models/

# Copy services dari core
cp src/app/core/services/*.ts src/app/services/

# Copy services dari shared (gabung ke services/)
cp src/app/shared/services/*.ts src/app/services/

# Copy layouts ke components/layouts
cp -r src/app/layouts/* src/app/components/layouts/

# Copy pages ke features (rename)
cp -r src/app/pages/* src/app/features/
```

---

# ✅ PHASE 3: UPDATE SEMUA IMPORT (PALING PENTING!)

## 3.1: Update Import di File-File yang Dipindah

**Untuk setiap file yang dipindah, update path import-nya.**

### Contoh: `src/app/guards/auth.guard.ts`

**SEBELUM (path lama):**
```typescript
import { AuthenticationService } from '../services/auth.service';
import { RestApiService } from '../services/rest-api.service';
import { RemoteConfigService } from '../services/remote-config.service';
import { setUser } from 'src/app/store/auth/auth.action';
import { setMenu } from 'src/app/store/menu/menu.action';
import { MENU } from 'src/app/layouts/sidebar/menu';
import { selectCurrentUser } from 'src/app/store/auth/auth.selector';
```

**SESUDAH (path baru):**
```typescript
import { AuthenticationService } from '../services/auth.service';  // ✅ Tetap sama
import { RestApiService } from '../services/rest-api.service';      // ✅ Tetap sama
import { RemoteConfigService } from '../services/remote-config.service'; // ✅ Tetap sama
import { setUser } from 'src/app/store/auth/auth.action';          // ✅ Tetap sama
import { setMenu } from 'src/app/store/menu/menu.action';          // ✅ Tetap sama
import { MENU } from 'src/app/components/layouts/sidebar/menu';    // ⚠️ UBAH!
import { selectCurrentUser } from 'src/app/store/auth/auth.selector'; // ✅ Tetap sama
```

---

## 3.2: Update Import di SELURUH Project

Gunakan **Find and Replace** di VS Code (Ctrl+Shift+H):

### Replace 1: core/guards → guards
- **Find:** `from './core/guards/`
- **Replace:** `from './guards/`
- Klik "Replace All"

- **Find:** `from '../core/guards/`
- **Replace:** `from '../guards/`
- Klik "Replace All"

- **Find:** `from '../../core/guards/`
- **Replace:** `from '../../guards/`
- Klik "Replace All"

### Replace 2: core/helpers → interceptors
- **Find:** `from './core/helpers/`
- **Replace:** `from './interceptors/`

- **Find:** `from '../core/helpers/`
- **Replace:** `from '../interceptors/`

### Replace 3: core/models → models
- **Find:** `from './core/models/`
- **Replace:** `from './models/`

- **Find:** `from '../core/models/`
- **Replace:** `from '../models/`

- **Find:** `from '../../core/models/`
- **Replace:** `from '../../models/`

### Replace 4: core/services → services
- **Find:** `from './core/services/`
- **Replace:** `from './services/`

- **Find:** `from '../core/services/`
- **Replace:** `from '../services/`

- **Find:** `from '../../core/services/`
- **Replace:** `from '../../services/`

### Replace 5: shared/services → services
- **Find:** `from './shared/services/`
- **Replace:** `from './services/`

- **Find:** `from '../shared/services/`
- **Replace:** `from '../services/`

### Replace 6: core/factories → factories
- **Find:** `from './core/factories/`
- **Replace:** `from './factories/`

### Replace 7: layouts → components/layouts
- **Find:** `from './layouts/`
- **Replace:** `from './components/layouts/`

- **Find:** `from '../layouts/`
- **Replace:** `from '../components/layouts/`

### Replace 8: pages → features
- **Find:** `from './pages/`
- **Replace:** `from './features/`

- **Find:** `from '../pages/`
- **Replace:** `from '../features/`

---

## 3.3: Update Module Imports

### Update `app.module.ts`:

```typescript
// SEBELUM
import { LayoutsModule } from './layouts/layouts.module';
import { PagesModule } from './pages/pages.module';
import { HttpInterceptorService } from './core/helpers/http.interceptor';
import { ErrorInterceptor } from './core/helpers/error.interceptor';
import { fetchUserInitializer } from './core/factories/fetch-user.factory';
import { RestApiService } from './core/services/rest-api.service';

// SESUDAH
import { LayoutsModule } from './components/layouts/layouts.module';
import { FeaturesModule } from './features/features.module';  // Renamed!
import { HttpInterceptorService } from './interceptors/http.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { fetchUserInitializer } from './factories/fetch-user.factory';
import { RestApiService } from './services/rest-api.service';
```

### Update `app-routing.module.ts`:

Ganti semua path `pages` → `features`.

### Rename `pages.module.ts` → `features.module.ts`:

```bash
mv src/app/features/pages.module.ts src/app/features/features.module.ts
```

Update isi file:
```typescript
// SEBELUM
export class PagesModule { }

// SESUDAH
export class FeaturesModule { }
```

---

# ✅ PHASE 4: BUAT BARREL FILES

## 4.1: `src/app/guards/index.ts`
```typescript
export * from './auth.guard';
```

## 4.2: `src/app/interceptors/index.ts`
```typescript
export * from './error.interceptor';
export * from './http.interceptor';
```

## 4.3: `src/app/models/index.ts`
```typescript
export * from './auth.models';
export * from './countdown-event.model';
export * from './master.models';
export * from './pendapatan.model';
```

## 4.4: `src/app/services/index.ts`
```typescript
// From core/services
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

// From shared/services
export * from './listjs.service';
export * from './modal.service';
export * from './pagination.service';
export * from './toast-service';
export * from './utilities.service';
```

## 4.5: `src/app/factories/index.ts`
```typescript
export * from './fetch-user.factory';
```

---

# ✅ PHASE 5: TEST BUILD

```bash
ng build --configuration development
```

## Jika ADA ERROR:
1. Baca error message
2. Cari file yang error
3. Perbaiki import path-nya
4. Build lagi

## Jika SUKSES:
Lanjut ke Phase 6.

---

# ✅ PHASE 6: HAPUS FOLDER LAMA

**⚠️ Hanya hapus SETELAH build sukses!**

```bash
rm -rf src/app/core
rm -rf src/app/layouts
rm -rf src/app/pages
rm -rf src/app/shared/services
```

---

# ✅ PHASE 7: TEST FINAL

```bash
# Build lagi
ng build --configuration development

# Run app
npm run start
```

Buka browser: http://localhost:4200

---

# ✅ PHASE 8: COMMIT & PUSH

```bash
git add .
git commit -m "refactor: restructure folders following university-frontend pattern"
git push -u origin refactor/complete-restructure
```

---

# 📁 STRUKTUR AKHIR

```
src/app/
├── account/             ✅
├── components/          ✅
│   └── layouts/         ✅ (dipindah dari layouts/)
├── extraspages/         ✅
├── factories/           ✅ (dipindah dari core/)
├── features/            ✅ (renamed dari pages/)
├── guards/              ✅ (dipindah dari core/)
├── interceptors/        ✅ (renamed dari core/helpers/)
├── models/              ✅ (dipindah dari core/)
├── services/            ✅ (gabungan core/ + shared/)
├── shared/              ✅ (tanpa services/)
├── store/               ✅
├── app.module.ts        ✅
└── app-routing.module.ts ✅
```

---

# 🔥 TROUBLESHOOTING

## Error: Cannot find module '...'
Path import salah. Cek dan perbaiki path.

## Error: PagesModule not found
Rename `PagesModule` → `FeaturesModule` di:
- `features.module.ts`
- `app.module.ts`
- `app-routing.module.ts`

## Error: MENU not found
Update import:
```typescript
// SEBELUM
import { MENU } from 'src/app/layouts/sidebar/menu';
// SESUDAH
import { MENU } from 'src/app/components/layouts/sidebar/menu';
```

## Mau mulai ulang dari awal
```bash
git checkout .
```

---

# ✅ CHECKLIST FINAL

- [ ] Semua folder baru dibuat
- [ ] Semua file di-copy ke lokasi baru
- [ ] Semua import di-update
- [ ] pages.module.ts di-rename ke features.module.ts
- [ ] PagesModule di-rename ke FeaturesModule
- [ ] Barrel files (index.ts) dibuat
- [ ] `ng build` sukses
- [ ] Folder lama dihapus
- [ ] `ng build` sukses lagi
- [ ] App berjalan di browser
- [ ] Push ke GitHub
