# 🎯 Frontend Refactoring Guide (leaflet-geo-FE)

> **Siapa yang harus baca ini?** Frontend developer  
> **Apa yang akan kamu kerjakan?** Merapikan kode `leaflet-geo-FE` mengikuti pola dari `university-frontend`  
> **Berapa lama?** Sekitar 2-3 hari kerja

---

# 📋 DAFTAR ISI

1. [Persiapan Awal](#-persiapan-awal-wajib)
2. [Phase 1: Membuat Barrel File](#-phase-1-membuat-barrel-file)
3. [Phase 2: Menambah Komentar di app.module.ts](#-phase-2-menambah-komentar-di-appmodulets)
4. [Phase 3: Testing](#-phase-3-testing)
5. [Phase 4: Push ke GitHub](#-phase-4-push-ke-github)
6. [Jika Ada Error](#-jika-ada-error-baca-ini)

---

# 🚦 PERSIAPAN AWAL (WAJIB!)

## Langkah 1: Buka Terminal

**Di VS Code:**
1. Klik menu `Terminal` di atas
2. Klik `New Terminal`
3. Terminal akan muncul di bawah

## Langkah 2: Pindah ke Folder Frontend

**Copy-paste perintah ini ke terminal, lalu tekan Enter:**
```bash
cd "/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo-FE"
```

**Cara mengecek sudah benar:**
```bash
pwd
```

**Hasilnya harus:**
```
/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo-FE
```

## Langkah 3: Buat Branch Baru

> ⚠️ **PENTING:** JANGAN SKIP langkah ini! Ini untuk melindungi kode asli kalau ada error.

**Copy-paste perintah ini:**
```bash
git checkout -b refactor/frontend-cleanup
```

**Hasilnya:**
```
Switched to a new branch 'refactor/frontend-cleanup'
```

## Langkah 4: Pastikan Branch Sudah Benar

```bash
git branch
```

**Hasilnya harus seperti ini (ada tanda bintang di branch baru):**
```
  main
* refactor/frontend-cleanup
```

## Langkah 5: Install Dependencies (Jika Belum)

```bash
npm install
```

Tunggu sampai selesai (bisa 2-5 menit).

---

# ✅ PHASE 1: MEMBUAT BARREL FILE

## Apa itu Barrel File?

Barrel file adalah file `index.ts` yang mengumpulkan semua export dalam satu tempat.

**Contoh SEBELUM (ribet):**
```typescript
import { AuthService } from '../../core/services/auth.service';
import { RestApiService } from '../../core/services/rest-api.service';
import { BidangService } from '../../core/services/bidang.service';
```

**Contoh SESUDAH (rapi):**
```typescript
import { AuthService, RestApiService, BidangService } from '../../core/services';
```

---

## Langkah 1.1: Buka Folder Services di VS Code

1. Di panel kiri VS Code, cari folder ini:
   ```
   src/app/core/services/
   ```
2. Klik kanan pada folder `services`
3. Pilih `New File`
4. Ketik nama file: `index.ts`
5. Tekan Enter

## Langkah 1.2: Copy-Paste Kode Ini ke File index.ts

```typescript
/**
 * Barrel file untuk core services
 * File ini mengexport semua service dari folder core/services
 * 
 * Cara pakai:
 * import { AuthService, RestApiService } from '../../core/services';
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

## Langkah 1.3: Simpan File

Tekan `Ctrl + S` untuk menyimpan.

## Langkah 1.4: Test Apakah Berhasil

**Di terminal, jalankan:**
```bash
ng build --configuration development
```

**Tunggu prosesnya selesai (1-3 menit).**

### Jika BERHASIL, hasilnya seperti ini:
```
✔ Browser application bundle generation complete.
✔ Copying assets complete.
✔ Index html generation complete.

Build at: 2024-xx-xx - Chunk Files ...
```

➡️ **Lanjut ke Langkah 1.5**

### Jika GAGAL, hasilnya ada tulisan ERROR:
```
Error: ...
```

➡️ **Baca bagian [JIKA ADA ERROR](#-jika-ada-error-baca-ini)**

## Langkah 1.5: Simpan Perubahan ke Git

**Jalankan perintah ini satu per satu:**

```bash
git add .
```

```bash
git commit -m "feat: add barrel file for core services"
```

**Hasilnya:**
```
[refactor/frontend-cleanup xxxxxxx] feat: add barrel file for core services
 1 file changed, xx insertions(+)
 create mode 100644 src/app/core/services/index.ts
```

---

## Langkah 1.6: Buat Barrel File untuk Shared Services

1. Buka folder `src/app/shared/services/`
2. Klik kanan → `New File`
3. Nama file: `index.ts`
4. Copy-paste kode ini:

```typescript
/**
 * Barrel file untuk shared services
 */

export * from './event.service';
export * from './listjs.service';
export * from './modal.service';
export * from './pagination.service';
export * from './toast-service';
export * from './utilities.service';
```

5. Simpan (`Ctrl + S`)

## Langkah 1.7: Test Lagi

```bash
ng build --configuration development
```

## Langkah 1.8: Commit

```bash
git add .
```

```bash
git commit -m "feat: add barrel file for shared services"
```

---

# ✅ PHASE 2: MENAMBAH KOMENTAR DI app.module.ts

## Apa yang Akan Kita Lakukan?

Menambah komentar pemisah agar kode lebih mudah dibaca.

## Langkah 2.1: Buka File app.module.ts

1. Di VS Code, buka file:
   ```
   src/app/app.module.ts
   ```

## Langkah 2.2: Ganti Isi File

**HAPUS semua isi file, lalu GANTI dengan kode di bawah ini:**

```typescript
import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgPipesModule } from 'ngx-pipes';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutsModule } from './layouts/layouts.module';
import { PagesModule } from './pages/pages.module';
import {
  HttpClient,
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
  withXsrfConfiguration,
} from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { environment } from '../environments/environment';

// ============================================
// INTERCEPTORS
// ============================================
import { HttpInterceptorService } from './core/helpers/http.interceptor';
import { ErrorInterceptor } from './core/helpers/error.interceptor';

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
import { NgxSpinnerModule } from 'ngx-spinner';

// ============================================
// CORE SERVICES & FACTORIES
// ============================================
import { fetchUserInitializer } from './core/factories/fetch-user.factory'
import { Router } from '@angular/router';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RestApiService } from './core/services/rest-api.service';
import { SharedModule } from './shared/shared.module';

/**
 * Translation Initializer
 * Fungsi ini memuat file terjemahan saat aplikasi pertama kali dibuka
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
 * Fungsi ini membuat loader untuk file terjemahan
 */
export function createTranslateLoader(http: HttpClient): any {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

@NgModule({
  // ============================================
  // DECLARATIONS
  // Komponen yang didaftarkan di module ini
  // ============================================
  declarations: [
    AppComponent
  ],

  // ============================================
  // BOOTSTRAP
  // Komponen utama yang dijalankan pertama kali
  // ============================================
  bootstrap: [AppComponent],

  // ============================================
  // IMPORTS
  // Module-module yang dibutuhkan
  // ============================================
  imports: [
    // --- Angular Core Modules ---
    BrowserModule,
    BrowserAnimationsModule,
    
    // --- Routing ---
    AppRoutingModule,
    
    // --- Feature Modules ---
    LayoutsModule,
    PagesModule,
    SharedModule,
    
    // --- Translation Module ---
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient],
      },
    }),
    
    // --- State Management (NgRx) ---
    StoreModule.forRoot(rootReducer),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production,
    }),
    EffectsModule.forRoot(),
    
    // --- Third Party Modules ---
    NgPipesModule,
    NgxSpinnerModule,
  ],

  // ============================================
  // SCHEMAS
  // ============================================
  schemas: [CUSTOM_ELEMENTS_SCHEMA],

  // ============================================
  // PROVIDERS
  // Services dan konfigurasi yang disediakan
  // ============================================
  providers: [
    // --- Pipes ---
    DecimalPipe,
    DatePipe,
    
    // --- HTTP Interceptors ---
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true },
    
    // --- App Initializers ---
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
    
    // --- HTTP Client Configuration ---
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

## Langkah 2.3: Simpan File

Tekan `Ctrl + S`

## Langkah 2.4: Test

```bash
ng build --configuration development
```

**Jika BERHASIL, lanjut ke langkah berikutnya.**

## Langkah 2.5: Commit

```bash
git add .
```

```bash
git commit -m "style: add section comments to app.module.ts"
```

---

# ✅ PHASE 3: TESTING

## Langkah 3.1: Jalankan Aplikasi

```bash
npm run start
```

**Tunggu sampai muncul:**
```
** Angular Live Development Server is listening on localhost:4200 **
```

## Langkah 3.2: Buka Browser

Buka browser (Chrome/Firefox), ketik di address bar:
```
http://localhost:4200
```

## Langkah 3.3: Checklist Testing

Cek satu per satu:

- [ ] Halaman login muncul
- [ ] Bisa login dengan akun test
- [ ] Setelah login, dashboard muncul
- [ ] Peta (map) bisa dimuat
- [ ] Menu di sidebar bisa diklik
- [ ] Tidak ada error merah di console (tekan F12 untuk buka console)

---

# ✅ PHASE 4: PUSH KE GITHUB

## Langkah 4.1: Stop Server

Di terminal, tekan `Ctrl + C` untuk stop server.

## Langkah 4.2: Push ke GitHub

```bash
git push -u origin refactor/frontend-cleanup
```

**Hasilnya:**
```
 * [new branch]      refactor/frontend-cleanup -> refactor/frontend-cleanup
Branch 'refactor/frontend-cleanup' set up to track remote branch ...
```

## Langkah 4.3: Buat Pull Request di GitHub

1. Buka browser
2. Pergi ke: https://github.com/theniswara/gis-tax-refactoring
3. Akan muncul banner kuning: "refactor/frontend-cleanup had recent pushes"
4. Klik tombol hijau **"Compare & pull request"**
5. Isi:
   - **Title:** `Frontend: Add barrel files and organize app.module.ts`
   - **Description:** 
     ```
     Perubahan:
     - Menambah barrel file (index.ts) di core/services
     - Menambah barrel file (index.ts) di shared/services  
     - Menambah komentar section di app.module.ts
     ```
6. Klik **"Create pull request"**
7. Tunggu review dari team

---

# 🔥 JIKA ADA ERROR (BACA INI!)

## Error 1: "Cannot find module..."

**Contoh error:**
```
Error: Cannot find module './auth.service'
```

**Penyebab:** Nama file salah di barrel file.

**Solusi:**
1. Cek nama file di folder tersebut
2. Pastikan nama di `index.ts` sama persis dengan nama file (termasuk huruf besar/kecil)

---

## Error 2: Build gagal setelah edit app.module.ts

**Solusi:** Kembalikan file ke versi sebelumnya:

```bash
git checkout src/app/app.module.ts
```

Lalu coba lagi dengan lebih hati-hati.

---

## Error 3: Semua rusak, mau mulai ulang

**Kembalikan SEMUA file ke kondisi sebelum diubah:**

```bash
git checkout .
```

---

## Error 4: Mau pindah ke branch main (abaikan semua perubahan)

```bash
git checkout main
```

---

# ❓ FAQ (Pertanyaan yang Sering Muncul)

**Q: Apa bedanya `git add .` dan `git commit`?**
- `git add .` = Tandai file yang mau disimpan
- `git commit` = Simpan perubahan dengan pesan

**Q: Kenapa harus buat branch baru?**
- Supaya kalau ada error, kode asli di branch `main` tidak rusak

**Q: Saya sudah selesai, apa selanjutnya?**
- Tunggu Pull Request diapprove
- Setelah diapprove, kode akan digabung ke branch `main`

**Q: Terminal saya tidak mau jalan, gimana?**
- Tutup VS Code
- Buka lagi
- Buka terminal baru

---

# ✅ CHECKLIST AKHIR

Sebelum selesai, pastikan semua ini sudah dilakukan:

- [ ] Branch baru sudah dibuat
- [ ] Barrel file `core/services/index.ts` sudah dibuat
- [ ] Barrel file `shared/services/index.ts` sudah dibuat
- [ ] `app.module.ts` sudah ditambah komentar
- [ ] `ng build` berhasil tanpa error
- [ ] Aplikasi bisa jalan di browser
- [ ] Semua commit sudah dipush
- [ ] Pull Request sudah dibuat di GitHub
