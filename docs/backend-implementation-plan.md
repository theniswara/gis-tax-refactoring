# 🎯 Backend Refactoring Guide: leaflet-geo

> **Untuk siapa?** Backend Developer  
> **Tujuan:** Merapikan struktur kode `leaflet-geo` mengikuti pola `university-backend`  
> **Estimasi waktu:** 3-5 hari kerja

---

# 📋 DAFTAR ISI

1. [Perbandingan Struktur](#-perbandingan-struktur)
2. [Persiapan Awal](#-persiapan-awal-wajib)
3. [Phase 1: Pindahkan Dokumentasi](#-phase-1-pindahkan-dokumentasi)
4. [Phase 2: Rename entity → model](#-phase-2-rename-entity--model)
5. [Phase 3: Buat ApiResponse DTO](#-phase-3-buat-apiresponse-dto)
6. [Phase 4: Refactor Controllers](#-phase-4-refactor-controllers)
7. [Phase 5: Tambah Javadoc Comments](#-phase-5-tambah-javadoc-comments)
8. [Phase 6: Testing & Push](#-phase-6-testing--push)
9. [Troubleshooting](#-troubleshooting)

---

# 📊 PERBANDINGAN STRUKTUR

## Struktur university-backend (YANG BENAR) ✅

```
src/main/java/com/psdku/lmj/university_backend/
├── UniversityBackendApplication.java
├── config/              ← Konfigurasi Spring
├── controller/          ← REST Controllers
├── dto/                 ← Data Transfer Objects
│   └── ApiResponse.java ← ✅ Standard response wrapper
├── model/               ← ✅ Entity classes (bukan "entity"!)
├── repository/          ← JPA Repositories
├── security/            ← Security configuration
├── service/             ← Business logic
└── util/                ← Utility classes
```

## Struktur leaflet-geo (YANG PERLU DIUBAH) ❌

```
src/main/java/com/example/leaflet_geo/
├── LeafletGeoApplication.java
├── config/
├── controller/          ← Pakai HashMap untuk response (messy!)
├── dto/                 ← Tidak ada ApiResponse
├── entity/              ← ❌ Harusnya "model" bukan "entity"!
├── repository/
├── service/
└── util/
```

## Perubahan yang Akan Dilakukan

| Dari (Sekarang) | Ke (Target) |
|-----------------|-------------|
| `entity/` | `model/` (rename folder) |
| HashMap di controller | ApiResponse DTO |
| Tidak ada Javadoc | Tambah Javadoc comments |
| Docs di root | Pindah ke `docs/` folder |

---

# 🚦 PERSIAPAN AWAL (WAJIB!)

## Langkah 1: Buka Terminal

Di VS Code atau IDE, buka terminal.

## Langkah 2: Pindah ke Folder Backend

```bash
cd "/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo"
```

## Langkah 3: Buat Branch Baru

> ⚠️ **WAJIB!** Jangan skip langkah ini!

```bash
git checkout -b refactor/backend-cleanup
```

**Output yang diharapkan:**
```
Switched to a new branch 'refactor/backend-cleanup'
```

## Langkah 4: Pastikan Branch Benar

```bash
git branch
```

**Output (harus ada bintang di branch baru):**
```
  main
* refactor/backend-cleanup
```

---

# ✅ PHASE 1: PINDAHKAN DOKUMENTASI

## Apa yang Dilakukan?
Memindahkan file .md ke folder docs/ agar root bersih.

## Langkah 1.1: Buat Folder docs

```bash
mkdir -p docs
```

## Langkah 1.2: Pindahkan File Markdown

```bash
mv DASHBOARD_PENDAPATAN_QUICKSTART.md docs/
mv DATABASE_SETUP.md docs/
mv DAT_OBJEK_PAJAK_API.md docs/
mv MULTIPLE_DATABASE_SETUP.md docs/
mv SIMATDA_TARGET_REALISASI_QUERIES.md docs/
mv WKB_TO_GEOJSON_CONVERSION.md docs/
```

## Langkah 1.3: Verifikasi

```bash
ls docs/
```

**Output yang diharapkan:**
```
DASHBOARD_PENDAPATAN_QUICKSTART.md
DATABASE_SETUP.md
DAT_OBJEK_PAJAK_API.md
MULTIPLE_DATABASE_SETUP.md
SIMATDA_TARGET_REALISASI_QUERIES.md
WKB_TO_GEOJSON_CONVERSION.md
```

## Langkah 1.4: Commit

```bash
git add .
git commit -m "docs: move markdown files to docs folder"
```

---

# ✅ PHASE 2: RENAME entity → model

## Apa yang Dilakukan?
Mengubah nama folder `entity` menjadi `model` sesuai university-backend.

## ⚠️ PERINGATAN: Phase ini mengubah banyak file. Hati-hati!

---

## Langkah 2.1: Rename Folder

```bash
mv src/main/java/com/example/leaflet_geo/entity src/main/java/com/example/leaflet_geo/model
```

## Langkah 2.2: Update Package Declaration di Setiap File

**Buka SETIAP file di folder `model/` dan ubah baris pertama:**

### File yang perlu di-edit:
1. `model/Bidang.java`
2. `model/DatObjekPajak.java`
3. `model/DatSubjekPajak.java`
4. `model/KecamatanWithCount.java`
5. `model/KelurahanWithCount.java`
6. `model/RefKecamatan.java`
7. `model/RefKelurahan.java`

### Cara edit:

**CARI baris ini di setiap file:**
```java
package com.example.leaflet_geo.entity;
```

**GANTI menjadi:**
```java
package com.example.leaflet_geo.model;
```

## Langkah 2.3: Update Semua Import di File Lain

**Gunakan Find and Replace di IDE (Ctrl + Shift + H di VS Code):**

- **Find:** `com.example.leaflet_geo.entity`
- **Replace:** `com.example.leaflet_geo.model`
- Klik **"Replace All"**

**Atau cari manual:**
```bash
grep -r "leaflet_geo.entity" src/main/java --include="*.java"
```

## Langkah 2.4: Test Compile

```bash
./mvnw clean compile
```

### Jika BERHASIL:
```
[INFO] BUILD SUCCESS
```
➡️ Lanjut ke langkah berikutnya.

### Jika GAGAL:
```
[ERROR] ...
```
➡️ Baca error message dan perbaiki import yang salah.

## Langkah 2.5: Commit

```bash
git add .
git commit -m "refactor: rename entity package to model"
```

---

# ✅ PHASE 3: BUAT ApiResponse DTO

## Apa yang Dilakukan?
Membuat class ApiResponse untuk standarisasi response API.

## Langkah 3.1: Buat File ApiResponse.java

Buat file baru di: `src/main/java/com/example/leaflet_geo/dto/ApiResponse.java`

## Langkah 3.2: Copy-Paste Kode Ini

```java
package com.example.leaflet_geo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API Response Wrapper
 * 
 * Gunakan class ini untuk semua response dari controller.
 * Ini memastikan format response konsisten di seluruh API.
 * 
 * Contoh penggunaan:
 * <pre>
 * // Success dengan data
 * return ResponseEntity.ok(ApiResponse.success("Data berhasil diambil", myData));
 * 
 * // Success dengan data dan total count
 * return ResponseEntity.ok(ApiResponse.success("Data berhasil diambil", myList, totalCount));
 * 
 * // Error
 * return ResponseEntity.badRequest().body(ApiResponse.error("Data tidak ditemukan"));
 * </pre>
 * 
 * @param <T> Tipe data yang dikembalikan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * Menandakan apakah request berhasil atau tidak.
     * true = berhasil, false = gagal
     */
    private boolean success;
    
    /**
     * Pesan yang menjelaskan hasil operasi.
     * Contoh: "Data berhasil diambil", "User tidak ditemukan"
     */
    private String message;
    
    /**
     * Data yang dikembalikan (bisa berupa object, list, dll).
     * Null jika error.
     */
    private T data;
    
    /**
     * Total jumlah data (untuk response yang dipaginasi).
     * Optional - bisa null jika tidak diperlukan.
     */
    private Long totalCount;
    
    /**
     * Membuat response sukses dengan data.
     * 
     * @param message Pesan sukses
     * @param data Data yang dikembalikan
     * @return ApiResponse dengan success=true
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }
    
    /**
     * Membuat response sukses dengan data dan total count.
     * Gunakan untuk response yang dipaginasi.
     * 
     * @param message Pesan sukses
     * @param data Data yang dikembalikan
     * @param totalCount Total jumlah data
     * @return ApiResponse dengan success=true
     */
    public static <T> ApiResponse<T> success(String message, T data, Long totalCount) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .totalCount(totalCount)
            .build();
    }
    
    /**
     * Membuat response error.
     * 
     * @param message Pesan error
     * @return ApiResponse dengan success=false
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .build();
    }
}
```

## Langkah 3.3: Test Compile

```bash
./mvnw clean compile
```

## Langkah 3.4: Commit

```bash
git add .
git commit -m "feat: add ApiResponse DTO for standardized API responses"
```

---

# ✅ PHASE 4: REFACTOR CONTROLLERS

## Apa yang Dilakukan?
Mengganti HashMap dengan ApiResponse di setiap controller.

## ⚠️ KERJAKAN SATU CONTROLLER PADA SATU WAKTU!

---

## Langkah 4.1: Mulai dengan RefKecamatanController

Buka file: `src/main/java/com/example/leaflet_geo/controller/RefKecamatanController.java`

## Langkah 4.2: Tambah Import ApiResponse

Tambahkan di bagian import:
```java
import com.example.leaflet_geo.dto.ApiResponse;
```

## Langkah 4.3: Refactor Satu Method

### SEBELUM (Kode lama - ~20 baris per endpoint):
```java
@GetMapping
public ResponseEntity<Map<String, Object>> getAllKecamatan() {
    try {
        List<RefKecamatan> kecamatanList = refKecamatanRepository.findAll();
        long totalCount = refKecamatanRepository.count();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Data kecamatan berhasil diambil");
        response.put("totalCount", totalCount);
        response.put("data", kecamatanList);
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Gagal mengambil data kecamatan: " + e.getMessage());
        response.put("data", null);
        
        return ResponseEntity.internalServerError().body(response);
    }
}
```

### SESUDAH (Kode baru - ~8 baris per endpoint):
```java
@GetMapping
public ResponseEntity<ApiResponse<List<RefKecamatan>>> getAllKecamatan() {
    try {
        List<RefKecamatan> kecamatanList = refKecamatanRepository.findAll();
        long totalCount = refKecamatanRepository.count();
        
        return ResponseEntity.ok(
            ApiResponse.success("Data kecamatan berhasil diambil", kecamatanList, totalCount)
        );
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(
            ApiResponse.error("Gagal mengambil data kecamatan: " + e.getMessage())
        );
    }
}
```

## Langkah 4.4: Test Compile Setelah Setiap Method

```bash
./mvnw clean compile
```

## Langkah 4.5: Lanjutkan ke Method Lain

Ulangi langkah 4.3-4.4 untuk setiap method di controller.

## Langkah 4.6: Commit Setelah Satu Controller Selesai

```bash
git add .
git commit -m "refactor: use ApiResponse in RefKecamatanController"
```

## Langkah 4.7: Lanjut ke Controller Berikutnya

Ulangi untuk controller lain dengan urutan:

1. [ ] `RefKecamatanController.java` ✅ (sudah selesai)
2. [ ] `RefKelurahanController.java`
3. [ ] `BidangController.java`
4. [ ] `DatObjekPajakController.java`
5. [ ] `DatSubjekPajakController.java`
6. [ ] `PendapatanController.java`
7. [ ] `SimatdaController.java`
8. [ ] `SismiopController.java`
9. [ ] `BphtbController.java`
10. [ ] `BprdProxyController.java`
11. [ ] `EpasirController.java`
12. [ ] `DatabaseTestController.java`

> 💡 **Tips:** Tidak perlu selesai semua dalam satu hari. Kerjakan 2-3 controller per hari.

---

# ✅ PHASE 5: TAMBAH JAVADOC COMMENTS

## Apa yang Dilakukan?
Menambah dokumentasi ke setiap class dan method publik.

## Langkah 5.1: Template Javadoc untuk Controller

```java
/**
 * Controller untuk mengelola data Kecamatan.
 * 
 * Endpoint yang tersedia:
 * - GET /api/ref-kecamatan - Ambil semua kecamatan
 * - GET /api/ref-kecamatan/{id} - Ambil kecamatan by ID
 * - POST /api/ref-kecamatan - Buat kecamatan baru
 * 
 * @author [Nama Kamu]
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/ref-kecamatan")
public class RefKecamatanController {
    // ...
}
```

## Langkah 5.2: Template Javadoc untuk Method

```java
/**
 * Mengambil semua data kecamatan.
 * 
 * @return ResponseEntity berisi list kecamatan dan total count
 */
@GetMapping
public ResponseEntity<ApiResponse<List<RefKecamatan>>> getAllKecamatan() {
    // ...
}
```

## Langkah 5.3: Commit

```bash
git add .
git commit -m "docs: add Javadoc comments to controllers"
```

---

# ✅ PHASE 6: TESTING & PUSH

## Langkah 6.1: Compile Project

```bash
./mvnw clean compile
```

## Langkah 6.2: Jalankan Aplikasi

```bash
./mvnw spring-boot:run
```

## Langkah 6.3: Test Endpoint dengan Postman/Browser

Buka: `http://localhost:8080/api/ref-kecamatan`

**Response yang diharapkan:**
```json
{
    "success": true,
    "message": "Data kecamatan berhasil diambil",
    "data": [...],
    "totalCount": 10
}
```

## Langkah 6.4: Checklist Testing

- [ ] Semua endpoint mengembalikan format ApiResponse
- [ ] Tidak ada error di console
- [ ] Data ditampilkan dengan benar

## Langkah 6.5: Push ke GitHub

```bash
git push -u origin refactor/backend-cleanup
```

## Langkah 6.6: Buat Pull Request

1. Buka: https://github.com/theniswara/gis-tax-refactoring
2. Klik "Compare & pull request"
3. Judul: `Backend: Restructure folders and standardize API responses`
4. Deskripsi:
   ```
   Perubahan:
   - Pindahkan dokumentasi ke docs/
   - Rename entity → model
   - Tambah ApiResponse DTO
   - Refactor semua controller menggunakan ApiResponse
   - Tambah Javadoc comments
   ```
5. Klik "Create pull request"

---

# 🔥 TROUBLESHOOTING

## Error: Cannot find symbol 'ApiResponse'

**Penyebab:** Import belum ditambahkan.

**Solusi:** Tambahkan di bagian import:
```java
import com.example.leaflet_geo.dto.ApiResponse;
```

## Error: Package does not exist 'entity'

**Penyebab:** Ada file yang masih import dari entity.

**Solusi:** Cari dan ganti semua import:
```bash
grep -r "leaflet_geo.entity" src/main/java --include="*.java"
```

## Error: BUILD FAILURE

**Solusi umum:**
1. Baca error message dengan teliti
2. Cek file dan baris yang disebutkan
3. Perbaiki error tersebut
4. Compile ulang

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

Setelah semua phase selesai:

```
src/main/java/com/example/leaflet_geo/
├── LeafletGeoApplication.java
├── config/
│   ├── CorsConfig.java
│   ├── DatabaseConfig.java
│   └── ...
├── controller/          ← ✅ Semua pakai ApiResponse
│   ├── RefKecamatanController.java
│   └── ...
├── dto/
│   ├── ApiResponse.java  ← ✅ Baru
│   └── ...
├── model/               ← ✅ Renamed dari entity
│   ├── Bidang.java
│   ├── RefKecamatan.java
│   └── ...
├── repository/
├── service/
└── util/
```

---

# ✅ CHECKLIST AKHIR

Pastikan semua sudah dilakukan:

- [ ] Branch baru dibuat: `refactor/backend-cleanup`
- [ ] Dokumentasi dipindah ke `docs/`
- [ ] Folder `entity/` di-rename menjadi `model/`
- [ ] Semua import diperbarui
- [ ] `ApiResponse.java` sudah dibuat
- [ ] Semua controller menggunakan ApiResponse
- [ ] Javadoc comments ditambahkan
- [ ] `./mvnw clean compile` berhasil
- [ ] Endpoint bisa ditest dan response benar
- [ ] Semua sudah di-push ke GitHub
- [ ] Pull Request sudah dibuat
