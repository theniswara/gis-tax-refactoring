# 🎯 Backend Complete Restructuring Guide: leaflet-geo

> **Tujuan:** Mengubah struktur folder leaflet-geo agar SAMA PERSIS dengan university-backend  
> **Estimasi waktu:** 1-2 minggu  
> **Tingkat kesulitan:** ⚠️ TINGGI

---

# 📊 PERBANDINGAN STRUKTUR LENGKAP

## university-backend (TARGET) ✅
```
src/main/java/com/psdku/lmj/university_backend/
├── UniversityBackendApplication.java
├── config/                      ← Konfigurasi
│   ├── CorsConfig.java
│   ├── SecurityConfig.java      ← ✅ Security configuration
│   └── SecurityHeadersFilter.java
├── controller/                  ← REST Controllers
│   ├── AdminController.java
│   ├── AuthController.java
│   ├── StudentController.java
│   └── ...
├── dto/                         ← Data Transfer Objects
│   ├── ApiResponse.java         ← ✅ Standard response wrapper
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── ...
├── model/                       ← ✅ Entity classes (bukan "entity"!)
│   ├── Student.java
│   ├── Admin.java
│   └── ...
├── repository/                  ← JPA Repositories
├── security/                    ← ✅ Security components (TIDAK ADA DI leaflet-geo!)
│   ├── AccountLockoutService.java
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenService.java
│   └── RateLimitingService.java
├── service/                     ← Business logic
└── util/                        ← Utilities
    ├── AdminInitializer.java
    ├── PasswordHashGenerator.java
    └── ...
```

## leaflet-geo (SEKARANG) ❌
```
src/main/java/com/example/leaflet_geo/
├── LeafletGeoApplication.java
├── config/                      ← ✅ Ada tapi kurang lengkap
│   ├── CorsConfig.java
│   ├── DatabaseConfig.java
│   └── RestTemplateConfig.java
├── controller/                  ← ❌ Pakai HashMap, bukan ApiResponse
├── dto/                         ← ❌ Tidak ada ApiResponse
├── entity/                      ← ❌ Harusnya "model"!
├── repository/
├── service/
├── util/                        ← ❌ Kurang lengkap
└── (TIDAK ADA security/)        ← ❌ Optional, tapi bagus untuk ditambah nanti
```

---

# 🎯 TARGET STRUKTUR AKHIR

```
src/main/java/com/example/leaflet_geo/
├── LeafletGeoApplication.java
├── config/                      ✅
│   ├── CorsConfig.java
│   ├── DatabaseConfig.java
│   └── ...
├── controller/                  ✅ Menggunakan ApiResponse
├── dto/                         ✅
│   ├── ApiResponse.java         ← BARU
│   └── ... (existing DTOs)
├── model/                       ✅ RENAMED dari entity/
│   ├── Bidang.java
│   ├── RefKecamatan.java
│   └── ...
├── repository/                  ✅
├── service/                     ✅
└── util/                        ✅
```

> **Note:** Folder `security/` tidak wajib untuk sekarang karena leaflet-geo mungkin belum butuh JWT/authentication. Bisa ditambah nanti.

---

# 🚦 PERSIAPAN AWAL

## Langkah 1: Buka Terminal
```bash
cd "/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo"
```

## Langkah 2: Buat Branch Baru
```bash
git checkout -b refactor/backend-restructure
```

## Langkah 3: Verifikasi
```bash
git branch
```
Harus ada bintang (*) di branch `refactor/backend-restructure`.

---

# ✅ PHASE 1: PINDAHKAN DOKUMENTASI

## Langkah 1.1: Buat Folder docs/
```bash
mkdir -p docs
```

## Langkah 1.2: Pindahkan File .md
```bash
mv DASHBOARD_PENDAPATAN_QUICKSTART.md docs/
mv DATABASE_SETUP.md docs/
mv DAT_OBJEK_PAJAK_API.md docs/
mv MULTIPLE_DATABASE_SETUP.md docs/
mv SIMATDA_TARGET_REALISASI_QUERIES.md docs/
mv WKB_TO_GEOJSON_CONVERSION.md docs/
```

## Langkah 1.3: Commit
```bash
git add .
git commit -m "docs: move markdown files to docs folder"
```

---

# ✅ PHASE 2: RENAME entity → model

## Langkah 2.1: Rename Folder
```bash
mv src/main/java/com/example/leaflet_geo/entity src/main/java/com/example/leaflet_geo/model
```

## Langkah 2.2: Update Package Declaration

**Edit SETIAP file di folder `model/`:**

**File yang perlu di-edit:**
- `Bidang.java`
- `DatObjekPajak.java`
- `DatSubjekPajak.java`
- `KecamatanWithCount.java`
- `KelurahanWithCount.java`
- `RefKecamatan.java`
- `RefKelurahan.java`

**Ganti baris pertama:**
```java
// SEBELUM
package com.example.leaflet_geo.entity;

// SESUDAH
package com.example.leaflet_geo.model;
```

## Langkah 2.3: Update Semua Import

**Gunakan Find & Replace (Ctrl+Shift+H):**
- **Find:** `com.example.leaflet_geo.entity`
- **Replace:** `com.example.leaflet_geo.model`
- Klik **Replace All**

## Langkah 2.4: Test Compile
```bash
./mvnw clean compile
```

**Jika BUILD SUCCESS**, lanjut. Jika error, perbaiki import yang terlewat.

## Langkah 2.5: Commit
```bash
git add .
git commit -m "refactor: rename entity package to model"
```

---

# ✅ PHASE 3: BUAT ApiResponse DTO

## Langkah 3.1: Buat File

Buat file: `src/main/java/com/example/leaflet_geo/dto/ApiResponse.java`

## Langkah 3.2: Copy Kode Ini

```java
package com.example.leaflet_geo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API Response Wrapper
 * 
 * Gunakan class ini untuk SEMUA response dari controller.
 * Ini memastikan format response konsisten di seluruh API.
 * 
 * @param <T> Tipe data yang dikembalikan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /** true = berhasil, false = gagal */
    private boolean success;
    
    /** Pesan yang menjelaskan hasil operasi */
    private String message;
    
    /** Data yang dikembalikan */
    private T data;
    
    /** Total jumlah data (untuk pagination) */
    private Long totalCount;
    
    /** Membuat response sukses dengan data */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }
    
    /** Membuat response sukses dengan data dan count */
    public static <T> ApiResponse<T> success(String message, T data, Long totalCount) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .totalCount(totalCount)
            .build();
    }
    
    /** Membuat response error */
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
git commit -m "feat: add ApiResponse DTO for standardized responses"
```

---

# ✅ PHASE 4: REFACTOR CONTROLLERS (Satu per Satu!)

## ⚠️ ATURAN: Kerjakan SATU controller, test, commit. Baru lanjut ke controller berikutnya!

---

## Langkah 4.1: Buka Controller Pertama

File: `src/main/java/com/example/leaflet_geo/controller/RefKecamatanController.java`

## Langkah 4.2: Tambah Import

```java
import com.example.leaflet_geo.dto.ApiResponse;
```

## Langkah 4.3: Ubah Method getAllKecamatan()

### SEBELUM (~20 baris):
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

### SESUDAH (~8 baris):
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

## Langkah 4.4: Ubah Method Lainnya dengan Pola yang Sama

Untuk setiap method di controller, ganti HashMap dengan ApiResponse.

## Langkah 4.5: Test Compile
```bash
./mvnw clean compile
```

## Langkah 4.6: Commit
```bash
git add .
git commit -m "refactor: use ApiResponse in RefKecamatanController"
```

## Langkah 4.7: Lanjut ke Controller Berikutnya

Ulangi langkah 4.1-4.6 untuk setiap controller:

- [ ] `RefKecamatanController.java` ✅
- [ ] `RefKelurahanController.java`
- [ ] `BidangController.java`
- [ ] `DatObjekPajakController.java`
- [ ] `DatSubjekPajakController.java`
- [ ] `PendapatanController.java`
- [ ] `SimatdaController.java`
- [ ] `SismiopController.java`
- [ ] `BphtbController.java`
- [ ] `BprdProxyController.java`
- [ ] `EpasirController.java`
- [ ] `DatabaseTestController.java`

---

# ✅ PHASE 5: HAPUS IMPORT YANG TIDAK TERPAKAI

Setelah semua controller di-refactor, hapus import HashMap yang tidak terpakai:

**Cari dan hapus baris ini di semua controller:**
```java
import java.util.HashMap;
import java.util.Map;
```

(Jika masih dipakai untuk keperluan lain, jangan hapus)

---

# ✅ PHASE 6: FINAL TESTING

## Langkah 6.1: Compile
```bash
./mvnw clean compile
```

## Langkah 6.2: Run
```bash
./mvnw spring-boot:run
```

## Langkah 6.3: Test Endpoint

Buka browser atau Postman:
```
GET http://localhost:8080/api/ref-kecamatan
```

**Response yang diharapkan:**
```json
{
  "success": true,
  "message": "Data kecamatan berhasil diambil",
  "data": [...],
  "totalCount": 10
}
```

## Langkah 6.4: Checklist
- [ ] Semua endpoint return ApiResponse format
- [ ] Tidak ada error di console
- [ ] `./mvnw clean compile` sukses

---

# ✅ PHASE 7: PUSH & CREATE PR

## Langkah 7.1: Push
```bash
git push -u origin refactor/backend-restructure
```

## Langkah 7.2: Create Pull Request
1. Buka: https://github.com/theniswara/gis-tax-refactoring
2. Klik "Compare & pull request"
3. Judul: `Backend: Rename entity→model and standardize API responses`
4. Klik "Create pull request"

---

# 📁 STRUKTUR AKHIR

```
src/main/java/com/example/leaflet_geo/
├── LeafletGeoApplication.java
├── config/              ✅
├── controller/          ✅ Semua pakai ApiResponse
├── dto/                 ✅
│   ├── ApiResponse.java ← BARU
│   └── ... (DTOs lainnya)
├── model/               ✅ RENAMED dari entity/
├── repository/          ✅
├── service/             ✅
└── util/                ✅
```

---

# 🔥 TROUBLESHOOTING

## Error: Cannot find symbol ApiResponse
**Solusi:** Tambahkan import:
```java  
import com.example.leaflet_geo.dto.ApiResponse;
```

## Error: Package does not exist entity
**Solusi:** Cari file yang masih import dari entity:
```bash
grep -r "leaflet_geo.entity" src/main/java --include="*.java"
```
Ganti semua ke `leaflet_geo.model`.

## Build gagal total
```bash
git checkout .
```
Mulai ulang dari phase terakhir yang berhasil.

---

# ✅ CHECKLIST FINAL

- [ ] Dokumentasi dipindah ke `docs/`
- [ ] `entity/` di-rename ke `model/`
- [ ] Semua package declaration diupdate
- [ ] Semua import diupdate
- [ ] `ApiResponse.java` dibuat
- [ ] Semua controller menggunakan ApiResponse
- [ ] Import HashMap yang tidak terpakai dihapus
- [ ] `./mvnw clean compile` sukses
- [ ] Endpoint bisa ditest
- [ ] Push ke GitHub
- [ ] Pull Request dibuat
